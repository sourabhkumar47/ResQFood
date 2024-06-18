package com.project.resqfood.presentation.restaurantonboarding

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.project.resqfood.model.RestaurantType
import com.project.resqfood.presentation.login.emailCheck
import com.project.resqfood.presentation.login.passwordCheck
import com.project.resqfood.presentation.login.phoneNumberCheck
import com.project.resqfood.presentation.login.validatePassword
import kotlinx.coroutines.launch

class ListingViewModel : ViewModel() {
    var uiState = mutableStateOf(ListingUIState.RESTAURANT_DETAILS_SCREEN)
        private set
    var listingData = mutableStateOf(ListingUIStateData())
        private set
    val order = ListingUIState.entries.toList()
    var isStart = true
    private var currentIndex = 0

    fun onNextClick(snackbarHostState: SnackbarHostState){
        isStart = false
        when(currentIndex){
            in 0..(order.size-3) -> {
                if (validateRestaurantDetails(snackbarHostState)) {
                    currentIndex++
                    uiState.value = order[currentIndex]
                    isStart = true
                }
            }
        }
//        when(uiState.value){
//            ListingUIState.RESTAURANT_DETAILS_SCREEN -> {
//            }
//            ListingUIState.RESTAURANT_CONTACT_DETAILS_SCREEN -> TODO()
//            ListingUIState.RESTAURANT_OWNER_DETAILS_SCREEN -> TODO()
//            ListingUIState.RESTAURANT_TYPE_SCREEN -> TODO()
//            ListingUIState.RESTAURANT_WORKING_WEEK_SCREEN -> TODO()
//            ListingUIState.RESTAURANT_WORKING_HOURS_SCREEN -> TODO()
//            ListingUIState.RESTAURANT_IMAGES_SCREEN -> TODO()
//            ListingUIState.RESTAURANT_LOADING_SCREEN -> TODO()
//            ListingUIState.RESTAURANT_SUCCESS_SCREEN -> TODO()
//        }
    }

    fun validateRestaurantDetails(snackbarHostState: SnackbarHostState): Boolean {
        if (listingData.value.restaurantName.isEmpty()) {
            onRestaurantErrorChanged(true, "Restaurant name cannot be empty")
            showSnackbar(snackbarHostState, "Restaurant name cannot be empty")
            return false
        }
        if (listingData.value.restaurantAddress.street.isEmpty()) {
            showSnackbar(snackbarHostState, "Street cannot be empty")
            return false
        }
        if (listingData.value.restaurantAddress.city.isEmpty()) {
            showSnackbar(snackbarHostState, "City cannot be empty")
            return false
        }
        if (listingData.value.restaurantAddress.state.isEmpty()) {
            showSnackbar(snackbarHostState, "State cannot be empty")
            return false
        }
        if (listingData.value.restaurantAddress.postalCode.isEmpty()) {
            showSnackbar(snackbarHostState, "Postal code cannot be empty")
            return false
        }
        if(currentIndex == 0)
            return true
        if (phoneNumberCheck(listingData.value.phoneNumber, ::onPhoneNumberErrorChanged, false)) {
            showSnackbar(snackbarHostState, listingData.value.phoneNumberError)
            return false
        }
        if (phoneNumberCheck(listingData.value.alternatePhoneNumber, ::onAlternatePhoneNumberErrorChanged, false)) {
            showSnackbar(snackbarHostState, listingData.value.alternatePhoneNumberError)
            return false
        }
        if(emailCheck(listingData.value.email, ::onEmailErrorChanged)){
            showSnackbar(snackbarHostState, listingData.value.emailError)
            return false
        }
        if(currentIndex == 1)
            return true
        if (listingData.value.ownerName.isEmpty()) {
            onOwnerNameErrorChanged(true, "Owner name cannot be empty")
            showSnackbar(snackbarHostState, "Owner name cannot be empty")
            return false
        }

        if (emailCheck(listingData.value.ownerEmail, ::onOwnerEmailErrorChanged)) {
            showSnackbar(snackbarHostState, listingData.value.ownerEmailError)
            return false
        }

        if (phoneNumberCheck(listingData.value.ownerMobileNumber, ::onOwnerMobileNumberErrorChanged, false)) {
            showSnackbar(snackbarHostState, listingData.value.ownerMobileNumberError)
            return false
        }
        if(currentIndex == 2)
            return true

        if (listingData.value.selectedRestaurantType.isEmpty()) {
            showSnackbar(snackbarHostState, "Please select at least one restaurant type")
            return false
        }
        if(currentIndex == 3)
            return true
        if (listingData.value.workingDaysList.isEmpty()) {
            showSnackbar(snackbarHostState, "Please select at least one working day")
            return false
        }

        return true
    }

