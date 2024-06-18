package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.project.resqfood.ui.theme.displayFontFamily

@Composable
fun RestaurantOperationalHoursScreen(
    data: ListingUIStateData,
    restaurantListingViewModel: ListingViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Restaurant Operational Hours",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        SecondaryText(text = "Set your restaurant operational hours")
        Spacer(modifier = Modifier.height(48.dp))
        data.timeSlots.forEachIndexed { index, value ->
            Row {
                Column {
                    SecondaryText(text = "Opening time", alpha = 0.8f)
                    Spacer(modifier = Modifier.height(12.dp))
                    TimePicker(
                        selectedHour = data.timeSlots[index].openingTimeHour,
                        selectedMinute = data.timeSlots[index].openingTimeMinute,
                        isTimeSelected = true
                    ) { hour, minute ->
                        restaurantListingViewModel.onTimeSlotChanged(
                            RestaurantTimeSlot(
                                openingTimeHour = hour,
                                openingTimeMinute = minute,
                                closingTimeHour = data.timeSlots[index].closingTimeHour,
                                closingTimeMinute = data.timeSlots[index].closingTimeMinute
                            ), index
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                SecondaryText(
                    text = "to", modifier = Modifier
                        .align(Alignment.Bottom)
                        .padding(bottom = 24.dp), alpha = 0.8f
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    SecondaryText(text = "Closing time", alpha = 0.8f)
                    Spacer(modifier = Modifier.height(12.dp))
                    TimePicker(
                        selectedHour = data.timeSlots[index].closingTimeHour,
                        selectedMinute = data.timeSlots[index].closingTimeMinute,
                        isTimeSelected = true
                    ) { hour, minute ->
                        restaurantListingViewModel.onTimeSlotChanged(
                            RestaurantTimeSlot(
                                openingTimeHour = data.timeSlots[index].openingTimeHour,
                                openingTimeMinute = data.timeSlots[index].openingTimeMinute,
                                closingTimeHour = hour,
                                closingTimeMinute = minute
                            ), index
                        )
                    }
                }

            }
            Spacer(modifier = Modifier.height(32.dp))
        }
        FilledTonalButton(onClick = { restaurantListingViewModel.addTimeSlot() }) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add time slot")
        }
        Spacer(modifier = Modifier.height(16.dp))

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    isTimeSelected: Boolean,
    onSelectionClock: (Int, Int) -> Unit
) {
    var clockState = rememberSheetState()
    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            onSelectionClock(hours, minutes)
        }
    )
    Column {
        Row(
//        modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.clickable {
                    clockState.show()
                }
            ) {
                OutlinedTextField(
                    value = formatTime(selectedHour, selectedMinute),
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null
                        )
                    },
                    modifier = Modifier
                        .width(120.dp)
                        .clickable {
                            clockState.show()
                        },
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = MaterialTheme.colorScheme.surface,
                        disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        disabledIndicatorColor = MaterialTheme.colorScheme.outline,
                        disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}

private fun formatTime(hours: Int, minutes: Int): String {
    var formattedHours = String.format("%02d", hours)
    var formattedMinutes = String.format("%02d", minutes)
    return "$formattedHours:$formattedMinutes"
}


@Composable
fun SecondaryText(modifier: Modifier = Modifier, text: String, alpha: Float = 0.6f) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            fontSize = 14.sp,
            fontFamily = displayFontFamily,
            fontWeight = FontWeight(500),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha)
        )
    )
}