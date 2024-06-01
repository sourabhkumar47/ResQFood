package com.project.resqfood.presentation.login.mainlogin

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.project.resqfood.presentation.MainActivity
import com.project.resqfood.presentation.login.PhoneNumberSignIn
import com.project.resqfood.presentation.login.SignInDataViewModel
import kotlinx.serialization.Serializable


@Serializable
object NavOTPVerificationUI
/**
 * OTPVerificationUI is a composable function that provides the UI for the OTP verification process.
 * It includes a field for entering the OTP and a button for verifying the OTP.
 *
 * @param navController The NavController used for navigation.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTPVerificationUI(navController: NavController){
    val scrollable = rememberScrollState()
    var isLoading by remember {
        mutableStateOf(false)
    }
    val countDownTime by MainActivity.countDownTime.collectAsState(initial = 60000)
    val isResendEnabled by MainActivity.isResendButtonEnabled.collectAsState(initial = false)
    val phoneNumberSignIn = PhoneNumberSignIn()
    var otp by remember {
        mutableStateOf("")
    }
    val viewModel: SignInDataViewModel = viewModel()
    val context = LocalContext.current
    val onVerificationComplete = {
        onSignInSuccessful(navController)
        isLoading = false
    }
    val onClickVerifyOTP = {
        isLoading = true
        if (MainActivity.storedVerificationId.isEmpty()) {
            Toast.makeText(context, "Verification Id is empty", Toast.LENGTH_SHORT)
                .show()
        }
        else {
            phoneNumberSignIn.verifyPhoneNumberWithCode(
                FirebaseAuth.getInstance(),
                context,
                MainActivity.storedVerificationId,
                otp,
                onVerificationCompleted = onVerificationComplete,
                onVerificationFailed = {
                    isLoading = false
                },
                onInvalidOTP = {
                    isLoading = false
                }
            )
        }
    }
    @Composable
    fun TopAppBarOTP(){
        TopAppBar(title = {
            Text(text = "OTP Verification")
        },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
    }
    Scaffold(
        topBar = {
            TopAppBarOTP()
        }
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(scrollable),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(isLoading)
                    Wait()
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "We have sent a verification code to")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = MainActivity.phoneNumber)
                Spacer(modifier = Modifier.height(48.dp))
                Text(text = "Enter OTP")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = otp, onValueChange = {
                    otp = it
                },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onClickVerifyOTP()
                        })
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onClickVerifyOTP,
                    enabled = !isLoading
                ) {
                    Text(text = "Verify OTP")
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    isLoading = true
                    phoneNumberSignIn.resendVerificationCode(
                        FirebaseAuth.getInstance(),
                        context,
                        MainActivity.phoneNumber,
                        viewModel,
                        onVerificationCompleted = onVerificationComplete,
                        onInvalidRequest = {
                            Toast.makeText(context, "Invalid Request", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        },
                        onQuotaExceeded = {
                            Toast.makeText(context, "Quota Exceeded", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        },
                        onRecaptchaVerification = {
                            Toast.makeText(context, "Recaptcha Verification", Toast.LENGTH_SHORT)
                                .show()
                            isLoading = false
                        },
                        onCodeSent = {
                            MainActivity.isResendButtonEnabled.value = false
                            isLoading = false
                        }
                    )
                }, enabled = isResendEnabled && !isLoading) {
                    Text(
                        text =
                        if (isResendEnabled) "Resend OTP" else "Resend OTP in $countDownTime seconds"
                    )
                }
                Spacer(modifier = Modifier.height(54.dp))
                TextButton(onClick = {
                    navController.popBackStack()
                }) {
                    Text(text = "Go back to Login Methods")
                }
            }
        }
    }
}
