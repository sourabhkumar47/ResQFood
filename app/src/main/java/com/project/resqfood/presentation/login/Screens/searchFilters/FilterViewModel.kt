package com.project.resqfood.presentation.login.Screens.searchFilters

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.project.resqfood.presentation.login.Screens.searchFilters.SearchFilterData

class FilterViewModel : ViewModel(){

    var filterData by mutableStateOf(SearchFilterData())
        private set


    fun updateLocation (newLocation : String){
        filterData = filterData.copy(location = newLocation)
    }

    fun updateCuisineType (newCuisineType : String){
        filterData = filterData.copy(cuisineType = newCuisineType)

    }

    fun updatePrice (newPrice :String){
        filterData = filterData.copy(price = newPrice)
    }

    fun updatePickupWindow (newPickupWindow : String){
        filterData = filterData.copy(pickupWindow = newPickupWindow)
    }
}