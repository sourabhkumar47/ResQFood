package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.ui.theme.displayFontFamily

enum class DayOfWeek {
    Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
}

enum class RadioButtonOption {
    Everyday, Weekdays
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RestaurantOpenDaysScreen(
    data: ListingUIStateData,
    restaurantListingViewModel: ListingViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Mark open days",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Don't forget to uncheck your day off",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(500),
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        FlowRow() {
            RadioButtonOption.entries.forEach { option ->
                RadioButtonCard(text = option.toString(),
                    isSelected = data.workingDaysRadioOption == option,
                    onClick = {
                        restaurantListingViewModel.onWorkingDaysRadioOptionChanged(
                            option
                        )
                    })
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        FlowRow {
            DayOfWeek.entries.forEach {
                DayCard(text = it.toString(),
                    isSelected = data.workingDaysList.contains(it),
                    isEnabled = data.workingDaysRadioOption == null,
                    onClick = { restaurantListingViewModel.onWorkingDaysChanged(it) })
            }
        }
    }
}


@Composable
private fun RadioButtonCard(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier.clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = { onClick() })
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(500),
            ),
        )
    }
}

@Composable
private fun DayCard(text: String, isSelected: Boolean, isEnabled: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .clickable {
                if (isEnabled)
                    onClick()
            }
            .width(140.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = isSelected, onCheckedChange = { onClick() }, enabled = isEnabled)
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(500),
                color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(
                    alpha = 0.5f
                )
            ),
        )
    }
}