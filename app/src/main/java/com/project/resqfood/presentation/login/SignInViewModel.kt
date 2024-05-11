package com.project.resqfood.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    //This is the public immutable version because we don't want to expose the mutable state flow
    val state = _state.asStateFlow()
    var storedVerificationId = ""

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
}