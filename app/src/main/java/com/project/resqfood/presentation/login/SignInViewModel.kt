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

    private val _state = MutableStateFlow(SignInState())
    //This is the public immutable version because we don't want to expose the mutable state flow
    val state = _state.asStateFlow()

    fun onGoogleSignInResult(result: GoogleSignInResult){
        if(result.data != null){
            _state.value = SignInState(isSignInSuccessful = true)
        }else{
            _state.value = SignInState(signInError = result.errorMessage)
        }

    }


    fun resetSate(){
        _state.update {
            SignInState()
        }
    }

    fun addVerificationIds(verificationId: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken){
        MainActivity.storedVerificationId = verificationId
        MainActivity.forceResendingToken = forceResendingToken
    }
}