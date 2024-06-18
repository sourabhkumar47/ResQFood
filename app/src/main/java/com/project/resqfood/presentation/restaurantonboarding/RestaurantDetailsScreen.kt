package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.R
import com.project.resqfood.ui.theme.displayFontFamily


@Composable
fun RestaurantDetailScreen(data: ListingUIStateData, restaurantListingViewModel: ListingViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Restaurant Details",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Ensure that details are correct",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(500),
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        NormalTextFieldSupport(
            modifier = Modifier.fillMaxWidth(),
            valueInitial = data.restaurantName,
            leadingIcon = Icons.Default.Restaurant,
            placeholderText = "Enter restaurant name",
            isError = data.isRestaurantError,
            supportingText = data.restaurantError,
            labelText = "Restaurant name"
        ) {
            restaurantListingViewModel.onRestaurantNameChanged(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            modifier = Modifier.fillMaxWidth(),
            valueInitial = data.restaurantAddress.street,
            leadingIcon = Icons.Default.House,
            placeholderText = "Enter street",
            isError = false,
            supportingText = "",
            labelText = "Street"
        ) {
            restaurantListingViewModel.onRestaurantAddressChanged(data.restaurantAddress.copy(street = it))
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            modifier = Modifier.fillMaxWidth(),
            valueInitial = data.restaurantAddress.city,
            leadingIcon = Icons.Default.LocationCity,
            placeholderText = "Enter city",
            isError = false,
            supportingText = "",
            labelText = "City"
        ) {
            restaurantListingViewModel.onRestaurantAddressChanged(data.restaurantAddress.copy(city = it))
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            modifier = Modifier.fillMaxWidth(),
            valueInitial = data.restaurantAddress.state,
            leadingIcon = Icons.Default.LocationCity,
            placeholderText = "Enter state",
            isError = false,
            supportingText = "",
            labelText = "State"
        ) {
            restaurantListingViewModel.onRestaurantAddressChanged(data.restaurantAddress.copy(state = it))
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            modifier = Modifier.fillMaxWidth(),
            valueInitial = data.restaurantAddress.postalCode,
            leadingIcon = Icons.Default.Pin,
            placeholderText = "Enter postal code",
            isError = false,
            supportingText = data.addressError,
            labelText = "Postal Code"
        ) {
            restaurantListingViewModel.onRestaurantAddressChanged(
                data.restaurantAddress.copy(
                    postalCode = it
                )
            )
        }
    }
}

@Composable
fun NormalTextFieldSupport(
    modifier: Modifier = Modifier,
    valueInitial: String,
    placeholderText: String,
    labelText: String,
    leadingIcon: ImageVector? = null,
    isPhoneNumber: Boolean = false,
    supportingText: String,
    isError: Boolean,
    radioText: String? = null,
    isRadioSelected: (Boolean) -> Unit = {},
    onValueChange: (String) -> Unit
) {
    var isSelected by remember {
        mutableStateOf(false)
    }
    Column(modifier = modifier) {
        val onClick = {isSelected = !isSelected
            isRadioSelected(isSelected)}
        if (radioText != null) {
            Row(modifier = Modifier.clickable {
               onClick()
            }, verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = isSelected, onClick = onClick)
//                Spacer(modifier = Modifier.width(4.dp))
                Text(text = radioText,
                     style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(4.dp))
        }
        Text(text = "${labelText}:")
        Spacer(modifier = Modifier.height(4.dp))
        NormalTextField(
            valueInitial = valueInitial,
            placeholderText = placeholderText,
            leadingIcon = leadingIcon,
            labelText = labelText,
            isEnabled = !isSelected,
            isPhoneNumber = isPhoneNumber,
            supportingText = supportingText,
            isError = isError
        ) {
            onValueChange(it)
        }
    }
}

@Composable
fun NormalTextField(
    modifier: Modifier = Modifier,
    valueInitial: String,
    placeholderText: String,
    isEnabled: Boolean = true,
    labelText: String,
    leadingIcon: ImageVector? = null,
    isPhoneNumber: Boolean = false,
    supportingText: String,
    isError: Boolean,
    onValueChange: (String) -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    var value by remember {
        mutableStateOf(valueInitial)
    }
    OutlinedTextField(
        value = value, onValueChange = {
            value = it
            onValueChange(it)
        },
        enabled = isEnabled,
        placeholder = {
            Text(text = placeholderText)
        },
//        label = {
//            if(isFocused)
//                Text(text = labelText)
//        },
        modifier = modifier.onFocusChanged { focusState ->
            isFocused = focusState.isFocused
        },
        supportingText = {
            if (isError)
                Text(text = supportingText)
        },
        isError = isError,
        prefix = {
            if (isPhoneNumber) {
                Row {
                    Image(
                        painterResource(id = R.drawable.india),
                        contentDescription = null,
                        modifier = Modifier
                            .height(24.dp)
                            .width(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "+91")
                    Spacer(modifier = Modifier.width(8.dp))
                }
            } else if (leadingIcon != null) {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(32.dp))
            }
        },
//        onFocusChanged = {
//        },
        keyboardOptions =
        KeyboardOptions(
            keyboardType = if (isPhoneNumber) KeyboardType.Phone else KeyboardType.Text
        )
    )
}