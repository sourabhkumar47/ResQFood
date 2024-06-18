package com.project.resqfood.presentation.restaurantonboarding

import com.project.resqfood.model.RestaurantType

enum class ListingUIState(val progress: Float){
    RESTAURANT_DETAILS_SCREEN(0.1f),
    CONTACT_DETAILS_SCREEN(0.2f),
}

data class ListingUIStateData(
    val isLoading: Boolean = false,
    val restaurantName: String = "",
    val isRestaurantError: Boolean = false,
    val restaurantError: String = "",
    //Value 4: Address of the restaurant
    val restaurantAddress: AddressEntity = AddressEntity(),
    //Value 5: Does the address have an error
    val isAddressError: Boolean = false,
    //Value 6: The text of the address error
    val addressError: String = "",
    val phoneNumber: String = "",
    val isPhoneNumberError: Boolean = false,
    val phoneNumberError: String = "",
    val alternatePhoneNumber: String = "",
    val isAlternatePhoneNumberError : Boolean = false,
    val alternatePhoneNumberError: String = "",
    val email: String = "",
    val isEmailError: Boolean = false,
    val emailError: String = "",
    val ownerMobileNumber: String = "",
    val isOwnerMobileNumberError: Boolean = false,
    val ownerMobileNumberError: String = "",
    val ownerName: String = "",
    val isOwnerNameError: Boolean = false,
    val ownerNameError: String = "",
    val ownerEmail: String = "",
    val isOwnerEmailError: Boolean = false,
    val ownerEmailError: String = "",
    val isOwnerEmailSameAsRestaurantEmail: Boolean = false,
    val isOwnerMobileSameAsRestaurantMobile: Boolean = false,
    val selectedRestaurantType: List<RestaurantType> = emptyList(),
    val workingDaysList: List<DayOfWeek> = emptyList(),
    val workingDaysRadioOption: RadioButtonOption? = null,
    val timeSlots: List<RestaurantTimeSlot> = listOf(RestaurantTimeSlot()),
)

data class AddressEntity(
    val street: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = ""
)

data class RestaurantTimeSlot(
    val openingTimeHour: Int = 8,
    val openingTimeMinute: Int = 40,
    val closingTimeHour: Int = 12,
    val closingTimeMinute: Int = 0
)