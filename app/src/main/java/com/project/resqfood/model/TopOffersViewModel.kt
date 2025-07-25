package com.project.resqfood.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.project.resqfood.presentation.login.Screens.allCategories
import com.project.resqfood.presentation.login.Screens.cards
import com.project.resqfood.presentation.login.Screens.Card

/**
 * ViewModel for Top Offers Home Section.
 * Manages category filtering, persistent selection, and filtered offer exposure for the UI.
 */
class TopOffersViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val categories: List<String> = listOf("All") + allCategories
    val allOffers: List<Card> = cards

    var selectedCategories by mutableStateOf(
        savedStateHandle.get<Set<String>>("selectedCategories") ?: setOf("All")
    )
        private set

    val filteredOffers: List<Card>
        get() = if (selectedCategories.contains("All")) allOffers
        else allOffers.filter { offer ->
            offer.categories.any { cat -> selectedCategories.contains(cat) }
        }

    /** Toggle a category chip (multi-select). If 'All' toggled, resets; else toggles just that cat. */
    fun onCategoryChipClick(category: String) {
        selectedCategories = when {
            category == "All" -> setOf("All")
            selectedCategories.contains(category) -> {
                // Remove, fallback to All if selection empty
                val updated = selectedCategories - category
                if (updated.isEmpty()) setOf("All") else updated - "All"
            }

            else -> (selectedCategories - "All") + category
        }
        savedStateHandle["selectedCategories"] = selectedCategories
    }
}
