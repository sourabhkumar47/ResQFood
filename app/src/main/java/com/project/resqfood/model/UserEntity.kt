package com.project.resqfood.model

data class UserEntity(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var phoneNumber: String = "",
    var gender: String = "",
    var isEmailVerified: Boolean = false,
    var isPhoneVerified: Boolean = false,
    var isProfileComplete: Boolean = false,
    var profileUrl : String = "",
    var houseNumber: String = "",
    var street: String = "",
    var city: String = "",
    var state: String = "",
    var pincode: String = "",
    var locality: String = ""
    )
