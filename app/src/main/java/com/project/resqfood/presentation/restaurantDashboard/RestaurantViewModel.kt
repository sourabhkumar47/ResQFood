package com.project.resqfood.presentation.restaurantDashboard

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.project.resqfood.model.OrderData
import com.project.resqfood.model.OrderStatus
import com.project.resqfood.model.OrderSummary
import com.project.resqfood.model.RestaurantEntity
import com.project.resqfood.model.RestaurantFoodSavedData
import com.project.resqfood.model.RestaurantOrderStats
import com.project.resqfood.model.RestaurantReview
import com.project.resqfood.model.UserEntity
import com.project.resqfood.presentation.restaurantonboarding.AddressEntity

class RestaurantViewModel : ViewModel() {

    private var _restaurant by  mutableStateOf(RestaurantEntity())
    val restaurant : RestaurantEntity get()  = _restaurant

    val restaurantName : String get() = _restaurant.restaurantName
    val restaurantAddress : AddressEntity get() = _restaurant.restaurantAddress

    private var _restaurantFood by mutableStateOf(RestaurantFoodSavedData())
    val restaurantFood : RestaurantFoodSavedData get() = _restaurantFood

    val goal : Int get() = restaurantFood.goal
    val saved : Int get() = restaurantFood.saved
    val remaining : Int get() = restaurantFood.remaining

    private var _restaurantOrder by mutableStateOf(listOf<RestaurantOrderStats>())
    val restaurantOrder : List<RestaurantOrderStats> get() = _restaurantOrder

    private var _summary by mutableStateOf(OrderSummary())
    val summary: OrderSummary get() = _summary

    fun setOrder(order : List<RestaurantOrderStats>){
        _restaurantOrder = order

        val completedCount = restaurantOrder.count{ it.status == OrderStatus.COMPLETED }
        val pendingCount = restaurantOrder.count{ it.status == OrderStatus.PENDING}
        val cancelledCount = restaurantOrder.count{ it.status == OrderStatus.CANCELLED }

        _summary = OrderSummary(
            completed = completedCount,
            pending = pendingCount,
            cancelled = cancelledCount
        )
    }

    private var _restaurantReview by mutableStateOf(RestaurantReview())
    val restaurantReview : RestaurantReview get() = _restaurantReview

    private var _restaurantReviews by mutableStateOf(listOf<RestaurantReview>())
    val restaurantReviews : List<RestaurantReview>  get() = _restaurantReviews

    fun addReview(userName: String, userReview: String) {
        val review = RestaurantReview(userName, userReview)
        _restaurantReviews = _restaurantReviews + review
    }

    val userName : String get() = restaurantReview.userName
    val userReview : String get() = restaurantReview.userReview

    private  var _order by mutableStateOf(OrderData())
    val order : OrderData get() = _order

    private var _user by mutableStateOf(UserEntity())
    val user : UserEntity get() = _user

    val orderName : String get() = order.orderName
    val customerName : String get() = user.name
    val orderQuantity : Int get() = order.orderQuantity

    init {
        _restaurant = RestaurantEntity(
            restaurantName = "The Velvet Spoon",
            restaurantAddress = AddressEntity("1471 Marcelline", "Rosemont", "San Aurelio", "NY 10384")
        )

        _restaurantFood = RestaurantFoodSavedData(
            goal = 15,
            saved = 9
        )

        val testOrders = listOf(
            RestaurantOrderStats(1, OrderStatus.COMPLETED),
            RestaurantOrderStats(2, OrderStatus.PENDING),
            RestaurantOrderStats(3, OrderStatus.CANCELLED),
            RestaurantOrderStats(4, OrderStatus.PENDING),
            RestaurantOrderStats(5, OrderStatus.COMPLETED),
            RestaurantOrderStats(6, OrderStatus.COMPLETED),
            RestaurantOrderStats(7, OrderStatus.COMPLETED),
            RestaurantOrderStats(8, OrderStatus.PENDING),
            RestaurantOrderStats(9, OrderStatus.COMPLETED),
            RestaurantOrderStats(10, OrderStatus.COMPLETED),
            RestaurantOrderStats(11, OrderStatus.CANCELLED),
            RestaurantOrderStats(12, OrderStatus.COMPLETED),
        )
        setOrder(testOrders)

        _restaurantReviews = listOf(
            RestaurantReview(
                userName = "Aanya Deshmukh",
                userReview = "The place had a calm vibe , love it so much"
            )
        )

        _user = UserEntity(name = "Aanya Deshmukh")
        _order = OrderData(orderName = "Pizza", orderQuantity = 3)
    }
}



















