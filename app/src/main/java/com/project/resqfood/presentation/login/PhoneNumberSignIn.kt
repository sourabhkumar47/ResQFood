package com.project.resqfood.presentation.login

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.presentation.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PhoneNumberSignIn {

    /**
     * verifyPhoneNumberWithCode is a function that verifies the phone number with the provided verification code.
     *
     * @param auth The FirebaseAuth instance used for authentication.
     * @param context The context used to access resources and services.
     * @param verificationId The verification ID obtained from the Firebase PhoneAuthProvider.
     * @param code The verification code entered by the user.
     * @param onVerificationCompleted A function to be invoked when the verification is completed successfully.
     * @param onInvalidOTP A function to be invoked when the entered OTP is invalid. This is optional and defaults to an empty function.
     * @param onVerificationFailed A function to be invoked when the verification fails. This is optional and defaults to an empty function.
     */
    fun verifyPhoneNumberWithCode(auth: FirebaseAuth, context: Context, verificationId: String,
                                  code: String, onVerificationCompleted:() -> Unit,
                                  onInvalidOTP: () -> Unit = {},onVerificationFailed: () -> Unit){
        try {
            Log.d("PhoneSignIn", "verifyPhoneNumberWithCode: $verificationId and code is $code")
            val credential = PhoneAuthProvider.getCredential(verificationId,code)
            signInWithPhoneAuthCredential(auth, context, credential, onVerificationCompleted, onInvalidOTP, onVerificationFailed)
        }
        catch (e: Exception){
            Log.d("PhoneSignIn", "verifyPhoneNumberWithCode: ${e.message}")
            Toast.makeText(context, "Error,check Log", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * onLoginClicked is a function that initiates the phone number sign-in process.
     *
     * @param auth The FirebaseAuth instance used for authentication.
     * @param context The context used to access resources and services.
     * @param phoneNumber The phone number entered by the user.
     * @param viewModel The SignInViewModel instance used to manage the sign-in state.
     * @param onCodeSent A function to be invoked when the verification code is sent.
     * @param onAutoVerify A function to be invoked when the verification is completed automatically.
     * @param onInvalidRequest A function to be invoked when the verification request is invalid.
     * @param onQuotaExceeded A function to be invoked when the quota for verification requests is exceeded.
     * @param onRecaptchaVerification A function to be invoked when reCAPTCHA verification is required.
     */
    fun onLoginClicked(auth: FirebaseAuth,
                       context: Context,
                       phoneNumber: String, viewModel: SignInViewModel, onCodeSent:() -> Unit
    ,onAutoVerify:() -> Unit, onInvalidRequest: () -> Unit,
                       onQuotaExceeded: () -> Unit,
                       onRecaptchaVerification: () -> Unit){
        auth.setLanguageCode("en")
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                viewModel.addVerificationIds(p0,p1)
                startCountdown()
                Log.d("PhoneSignIn", "onCodeSent: $p0")
                Log.d("InViewModel", "onCodeSent: ${MainActivity.storedVerificationId} ${MainActivity.forceResendingToken}")
                onCodeSent()
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(auth,context, credential, onAutoVerify )
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                Log.d("PhoneSignIN", "onVerificationFailed: ${e.message}")
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        onInvalidRequest()
                    }

                    is FirebaseTooManyRequestsException -> {
                        onQuotaExceeded()
                    }

                    is FirebaseAuthMissingActivityForRecaptchaException -> {
                        // reCAPTCHA verification attempted with null Activity
                        onRecaptchaVerification()
                    }

                    else -> {
                        onInvalidRequest()
                        Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                //Add functionality when code retrieval times out
                Toast.makeText(context, "Time Out, Try Resending", Toast.LENGTH_SHORT).show()
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val options = context.getActivity()?.let {
                    PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(120L, java.util.concurrent.TimeUnit.SECONDS)
                        .setActivity(it)
                        .setCallbacks(callback)
                        .build()
                }

                if(options != null){
                    Log.d("phoneBook", options.toString())
                    withContext(Dispatchers.IO) {
                        PhoneAuthProvider.verifyPhoneNumber(options)
                    }
                }
            } catch (e: Exception) {
                // Handle exception
                Log.w("PhoneSignIn", "verifyPhoneNumber:failure", e)
            }
        }
    }



    private fun signInWithPhoneAuthCredential(auth: FirebaseAuth, context: Context, credential: PhoneAuthCredential,
                                              onVerificationCompleted: () -> Unit,
                                              onInvalidOTP: () -> Unit = {},
                                              onVerificationFailed: () -> Unit = {}){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    auth.signInWithCredential(credential).await()
                }
                // Sign In successful, update UI with signed-in user's information
                Log.d("PhoneSignIn", "Success signInWithPhoneAuthCredential: user")
                onVerificationCompleted()
            } catch (e: Exception) {
                // Sign In failed, display message and update the UI
                Log.w("PhoneSignIn", "signInWithPhoneAuthCredential:failure", e)
                if (e is FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                    onInvalidOTP()
                } else {
                    Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
                    onVerificationFailed()
                }
            }
        }
    }


    /**
     * resendVerificationCode is a function that resends the verification code.
     *
     * @param auth The FirebaseAuth instance used for authentication.
     * @param context The context used to access resources and services.
     * @param phoneNumber The phone number entered by the user.
     * @param viewModel The SignInViewModel instance used to manage the sign-in state.
     * @param onVerificationCompleted A function to be invoked when the verification is completed successfully.
     * @param onRecaptchaVerification A function to be invoked when reCAPTCHA verification is required.
     * @param onQuotaExceeded A function to be invoked when the quota for verification requests is exceeded.
     * @param onInvalidRequest A function to be invoked when the verification request is invalid.
     * @param onCodeSent A function to be invoked when the verification code is sent.
     */
    fun resendVerificationCode(auth: FirebaseAuth, context: Context, phoneNumber: String,viewModel: SignInViewModel
    ,onVerificationCompleted: () -> Unit, onRecaptchaVerification: () -> Unit, onQuotaExceeded: () -> Unit, onInvalidRequest: () -> Unit,
                               onCodeSent: () -> Unit){
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                viewModel.addVerificationIds(p0,p1)
                Toast.makeText(context,"Code sent successfully", Toast.LENGTH_SHORT).show()
                onCodeSent()
                Log.d("PhoneSignIn", "onCodeSent: $p0")
                MainActivity.isResendButtonEnabled.value = false
                startCountdown()
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(auth,context, p0, onVerificationCompleted)

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                Log.d("PhoneSignIN", "onVerificationFailed: ${p0.message}")
                when (p0) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        onInvalidRequest()
                    }

                    is FirebaseTooManyRequestsException -> {
                        onQuotaExceeded()
                    }

                    is FirebaseAuthMissingActivityForRecaptchaException -> {
                        // reCAPTCHA verification attempted with null Activity
                        onRecaptchaVerification()
                    }
                }
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                //Add functionality when code retrieval times out if user is not authenticated
                if(auth.currentUser == null)
                    Toast.makeText(context, "Time Out, Try Resending", Toast.LENGTH_SHORT).show()
            }
        }
        val options = context.getActivity()?.let {
            MainActivity.forceResendingToken?.let { it1 ->
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(120L, java.util.concurrent.TimeUnit.SECONDS)
                    .setActivity(it)
                    .setCallbacks(callback)
                    .setForceResendingToken(it1)
                    .build()
            }
        }

        if(options != null){
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }


    /**
     * startCountdown is a function that starts a countdown for resending the verification code.
     */
    fun startCountdown(){
        object : CountDownTimer(60000,1000){
            override fun onTick(millisUntilFinished: Long){
                CoroutineScope(Dispatchers.Main).launch {
                    MainActivity.countDownTime.value = (millisUntilFinished / 1000).toInt()
                }
            }
            override fun onFinish(){
                CoroutineScope(Dispatchers.Main).launch{
                MainActivity.isResendButtonEnabled.value = true
                Log.d("PhoneSignIn", "onFinish: ${MainActivity.isResendButtonEnabled}")
                }
            }
        }.start()
    }

}