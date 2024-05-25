package com.project.resqfood.presentation

sealed class Destinations(val route: String) {
    data object Onboarding:Destinations("onboarding")
    data object SignIn: Destinations("signIn")
    data object OtpVerification: Destinations("otpVerification")
    data object EmailSignIn: Destinations("emailSignIn")
    data object ForgotPassword: Destinations("forgotPassword")
    data object MainScreen: Destinations("MainScreen")
    data object PersonalDetails: Destinations("personaldetails")
    data object WaitScreen: Destinations("waitScreen")
    data object Profile: Destinations("profile")
}