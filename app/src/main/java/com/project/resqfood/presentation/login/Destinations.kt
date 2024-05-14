package com.project.resqfood.presentation.login

sealed class Destinations(val route: String) {
    object SignIn: Destinations("signIn")
    object OtpVerification: Destinations("otpVerification")
    object Temporary: Destinations("temporary")
    object EmailSignIn: Destinations("emailSignIn")
    object ForgotPassword: Destinations("forgotPassword")
    object MainScreen: Destinations("MainScreen")
}