    private fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun onRestaurantNameChanged(restaurantName: String) {
        listingData.value = listingData.value.copy(restaurantName = restaurantName)
        if(restaurantName.isNotEmpty())
            onRestaurantErrorChanged(false, "")
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
        if(listingData.value.isPhoneNumberError)
            phoneNumberCheck(phoneNumber, ::onPhoneNumberErrorChanged, false)
    }

    fun onPhoneNumberErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isPhoneNumberError = isError, phoneNumberError = error)
    }

    fun onAlternatePhoneNumberChanged(alternatePhoneNumber: String) {
        listingData.value = listingData.value.copy(alternatePhoneNumber = alternatePhoneNumber)
        if(listingData.value.isAlternatePhoneNumberError)
            phoneNumberCheck(alternatePhoneNumber, ::onAlternatePhoneNumberErrorChanged, false)
    }

    fun onAlternatePhoneNumberErrorChanged(isError: Boolean, error: String) {
        listingData.value = listingData.value.copy(
            isAlternatePhoneNumberError = isError,
            alternatePhoneNumberError = error
        )
    }

    fun onEmailChanged(email: String) {
        listingData.value = listingData.value.copy(email = email)
        if(listingData.value.isEmailError)
            emailCheck(email, ::onEmailErrorChanged)
    }

    fun onEmailErrorChanged(isError: Boolean, error: String) {
        listingData.value = listingData.value.copy(isEmailError = isError, emailError = error)
    }

    fun onOwnerNameChanged(ownerName: String) {
        listingData.value = listingData.value.copy(ownerName = ownerName)
        if(ownerName.isNotEmpty())
            onOwnerNameErrorChanged(false, "")
    }

    fun onOwnerNameErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isOwnerNameError = isError, ownerNameError = error)
    }

    fun onOwnerEmailChanged(ownerEmail: String) {
        listingData.value = listingData.value.copy(ownerEmail = ownerEmail)
        onOwnerEmailSameAsRestaurantEmailChanged(listingData.value.isOwnerEmailSameAsRestaurantEmail)
        if(listingData.value.isOwnerEmailError)
            emailCheck(ownerEmail, ::onOwnerEmailErrorChanged)
    }

    fun onOwnerEmailErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isOwnerEmailError = isError, ownerEmailError = error)
    }

    fun onOwnerMobileNumberChanged(ownerMobileNumber: String) {
        listingData.value = listingData.value.copy(ownerMobileNumber = ownerMobileNumber)
        onOwnerMobileSameAsRestaurantMobileChanged(listingData.value.isOwnerMobileSameAsRestaurantMobile)
        if(listingData.value.isOwnerMobileNumberError)
            phoneNumberCheck(ownerMobileNumber,::onOwnerMobileNumberErrorChanged, false)
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

    fun addImage(uri: Uri) {
        listingData.value = listingData.value.copy(
            restaurantImages = listingData.value.restaurantImages
                    + uri
        )
    }

    fun removeImage(uri: Uri) {
        listingData.value = listingData.value.copy(
            restaurantImages = listingData.value.restaurantImages
                    - uri
        )
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
