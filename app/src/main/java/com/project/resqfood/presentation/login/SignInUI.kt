package com.project.resqfood.presentation.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun SignInUI(
    state: SignInState,
    onGoogleSignInClick: () -> Unit,
    navController: NavController
) {
    val viewModel: SignInViewModel = viewModel()
    val context = LocalContext.current
    val phoneNumberSignIn = PhoneNumberSignIn()
    //This launched effect is for google sign in
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    val auth = FirebaseAuth.getInstance()

    var phoneNumber by remember {
        mutableStateOf("")
    }

    Scaffold {
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Phone Number") })
        Button(onClick = {
            phoneNumberSignIn.onLoginClicked(auth, context, phoneNumber,viewModel = viewModel){
                //TODO(Make the OTP verification UI visible)
                navController.navigate(Destinations.OtpVerification.route)
            }
        }) {
            Text(text = "Send OTP")
        }
        Column(modifier = Modifier.padding(it)) {
            Button(onClick = onGoogleSignInClick) {
                Text(text = "Sign In With Google")
            }
        }
    }

}

@Composable
fun OTPVerificationUI(navController: NavController){
    val phoneNumberSignIn = PhoneNumberSignIn()
    var otp by remember {
        mutableStateOf("")
    }
    val viewModel: SignInViewModel = viewModel()
    val context = LocalContext.current
    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            OutlinedTextField(value = otp, onValueChange = {
                otp = it
            })
        }
        Button(onClick = { /*TODO*/
             phoneNumberSignIn.verifyPhoneNumberWithCode(FirebaseAuth.getInstance(), context,viewModel.storedVerificationId ,otp)
//            Toast.makeText(context, "OTP Verified", Toast.LENGTH_SHORT).show()
        }) {
            Text(text ="Verify OTP")
        }
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Resend OTP")
        }
        TextButton(onClick = { /*TODO*/ }) {
            Text(text = "Go back to Login Methods")
        }
    }


}

fun Context.getActivity(): Activity ?= when(this){
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}
@Composable
fun Test(){
    Surface {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = "Success")
        }
    }
}

@Composable
fun Wait(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}