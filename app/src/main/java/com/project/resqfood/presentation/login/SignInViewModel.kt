package com.project.resqfood.presentation.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.MainActivity

class SignInViewModel: ViewModel() {

    fun addVerificationIds(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken){
        MainActivity.storedVerificationId = verificationId
        MainActivity.forceResendingToken = forceResendingToken
    }
}