package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.model.RestaurantType
import com.project.resqfood.ui.theme.displayFontFamily

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RestaurantTypeScreen(data: ListingUIStateData, restaurantListingViewModel: ListingViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text ="Restaurant Category",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Select options which best describe your restaurant",
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight(500),
                color = if(isSystemInDarkTheme()) MaterialTheme.colorScheme.outline else MaterialTheme.colorScheme.outlineVariant
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        FlowRow(modifier = Modifier.fillMaxWidth()) {
            RestaurantType.entries.forEach {
                TypeCard(text = it.toString(), isSelected = data.selectedRestaurantType.contains(it),
                    onClick = { restaurantListingViewModel.onRestaurantTypeChanged(it) })
            }
        }
    }
}

@Composable
private fun TypeCard(text : String, isSelected : Boolean, onClick : () -> Unit){
    OutlinedCard(modifier = Modifier.padding(4.dp,),
        colors = CardDefaults.cardColors(
            containerColor = if(isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surface
        )
    , onClick = onClick) {
        Row(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
            if(isSelected){
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(text = text)
        }
    }
}