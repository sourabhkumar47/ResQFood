package com.project.resqfood.presentation.searchFilter.model

enum class Tab {
    FILTER,
    SORT
}

data class FilterTab(
    val tab: Tab,
    val isSelected: Boolean
)

enum class CuisineType(val description: String) {
    NORTH_INDIAN("North Indian"),
    SOUTH_INDIAN("South Indian"),
    CHINESE("Chinese"),
    ITALIAN("Italian"),
    FAST_FOOD("Fast Food"),
    CAFE("Cafe"),
    CONTINENTAL("Continental"),
    DESSERT("Dessert")
}

data class SortOption(
    val type: SortType,
    val isSelected: Boolean
)

enum class SortType(val displayName: String) {
    RELEVANCE("Relevance"),
    PRICE_LOW_TO_HIGH("Price Low to High"),
    PRICE_HIGH_TO_LOW("Price High to Low"),
    NEAREST_FIRST("Nearest First"),
    PICKUP_TIME_SOONEST("Pickup Time Soonest")
}
