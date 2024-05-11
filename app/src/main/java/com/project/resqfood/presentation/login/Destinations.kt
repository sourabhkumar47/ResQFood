package com.project.resqfood.presentation.login

sealed class Destinations(val route: String) {
    object SignIn: Destinations("signIn")
    object OtpVerification: Destinations("otpVerification")

}