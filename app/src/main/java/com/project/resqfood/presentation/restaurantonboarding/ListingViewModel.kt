package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.resqfood.model.RestaurantType

class ListingViewModel : ViewModel() {
    var uiState = mutableStateOf(ListingUIState.RESTAURANT_DETAILS_SCREEN)
        private set
    var listingData = mutableStateOf(ListingUIStateData())
        private set

    fun onRestaurantNameChanged(restaurantName: String) {
        listingData.value = listingData.value.copy(restaurantName = restaurantName)
    }

    fun onRestaurantErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isRestaurantError = isError, restaurantError = error)
    }

    fun onRestaurantAddressChanged(address: AddressEntity) {
        listingData.value = listingData.value.copy(restaurantAddress = address)
    }

    fun onAddressErrorChanged(isError: Boolean, error: String) {
        listingData.value = listingData.value.copy(isAddressError = isError, addressError = error)
    }

    fun onPhoneNumberChanged(phoneNumber: String) {
        listingData.value = listingData.value.copy(phoneNumber = phoneNumber)
    }

    fun onPhoneNumberErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isPhoneNumberError = isError, phoneNumberError = error)
    }

    fun onAlternatePhoneNumberChanged(alternatePhoneNumber: String) {
        listingData.value = listingData.value.copy(alternatePhoneNumber = alternatePhoneNumber)
    }

    fun onAlternatePhoneNumberErrorChanged(isError: Boolean, error: String) {
        listingData.value = listingData.value.copy(
            isAlternatePhoneNumberError = isError,
            alternatePhoneNumberError = error
        )
    }

    fun onEmailChanged(email: String) {
        listingData.value = listingData.value.copy(email = email)
    }

    fun onEmailErrorChanged(isError: Boolean, error: String) {
        listingData.value = listingData.value.copy(isEmailError = isError, emailError = error)
    }

    fun onOwnerNameChanged(ownerName: String) {
        listingData.value = listingData.value.copy(ownerName = ownerName)
    }

    fun onOwnerNameErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isOwnerNameError = isError, ownerNameError = error)
    }

    fun onOwnerEmailChanged(ownerEmail: String) {
        listingData.value = listingData.value.copy(ownerEmail = ownerEmail)
        onOwnerEmailSameAsRestaurantEmailChanged(listingData.value.isOwnerEmailSameAsRestaurantEmail)
    }

    fun onOwnerEmailErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isOwnerEmailError = isError, ownerEmailError = error)
    }

    fun onOwnerMobileNumberChanged(ownerMobileNumber: String) {
        listingData.value = listingData.value.copy(ownerMobileNumber = ownerMobileNumber)
        onOwnerMobileSameAsRestaurantMobileChanged(listingData.value.isOwnerMobileSameAsRestaurantMobile)
    }

    fun onOwnerMobileNumberErrorChanged(isError: Boolean, error: String) {
        listingData.value = listingData.value.copy(
            isOwnerMobileNumberError = isError,
            ownerMobileNumberError = error
        )
    }

    //TODO : Implement these two while saving the data as well, because edited email cannot be saved
    fun onOwnerEmailSameAsRestaurantEmailChanged(isSame: Boolean) {
        listingData.value = listingData.value.copy(
            isOwnerEmailSameAsRestaurantEmail = isSame,
            ownerEmail = if (isSame) listingData.value.email
            else listingData.value.ownerEmail
        )
    }

    fun onOwnerMobileSameAsRestaurantMobileChanged(isSame: Boolean) {
        listingData.value = listingData.value.copy(
            isOwnerMobileSameAsRestaurantMobile = isSame,
            ownerMobileNumber = if (isSame) listingData.value.phoneNumber
            else listingData.value.ownerMobileNumber
        )
    }

    fun onRestaurantTypeChanged(restaurantType: RestaurantType) {
        if (listingData.value.selectedRestaurantType.contains(restaurantType))
            listingData.value =
                listingData.value.copy(selectedRestaurantType = listingData.value.selectedRestaurantType - restaurantType)
        else
            listingData.value =
                listingData.value.copy(selectedRestaurantType = listingData.value.selectedRestaurantType + restaurantType)
    }

    fun onWorkingDaysChanged(workingDays: DayOfWeek) {
        if (listingData.value.workingDaysList.contains(workingDays))
            listingData.value =
                listingData.value.copy(workingDaysList = listingData.value.workingDaysList - workingDays)
        else
            listingData.value =
                listingData.value.copy(workingDaysList = listingData.value.workingDaysList + workingDays)
    }

    fun onWorkingDaysRadioOptionChanged(radioButtonOption: RadioButtonOption) {
        if (listingData.value.workingDaysRadioOption == radioButtonOption)
            listingData.value = listingData.value.copy(workingDaysRadioOption = null)
        else {
            listingData.value = listingData.value.copy(workingDaysRadioOption = radioButtonOption)
            if (radioButtonOption == RadioButtonOption.Weekdays)
                listingData.value = listingData.value.copy(
                    workingDaysList =
                    listOf(
                        DayOfWeek.Monday,
                        DayOfWeek.Tuesday,
                        DayOfWeek.Wednesday,
                        DayOfWeek.Thursday,
                        DayOfWeek.Friday
                    )
                )
            else if (radioButtonOption == RadioButtonOption.Everyday)
                listingData.value = listingData.value.copy(
                    workingDaysList =
                    listOf(
                        DayOfWeek.Monday,
                        DayOfWeek.Tuesday,
                        DayOfWeek.Wednesday,
                        DayOfWeek.Thursday,
                        DayOfWeek.Friday,
                        DayOfWeek.Saturday,
                        DayOfWeek.Sunday
                    )
                )
        }
    }

    fun onTimeSlotChanged(timeSlot: RestaurantTimeSlot, index: Int) {
        val timeSlots = listingData.value.timeSlots.toMutableList()
        timeSlots[index] = timeSlot
        listingData.value = listingData.value.copy(timeSlots = timeSlots)
    }

    fun addTimeSlot() {
        val timeSlots = listingData.value.timeSlots.toMutableList()
        timeSlots.add(RestaurantTimeSlot())
        listingData.value = listingData.value.copy(timeSlots = timeSlots)
    }

}

fun ListingViewModelFactory(): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ListingViewModel::class.java)) {
                return ListingViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
