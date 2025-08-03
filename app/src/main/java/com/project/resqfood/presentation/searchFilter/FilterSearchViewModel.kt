package com.project.resqfood.presentation.searchFilter

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.project.resqfood.presentation.searchFilter.model.SortOption
import com.project.resqfood.presentation.searchFilter.model.SortType
import com.project.resqfood.presentation.searchFilter.model.Tab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.project.resqfood.presentation.searchFilter.model.CuisineType
import com.project.resqfood.presentation.searchFilter.model.FilterTab


class FilterSearchViewModel : ViewModel() {

    private val _tabItems = MutableStateFlow<List<FilterTab>>(emptyList())
    val tabItems = _tabItems.asStateFlow()

    init {
        try {
            updateTabItems(Tab.FILTER)
            Log.d("CrashDebug", "updateTabItems executed successfully")
        } catch (e: Exception) {
            Log.e("CrashDebug", "Crash in init block: ${e.message}", e)
        }
    }

    fun updateTabItems(activeTab: Tab) {
        _tabItems.value = Tab.values().map {
            FilterTab(it, isSelected = it == activeTab)
        }
        switchTab(activeTab)
    }


    private val _filterTabs = MutableStateFlow<List<FilterTab>>(emptyList())
    var isSearching by mutableStateOf(false)
        private set

    var searchText by mutableStateOf("")
        private set

    var recentSearches = mutableStateListOf<String>()
        private set

    val suggestionTitleWithItems: Pair<String, List<String>>
        get() = if (recentSearches.isNotEmpty()) {
            "Recent Searches" to recentSearches.take(3)
        } else {
            "Try These" to fallbackSuggestions.take(3)
        }

    val fallbackSuggestions = listOf("Pizza", "Biryani",  "Pasta")

    fun onSearchTextChange(text: String) {
        searchText = text
    }

    fun toggleSearching(isActive: Boolean) {
        isSearching = isActive
    }

    fun onSearchQuerySubmit(query: String) {
        searchText = query
        isSearching = true

        recentSearches.remove(query)
        recentSearches.add(0, query)
        if (recentSearches.size > 3) {
            recentSearches.removeLast()
        }

        addSearchQuery(query)
    }
    private val _recentSearches = mutableStateListOf<String>()

    fun addSearchQuery(query: String) {
        _recentSearches.remove(query)
        _recentSearches.add(0, query)
        if (_recentSearches.size > 3) {
            _recentSearches.removeAt(_recentSearches.lastIndex)
        }
    }
    private val _isSheetVisible = mutableStateOf(false)
    val isSheetVisible = _isSheetVisible
    fun hideSheet() { _isSheetVisible.value = false }

    private val _activeTab = MutableStateFlow(Tab.FILTER)
    val activeTab = _activeTab.asStateFlow()
    fun switchTab(tab: Tab) {
        _activeTab.value = tab
    }

    private val _location = MutableStateFlow("")
    val location = _location.asStateFlow()

    fun updateLocation(value: String) {
        _location.value = value
    }

    fun useCurrentLocation() {
        _location.value = "Current Location"
    }
    private val _selectedCuisines = MutableStateFlow(setOf<CuisineType>())
    val selectedCuisines = _selectedCuisines.asStateFlow()

    fun toggleCuisineSelection(label: String) {
        val cuisine = CuisineType.values().firstOrNull { it.description == label } ?: return
        _selectedCuisines.update { current ->
            if (cuisine in current) current - cuisine else current + cuisine
        }
    }

    private val _priceRange = MutableStateFlow(0f..1000f)
    val priceRange = _priceRange.asStateFlow()

    fun updatePriceRange(newRange: ClosedFloatingPointRange<Float>) {
        _priceRange.value = newRange
    }
    private val _pickupWindow = MutableStateFlow(10f..12f)
    val pickupWindow = _pickupWindow.asStateFlow()

    fun updatePickupWindow(range: ClosedFloatingPointRange<Float>) {
        _pickupWindow.value = range
    }

    fun resetFilters() {
        _location.value = ""
        _selectedCuisines.value = emptySet()
        _priceRange.value = 0f..1000f
        _pickupWindow.value = 10f..12f
    }

    private val _sort = MutableStateFlow(
        SortType.values().map { SortOption(it, false) }
    )
    val sort = _sort.asStateFlow()

    fun selectSortOption(type: SortType) {
        _sort.update { options ->
            options.map { it.copy(isSelected = it.type == type) }
        }
    }
}
