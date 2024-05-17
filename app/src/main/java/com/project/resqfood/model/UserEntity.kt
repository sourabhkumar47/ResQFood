package com.project.resqfood.model

data class UserEntity(
    val uid: String,
    val email: String,
    val name: String,
    val phoneNumber: String,
    val gender: String,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val isProfileComplete: Boolean = false,
    val profileUrl : String = ""
    )
