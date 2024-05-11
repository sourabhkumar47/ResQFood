package com.project.resqfood.presentation.login

data class SignInState(
    //So that we can proceed to the next screen when sign in is successful
    val isSignInSuccessful: Boolean = false,
    //So that we can show the error message when sign in fails
    val signInError: String? = null
)
