package com.project.resqfood.presentation.login.mainlogin

import android.content.Context
import android.os.CountDownTimer
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.R
import com.project.resqfood.domain.services.AccountService
import com.project.resqfood.presentation.MainActivity
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class MainSignInViewModel(val auth: FirebaseAuth, val accountService: AccountService): ViewModel() {
    var uiState = mutableStateOf(MainSignInUIState())
    private set

    var storedVerificationId = ""
        private set

    var forceResendingToken : PhoneAuthProvider.ForceResendingToken? = null
        private set

    var countDownTime = mutableStateOf(0)
        private set
    var otpState = mutableStateOf(OTPState.NOT_SENT_ONCE)
    private set

    fun addVerificationIds(verificationId: String, forceResendingTokenIn: PhoneAuthProvider.ForceResendingToken){
        storedVerificationId = verificationId
        forceResendingToken = forceResendingTokenIn
    }

    var countDownTimer: CountDownTimer? = null

    fun startCountDown(){
        stopCountDown()
        otpState.value = OTPState.OTP_TIMER_RUNNING
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModelScope.launch {
                    countDownTime.value = millisUntilFinished.toInt()
                }
            }

            override fun onFinish() {
                viewModelScope.launch {
                    countDownTime.value = 0
                    otpState.value = OTPState.OTP_TIMER_STOPPED
                }
            }
        }.start()
    }

    fun stopCountDown(){
        countDownTimer?.cancel()
        countDownTime.value = 0
    }

    fun setIsLoading(isLoading: Boolean) {
        uiState.value = uiState.value.copy(isLoading = isLoading)
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        uiState.value = uiState.value.copy(phoneNumber = "+91$newPhoneNumber")
    }

    fun onPhoneNumberErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value = uiState.value.copy(isPhoneNumberValid = !isError, phoneNumberError = errorText)
    }

    fun otpChange(newOtp: String){
        uiState.value = uiState.value.copy(otp = newOtp)
    }

    fun otpErrorStateChange(isError: Boolean, errorText: String){
        uiState.value = uiState.value.copy(isOtpValid = !isError, otpError = errorText)
    }

    fun loginAnonymously(snackbarHostState: SnackbarHostState, context: Context, navController: NavController){
        if(uiState.value.isLoading)
            return
        setIsLoading(true)
        viewModelScope.launch {
            accountService.signInAnonymously(context,auth) { task ->
                setIsLoading(false)
                if (task.isSuccessful) {
                    MainActivity.isUserAnonymous.value = true
                    navController.navigate(NavMainScreen)
                } else if (task.exception != null) {
                    showSnackbar(snackbarHostState, task.exception!!.message ?: "Failed to login")
                } else {
                    showSnackbar(snackbarHostState, "Failed to login")
                }
            }
        }
    }

    private fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun resendOTPButton(context: Context, snackbarHostState: SnackbarHostState, navController: NavController){
        if(uiState.value.isLoading)
            return
        otpState.value = OTPState.RESENDING_OTP
        setIsLoading(true)
        val callback = accountService.sendCallback(
            onCodeTimeOut = {
                otpState.value = OTPState.OTP_TIMER_STOPPED
                setIsLoading(false)
                showSnackbar(snackbarHostState, "Time out, try resending")
            },
            onCodeSent = {verificationId, forceResendingToken->
                addVerificationIds(storedVerificationId,forceResendingToken)
                startCountDown()
                setIsLoading(false)
            },
            onVerificationCompleted = { credential ->
                viewModelScope.launch {
                    accountService.phoneNumberSignInWithCredential(auth, credential) { task ->
                        setIsLoading(false)
                        if (task.isSuccessful) {
                            onSignInSuccessful(navController)
                        } else if (task.exception != null) {
                            otpState.value = OTPState.OTP_TIMER_STOPPED
                            showSnackbar(
                                snackbarHostState,
                                task.exception!!.message ?: "Failed to login"
                            )
                        } else {
                            otpState.value = OTPState.OTP_TIMER_STOPPED
                            showSnackbar(snackbarHostState, "Failed to login")
                        }
                    }
                }
            },
            onVerificationFailed = {e: FirebaseException ->
                otpState.value = OTPState.OTP_TIMER_STOPPED
                setIsLoading(false)
                showSnackbar(snackbarHostState, e.message ?: "Verification Failed")
            }

        )
        viewModelScope.launch {
            if(forceResendingToken != null) {
                try {
                    accountService.reSendOTP(
                        context,
                        auth,
                        uiState.value.phoneNumber,
                        120L,
                        callback,
                        forceResendingToken!!
                    )
                } catch (e: Exception) {
                    setIsLoading(false)
                    otpState.value = OTPState.NOT_SENT_ONCE
                    showSnackbar(snackbarHostState, e.message ?: "Failed to send OTP")
                }
            }
        }
    }

    fun sendOTPbutton(context: Context,snackbarHostState: SnackbarHostState, navController: NavController){
        if(uiState.value.isLoading)
            return
        otpState.value = OTPState.SENDING_OTP_FIRST_TIME
        setIsLoading(true)
        auth.setLanguageCode("en")
        val callback = accountService.sendCallback(
            onCodeTimeOut = {
                otpState.value = OTPState.NOT_SENT_ONCE
                setIsLoading(false)
                showSnackbar(snackbarHostState, "Time out, try resending")
            },
            onCodeSent = {verificationId, forceResendingToken->
                addVerificationIds(verificationId,forceResendingToken)
                startCountDown()
                setIsLoading(false)
                navController.navigate(NavOTPVerificationUI)
            },
            onVerificationCompleted = { credential ->
                viewModelScope.launch {
                    accountService.phoneNumberSignInWithCredential(auth, credential) { task ->
                        setIsLoading(false)
                        if (task.isSuccessful) {
                            onSignInSuccessful(navController)
                        } else if (task.exception != null) {
                            showSnackbar(
                                snackbarHostState,
                                task.exception!!.message ?: "Failed to login"
                            )
                        } else {
                            showSnackbar(snackbarHostState, "Failed to login")
                        }
                    }
                }
            },
            onVerificationFailed = {e: FirebaseException ->
                otpState.value = OTPState.NOT_SENT_ONCE
                setIsLoading(false)
                showSnackbar(snackbarHostState, e.message ?: "Verification Failed")
            }

        )
        viewModelScope.launch {
            try {
                accountService.sendOTP(context, auth, uiState.value.phoneNumber, 120L, callback)
            }catch (e: Exception){
                setIsLoading(false)
                otpState.value = OTPState.NOT_SENT_ONCE
                showSnackbar(snackbarHostState, e.message ?: "Failed to send OTP")
            }
        }
    }

    fun otpVerifyButton(navController: NavController, snackbarHostState: SnackbarHostState){
        if(uiState.value.isLoading)
            return
        setIsLoading(true)
        otpState.value = OTPState.VERIFYING_OTP
        try {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId, uiState.value.otp)
        viewModelScope.launch {
            accountService.phoneNumberSignInWithCredential(auth, credential) { task ->
                setIsLoading(false)
                if (task.isSuccessful) {
                    otpState.value = OTPState.OTP_TIMER_STOPPED
                    onSignInSuccessful(navController)
                } else if (task.exception != null) {
                    otpState.value = OTPState.OTP_TIMER_STOPPED
                    showSnackbar(
                        snackbarHostState,
                        task.exception!!.message ?: "Failed to login"
                    )
                } else {
                    showSnackbar(snackbarHostState, "Failed to login")
                }
            }
        }
        }catch (e: Exception){
            setIsLoading(false)
            otpState.value = if(countDownTime.value > 0)OTPState.OTP_TIMER_RUNNING else OTPState.OTP_TIMER_STOPPED
            showSnackbar(snackbarHostState, e.message ?: "Failed to verify OTP")
        }
    }

    fun googleLogIn(context: Context,snackbarHostState: SnackbarHostState, onSuccess: () -> Unit){
        uiState.value = uiState.value.copy(isLoading = true)
        val credentialManager = CredentialManager.create(context)
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setNonce(generateRandomNonce())
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(/* credentialOption = */ googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                accountService.googleSignIn(auth, authCredential) { task ->
                    uiState.value = uiState.value.copy(isLoading = false)
                    if (task.isSuccessful) {
                        showSnackbar(snackbarHostState, "Successfully logged in")
                        onSuccess()
                    } else if (task.exception != null) {
                        showSnackbar(snackbarHostState, task.exception!!.message ?: "Failed to login")
                    } else {
                        showSnackbar(snackbarHostState, "Failed to login")
                    }
                }
            }
            catch (e: Exception){
                showSnackbar(snackbarHostState, e.message ?: "Failed to login")
                uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun generateRandomNonce(): String {
        val ranNonce = UUID.randomUUID().toString()

        val bytes = ranNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
        return hashedNonce
    }
}

enum class OTPState(
    val isLoading: Boolean,
    val isLoginButtonEnabled: Boolean,
    val isResendOTPButtonEnable: Boolean,
    val isSendOTPButtonEnabled: Boolean
) {
    NOT_SENT_ONCE(
        isLoading = false,
        isResendOTPButtonEnable = false,
        isLoginButtonEnabled = false,
        isSendOTPButtonEnabled = true
    ),
    SENDING_OTP_FIRST_TIME(
        isLoading = true,
        isResendOTPButtonEnable = false,
        isLoginButtonEnabled = false,
        isSendOTPButtonEnabled = false
    ),
    OTP_TIMER_RUNNING(
        isLoading = false,
        isResendOTPButtonEnable = false,
        isLoginButtonEnabled = true,
        isSendOTPButtonEnabled = false
    ),
    OTP_TIMER_STOPPED(
        isLoading = false,
        isResendOTPButtonEnable = true,
        isLoginButtonEnabled = true,
        isSendOTPButtonEnabled = false
    ),
    RESENDING_OTP(
        isLoading = true,
        isResendOTPButtonEnable = false,
        isLoginButtonEnabled = false,
        isSendOTPButtonEnabled = false
    ),
    VERIFYING_OTP(
        isLoading = true,
        isResendOTPButtonEnable = false,
        isLoginButtonEnabled = false,
        isSendOTPButtonEnabled = false
    )
}

class MainSignInViewModelFactory(val auth:FirebaseAuth, val accountService: AccountService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainSignInViewModel::class.java))
            return MainSignInViewModel(auth, accountService) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}