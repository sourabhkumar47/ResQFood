package com.project.resqfood.presentation.login

import androidx.core.text.isDigitsOnly


fun nameCheck(name: String, error: (Boolean, String)-> Unit): Boolean{
    if(name.isEmpty()){
        error(true, "Name cannot be empty")
        return true
    }
    error(false, "")
    return false
}

fun emailCheck(email: String, error: (Boolean, String) -> Unit): Boolean{
    if(isValidEmail(email)){
        error(false, "")
        return false
    }
    error(true, "Invalid email format")
    return true
}

fun phoneNumberCheck(phoneNumber: String, error: (Boolean, String) -> Unit, containsStart: Boolean = true): Boolean{
    if(isValidPhoneNumber(if(containsStart)phoneNumber else "+91$phoneNumber")){
        error(false, "")
        return false
    }
    error(true, "Invalid phone number format")
    return true
}

fun otpCheck(otp: String, error: (Boolean, String) -> Unit): Boolean{
    if(otp.length == 6 && otp.isDigitsOnly()){
        error(false, "")
        return false
    }
    error(true, "OTP must be 6 digits long number")
    return true
}

fun passwordCheck(password: String, error: (Boolean, String)-> Unit):Boolean{
    val errorText = validatePassword(password)
    if(errorText == null){
        error(false, "")
        return false
    }
    error(true, errorText)
    return true
}

fun confirmPasswordCheck(password: String, confirmPassword: String,
                                 error: (Boolean, String) -> Unit):Boolean{
    if(password != confirmPassword){
        error(true, "Passwords must match")
        return true
    }
    error(false, "")
    return false
}

fun isValidPhoneNumber(phoneNumber: String): Boolean {
    val regex = "^\\+91[0-9]{10}$".toRegex()
    return regex.matches(phoneNumber)
}

/**
 * validatePassword is a function that validates a password based on certain criteria.
 *
 * @param password The password to be validated.
 * @return A string containing the validation error message if the password is invalid, or null if the password is valid.
 */
fun validatePassword(password: String): String? {
    if (password.length < 8) {
        return "Password should be at least 8 characters long"
    }
    if (!password.any { it.isUpperCase() }) {
        return "Password should contain at least one uppercase letter"
    }
    if (!password.any { it.isLowerCase() }) {
        return "Password should contain at least one lowercase letter"
    }
    if (!password.any { it.isDigit() }) {
        return "Password should contain at least one digit"
    }
    if (!password.contains(Regex("[@#$%^&+=]"))) {
        return "Password should contain at least one special character (@, #, $, %, ^, &, +, =)"
    }
    return null
}


/**
 * isValidEmail is a function that checks if an email is valid.
 *
 * @param email The email to be checked.
 * @return A boolean indicating whether the email is valid.
 */
fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return email.matches(emailRegex.toRegex())
}