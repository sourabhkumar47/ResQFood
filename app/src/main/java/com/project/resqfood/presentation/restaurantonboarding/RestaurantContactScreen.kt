package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.ui.theme.displayFontFamily

@Composable
fun RestaurantContactScreen(
    data: ListingUIStateData,
    restaurantListingViewModel: ListingViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Contact Number at Restaurant",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "This will help users contact you",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(500),
                color = if (isSystemInDarkTheme()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        NormalTextFieldSupport(
            valueInitial = data.phoneNumber,
            placeholderText = "Enter mobile number",
            labelText = "Mobile number at restaurant",
            isPhoneNumber = true,
            supportingText = data.phoneNumberError,
            isError = data.isPhoneNumberError
        ) {
            restaurantListingViewModel.onPhoneNumberChanged(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            valueInitial = data.alternatePhoneNumber,
            placeholderText = "Enter mobile number",
            labelText = "Alternate mobile number",
            isPhoneNumber = true,
            supportingText = data.alternatePhoneNumberError,
            isError = data.isAlternatePhoneNumberError
        ) {
            restaurantListingViewModel.onAlternatePhoneNumberChanged(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            valueInitial = data.ownerEmail,
            placeholderText = "Enter email address",
            labelText = "Owner email",
            supportingText = data.emailError,
            leadingIcon = Icons.Default.Email,
            isError = data.isEmailError
        ) {
            restaurantListingViewModel.onEmailChanged(it)
        }
    }
}