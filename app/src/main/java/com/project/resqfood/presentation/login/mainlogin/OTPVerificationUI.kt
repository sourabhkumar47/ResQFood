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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.project.resqfood.presentation.login.SignInDataViewModel
import com.project.resqfood.presentation.login.otpCheck
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
fun OTPVerificationUI(mainSignInViewModel: MainSignInViewModel,navController: NavController){
    val scrollable = rememberScrollState()
    val context = LocalContext.current
    val mainUi by mainSignInViewModel.uiState
    val otpState by mainSignInViewModel.otpState
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    if(!mainUi.isOtpValid)
        otpCheck(mainUi.otp, mainSignInViewModel::otpErrorStateChange)
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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
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
                if(otpState.isLoading)
                    Wait()
                Spacer(modifier = Modifier.height(32.dp))
                Text(text = "We have sent a verification code to")
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = mainUi.phoneNumber)
                Spacer(modifier = Modifier.height(48.dp))
                Text(text = "Enter OTP")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = mainUi.otp, onValueChange = mainSignInViewModel::otpChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions(),
                    isError = !mainUi.isOtpValid,
                    supportingText = {
                        if(!mainUi.isOtpValid)
                            Text(text = mainUi.otpError)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if(otpCheck(mainUi.otp, mainSignInViewModel::otpErrorStateChange))
                            return@Button
                        mainSignInViewModel.otpVerifyButton(navController, snackbarHostState =snackbarHostState )
                    },
                    enabled = otpState.isLoginButtonEnabled
                ) {
                    Text(text = "Verify OTP")
                }
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = {
                    mainSignInViewModel.resendOTPButton(context,snackbarHostState,navController)
                }, enabled = otpState.isResendOTPButtonEnable && !otpState.isLoading) {
                    Text(
                        text =
                        if (otpState.isResendOTPButtonEnable) "Resend OTP"
                        else if(otpState.isLoading) "Resend OTP"
                        else "Resend OTP in ${mainSignInViewModel.countDownTime.value/1000} seconds"
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
