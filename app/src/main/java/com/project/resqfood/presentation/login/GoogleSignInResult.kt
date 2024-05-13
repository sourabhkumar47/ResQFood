package com.project.resqfood.presentation.login

data class GoogleSignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val userName: String?,//In case account does not have name
    val profilePictureUrl: String?//In case account does not have profile picture
)
