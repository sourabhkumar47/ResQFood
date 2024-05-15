package com.project.resqfood.presentation.login

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.MainActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    fun addVerificationIds(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken){
        MainActivity.storedVerificationId = verificationId
        MainActivity.forceResendingToken = forceResendingToken
    }
}