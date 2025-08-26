package com.project.resqfood.presentation.restaurantonboarding

import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.storage.FirebaseStorage
import com.project.resqfood.domain.repository.RestaurantDataRepository
import com.project.resqfood.model.RestaurantEntity
import com.project.resqfood.model.RestaurantType
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import com.project.resqfood.presentation.login.Screens.NavRoleSelectScreen
import com.project.resqfood.presentation.login.emailCheck
import com.project.resqfood.presentation.login.phoneNumberCheck
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ListingViewModel : ViewModel() {
    var uiState = mutableStateOf(ListingUIState.RESTAURANT_DETAILS_SCREEN)
        private set
    var listingData = mutableStateOf(ListingUIStateData())
        private set
    val order = ListingUIState.entries.toList()
    var isStart = true
    private var currentIndex = 0

        fun onNextClick(snackbarHostState: SnackbarHostState, navController: NavController,entryPoint: String) {
            isStart = false
            if (uiState.value == ListingUIState.RESTAURANT_SUCCESS_SCREEN)
            //navController.navigateUp()
                if (entryPoint == "fromRegistration") {
                    navController.navigate(NavMainScreen) {
                        popUpTo(NavRoleSelectScreen) { inclusive = true }
                    }
                } else {
                    navController.navigateUp()
                }
            if (uiState.value == ListingUIState.RESTAURANT_IMAGES_SCREEN)
                saveData()
            when (currentIndex) {
                in 0..(order.size - 3) -> {
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

    fun onBackClick() {
        if (currentIndex > 0) {
            currentIndex--
            uiState.value = order[currentIndex]
        }
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
        if (currentIndex == 0)
            return true
        if (phoneNumberCheck(listingData.value.phoneNumber, ::onPhoneNumberErrorChanged, false)) {
            showSnackbar(snackbarHostState, listingData.value.phoneNumberError)
            return false
        }
        if (phoneNumberCheck(
                listingData.value.alternatePhoneNumber,
                ::onAlternatePhoneNumberErrorChanged,
                false
            )
        ) {
            showSnackbar(snackbarHostState, listingData.value.alternatePhoneNumberError)
            return false
        }
        if (emailCheck(listingData.value.email, ::onEmailErrorChanged)) {
            showSnackbar(snackbarHostState, listingData.value.emailError)
            return false
        }
        if (currentIndex == 1)
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

        if (phoneNumberCheck(
                listingData.value.ownerMobileNumber,
                ::onOwnerMobileNumberErrorChanged,
                false
            )
        ) {
            showSnackbar(snackbarHostState, listingData.value.ownerMobileNumberError)
            return false
        }
        if (currentIndex == 2)
            return true

        if (listingData.value.selectedRestaurantType.isEmpty()) {
            showSnackbar(snackbarHostState, "Please select at least one restaurant type")
            return false
        }
        if (currentIndex == 3)
            return true
        if (listingData.value.workingDaysList.isEmpty()) {
            showSnackbar(snackbarHostState, "Please select at least one working day")
            return false
        }
        return true
    }

    private fun saveData() {
        uiState.value = ListingUIState.RESTAURANT_LOADING_SCREEN
        val restaurantDataRepository = RestaurantDataRepository()
        viewModelScope.launch {
            val imageUrls = mutableListOf<String>()
            for (image in listingData.value.restaurantImages) {
                val imageUrl = uploadImageToFirebaseStorage(image)
                if (imageUrl != null)
                    imageUrls.add(imageUrl)
            }
            restaurantDataRepository.setRestaurantData(
                RestaurantEntity(
                    restaurantName = listingData.value.restaurantName,
                    restaurantAddress = listingData.value.restaurantAddress,
                    phoneNumber = listingData.value.phoneNumber,
                    alternatePhoneNumber = listingData.value.alternatePhoneNumber,
                    email = listingData.value.email,
                    ownerMobileNumber = listingData.value.ownerMobileNumber,
                    ownerName = listingData.value.ownerName,
                    ownerEmail = listingData.value.ownerEmail,
                    isOwnerEmailSameAsRestaurantEmail = listingData.value.isOwnerEmailSameAsRestaurantEmail,
                    isOwnerMobileSameAsRestaurantMobile = listingData.value.isOwnerMobileSameAsRestaurantMobile,
                    selectedRestaurantType = listingData.value.selectedRestaurantType,
                    workingDaysList = listingData.value.workingDaysList,
                    timeSlots = listingData.value.timeSlots,
                    restaurantImages = imageUrls
                )
            )
            uiState.value = ListingUIState.RESTAURANT_SUCCESS_SCREEN
        }
    }

    private fun showSnackbar(snackbarHostState: SnackbarHostState, message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun onRestaurantNameChanged(restaurantName: String) {
        listingData.value = listingData.value.copy(restaurantName = restaurantName)
        if (restaurantName.isNotEmpty())
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
        if (listingData.value.isPhoneNumberError)
            phoneNumberCheck(phoneNumber, ::onPhoneNumberErrorChanged, false)
    }

    fun onPhoneNumberErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isPhoneNumberError = isError, phoneNumberError = error)
    }

    fun onAlternatePhoneNumberChanged(alternatePhoneNumber: String) {
        listingData.value = listingData.value.copy(alternatePhoneNumber = alternatePhoneNumber)
        if (listingData.value.isAlternatePhoneNumberError)
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
        if (listingData.value.isEmailError)
            emailCheck(email, ::onEmailErrorChanged)
    }

    fun onEmailErrorChanged(isError: Boolean, error: String) {
        listingData.value = listingData.value.copy(isEmailError = isError, emailError = error)
    }

    fun onOwnerNameChanged(ownerName: String) {
        listingData.value = listingData.value.copy(ownerName = ownerName)
        if (ownerName.isNotEmpty())
            onOwnerNameErrorChanged(false, "")
    }

    fun onOwnerNameErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isOwnerNameError = isError, ownerNameError = error)
    }

    fun onOwnerEmailChanged(ownerEmail: String) {
        listingData.value = listingData.value.copy(ownerEmail = ownerEmail)
        onOwnerEmailSameAsRestaurantEmailChanged(listingData.value.isOwnerEmailSameAsRestaurantEmail)
        if (listingData.value.isOwnerEmailError)
            emailCheck(ownerEmail, ::onOwnerEmailErrorChanged)
    }

    fun onOwnerEmailErrorChanged(isError: Boolean, error: String) {
        listingData.value =
            listingData.value.copy(isOwnerEmailError = isError, ownerEmailError = error)
    }

    fun onOwnerMobileNumberChanged(ownerMobileNumber: String) {
        listingData.value = listingData.value.copy(ownerMobileNumber = ownerMobileNumber)
        onOwnerMobileSameAsRestaurantMobileChanged(listingData.value.isOwnerMobileSameAsRestaurantMobile)
        if (listingData.value.isOwnerMobileNumberError)
            phoneNumberCheck(ownerMobileNumber, ::onOwnerMobileNumberErrorChanged, false)
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

suspend fun uploadImageToFirebaseStorage(imageUri: Uri): String? {
    val storageRef =
        FirebaseStorage.getInstance().reference.child("images/${imageUri.lastPathSegment}")
    val uploadTask = storageRef.putFile(imageUri)
    val taskSnapshot =
        uploadTask.await() // This will suspend the coroutine until the upload is complete
    return taskSnapshot.storage.downloadUrl.await().toString()
}
