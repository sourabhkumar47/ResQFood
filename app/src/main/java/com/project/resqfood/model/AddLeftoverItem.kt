package com.project.resqfood.model

import android.net.Uri
import java.time.LocalDateTime
import java.util.Locale

data class AddLeftoverItem(
    val id: String = "",
    var name: String = "",
    var description: String = "",
    var category:FoodCategory = FoodCategory.APPETIZERS,
    val quantity: String = "",
    var estimatedPrice: Int = 0,
    var pickUpTimeWindow: Pair<LocalDateTime, LocalDateTime> = Pair(LocalDateTime.now(), LocalDateTime.now()),
    var images: List<Uri> = emptyList()
)

enum class FoodCategory(val examples: String) {
    APPETIZERS("Spring Rolls, Mozzarella Sticks, Bruschetta"),
    MAIN_COURSES("Grilled Chicken, Spaghetti Bolognese, Beef Stroganoff"),
    DESSERTS("Chocolate Cake, Tiramisu, Apple Pie"),
    SALADS("Caesar Salad, Greek Salad, Cobb Salad"),
    SOUPS("Tomato Soup, Chicken Noodle Soup, Minestrone"),
    SIDE_DISHES("Mashed Potatoes, Steamed Vegetables, Rice Pilaf"),
    BEVERAGES("Coffee, Lemonade, Iced Tea"),
    BREAD_AND_PASTRIES("Garlic Bread, Croissants, Muffins"),
    SEAFOOD("Grilled Salmon, Shrimp Cocktail, Fish Tacos"),
    VEGAN_VEGETARIAN("Vegan Burger, Quinoa Salad, Vegetable Stir-fry"),
    SANDWICHES_AND_WRAPS("Turkey Club, Veggie Wrap, BLT"),
    BREAKFAST_ITEMS("Pancakes, Omelettes, French Toast"),
    SNACKS("Chips and Salsa, Popcorn, Mixed Nuts"),
    CONDIMENTS_AND_SAUCES("Ketchup, Ranch Dressing, Hummus"),
    SPECIALTY_ITEMS("Sushi Rolls, Tacos, BBQ Ribs");

    override fun toString(): String {
        return name.replace("_", " ").lowercase(Locale.ROOT)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
    }
}

