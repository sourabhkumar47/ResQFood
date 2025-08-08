package com.project.resqfood.model

data class RestaurantFoodSavedData(
    val goal : Int = 15,
    val saved : Int = 0,
    val remaining : Int = goal - saved
)

data class RestaurantOrderStats(
    val id : Int,
    val status : OrderStatus
)

enum class OrderStatus{
    COMPLETED,
    PENDING,
    CANCELLED
}

data class OrderSummary(
    val completed: Int = 0,
    val pending: Int = 0,
    val cancelled: Int = 0
)

data class RestaurantReview(
    val userName : String =  "",
    val userReview : String = ""
)

data class OrderData(
    val orderName : String = "",
    val customerName : String = "",
    val orderQuantity : Int = 0
)