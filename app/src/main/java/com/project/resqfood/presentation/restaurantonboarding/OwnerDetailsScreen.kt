package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Restaurant
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
fun RestaurantOwnerDetailScreen(data: ListingUIStateData, restaurantListingViewModel: ListingViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text ="Restaurant Owner Details",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "These will be used for revenue related communications",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(500),
                color = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        NormalTextFieldSupport(
            valueInitial = data.ownerName,
            placeholderText = "Enter owner name",
            labelText = "Owner name",
            leadingIcon = Icons.Default.Person,
            supportingText = data.ownerNameError,
            isError = data.isOwnerNameError
        ) {
            restaurantListingViewModel.onOwnerNameChanged(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            valueInitial = data.ownerEmail,
            placeholderText = "Enter email",
            labelText = "Owner email",
            leadingIcon = Icons.Default.Email,
            radioText = "Same as restaurant email address",
            isRadioSelected = restaurantListingViewModel::onOwnerEmailSameAsRestaurantEmailChanged,
            supportingText = data.ownerEmailError,
            isError = data.isOwnerEmailError
        ) {
            restaurantListingViewModel.onOwnerEmailChanged(it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        NormalTextFieldSupport(
            valueInitial = data.ownerMobileNumber,
            isPhoneNumber = true,
            placeholderText = "Enter mobile number",
            labelText = "Owner mobile number",
            radioText = "Same as restaurant mobile no.",
            isRadioSelected = restaurantListingViewModel::onOwnerMobileSameAsRestaurantMobileChanged,
            supportingText = data.ownerMobileNumberError,
            isError = data.isOwnerMobileNumberError
        ) {
            restaurantListingViewModel.onOwnerMobileNumberChanged(it)
        }
    }
}