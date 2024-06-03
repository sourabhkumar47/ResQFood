package com.project.resqfood.presentation.login.mainlogin

data class MainSignInUIState(
    val phoneNumber: String = "",
    val isPhoneNumberValid: Boolean = true,
    val phoneNumberError: String = "",
    val isLoading: Boolean = false,
    val otp: String = "",
    val isOtpValid: Boolean = true,
    val otpError: String = "",
)
