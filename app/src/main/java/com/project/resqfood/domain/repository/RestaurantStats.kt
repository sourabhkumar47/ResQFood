package com.project.resqfood.domain.repository

data class RestaurantOrderStats(
    val completed : Int,
    val pending : Int,
    val cancelled : Int,
)

data class Reviews(
    val userName : String,
    val review : String
)

data class RestaurantFoodSavedStats(
    val goal : Int,
    val saved : Int,
)