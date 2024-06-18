package com.project.resqfood.model

enum class RestaurantType(val description: String) {
    NORTH_INDIAN("North Indian"),
    SOUTH_INDIAN("South Indian"),
    CHINESE("Chinese"),
    ITALIAN("Italian"),
    MEXICAN("Mexican"),
    FAST_FOOD("Fast Food"),
    CAFE("Cafe"),
    BAKERY("Bakery"),
    STREET_FOOD("Street Food"),
    SEAFOOD("Seafood"),
    MUGHLAI("Mughlai"),
    CONTINENTAL("Continental"),
    DESSERT("Dessert"),
    BBQ_GRILL("BBQ & Grill"),
    HEALTHY_FOOD("Healthy Food");

    override fun toString(): String {
        return description
    }
}

fun main() {
    for (type in RestaurantType.values()) {
        println(type)
    }
}

