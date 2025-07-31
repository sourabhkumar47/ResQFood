package com.project.resqfood.presentation.login.Screens.searchFilters

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.project.resqfood.ui.theme.AppTypography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFilterScreen(onClose: () -> Unit) {
    val viewModel: FilterViewModel = viewModel()
    var searchText by remember { mutableStateOf("") }
    val filterData = viewModel.filterData
    LaunchedEffect(Unit) {
        viewModel.updateLocation("San Francisco")
        viewModel.updateCuisineType("Italian")
        viewModel.updatePrice("Under ₹100")
        viewModel.updatePickupWindow("12pm - 2pm")
    }
    var showLocationDialog by remember { mutableStateOf(false) }
    var showCuisineTypeDialog by remember { mutableStateOf(false) }
    var showPriceDialog by remember { mutableStateOf(false) }
    var showPickupWindowDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Content should be separate and centered
        Spacer(modifier = Modifier.height(32.dp))
        IconButton(onClick = {onClose()},
            modifier = Modifier.align(Alignment.TopEnd).padding(10.dp)) {
            Icon(imageVector = Icons.Default.Close,
                contentDescription = "Close")
        }

                // All your content here
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 64.dp),
                    horizontalAlignment = Alignment.Start
                ) {
//                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Find Food",
                        style = AppTypography.displaySmall,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.12.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    SearchBar(
                        query = searchText,
                        onQueryChange = { searchText = it },
                        onSearch = { },
                        active = false,
                        onActiveChange = { },
                        placeholder = { Text("Search for your food items") },
                        shape = Shapes().medium,
                        modifier = Modifier.fillMaxWidth()
                    ) { }
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Filters",
                        style = AppTypography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),

                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        FilterRow(filterName = "Location",
                            currentValue = filterData.location,
                            onViewDetails = {showLocationDialog = true})
                        FilterRow(filterName = "Cuisine Type",
                            currentValue = filterData.cuisineType,
                            onViewDetails = {showCuisineTypeDialog = true})
                        FilterRow(filterName = "Price",
                            currentValue = filterData.price,
                            onViewDetails = {showPriceDialog = true})
                        FilterRow(filterName = "Pickup Window",
                            currentValue = filterData.pickupWindow,
                            onViewDetails = {showPickupWindowDialog = true})

                    }
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = ButtonDefaults.shape,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer)
                    ) {
                        Text("Apply Filters")


                    }
                }
            }
    ShowSelectionIfNeeded(
        show = showLocationDialog,
        title = "Location",
        options = listOf("San Francisco", "New York", "Los Angeles"),
        onDismiss = { showLocationDialog = false },
        onSelected = {
            viewModel.updateLocation(it)
        }
    )

    ShowSelectionIfNeeded(
        show = showCuisineTypeDialog,
        title = "Cuisine Type",
        options = listOf("Italian", "Mexican", "Chinese"),
        onDismiss = { showCuisineTypeDialog = false },
        onSelected = {
            viewModel.updateCuisineType(it)
        }
    )

    ShowSelectionIfNeeded(
        show = showPriceDialog,
        title = "Price",
        options = listOf("Under ₹100", "₹100 - ₹200", "Over ₹200"),
        onDismiss = { showPriceDialog = false },
        onSelected = {
            viewModel.updatePrice(it)
        }
    )

    ShowSelectionIfNeeded(
        show = showPickupWindowDialog,
        title = "Pickup Window",
        options = listOf("12pm - 2pm", "2pm - 4pm", "4pm - 6pm"),
        onDismiss = { showPickupWindowDialog = false },
        onSelected = {
            viewModel.updatePickupWindow(it)
        }
    )

}


@Composable
fun FilterRow(filterName: String,
              currentValue : String,
              onViewDetails : () -> Unit) {

    Column (
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = filterName,
                color = Color.Black,
                fontWeight = FontWeight.Medium,
                style = AppTypography.titleLarge,
                maxLines = 1
            )
            TextButton(
                onClick = onViewDetails,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "View Details",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = currentValue,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            style = AppTypography.titleMedium,
            maxLines = 1
        )
    }




    }

@Composable
fun ShowSelectionIfNeeded(
    show : Boolean,
    title: String,
    options : List<String>,
    onDismiss : () -> Unit,
    onSelected : (String) -> Unit
){
    if(show){
        SelectionDialog(
            title = title,
            options = options,
            onDismiss = onDismiss,
            onOptionSelected = onSelected
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionDialog(
    title: String,
    options : List<String>,
    onDismiss : () -> Unit,
    onOptionSelected : (String) -> Unit

){
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Column {
                options.forEach { option ->
                    Text(
                        text = option,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onOptionSelected(option)
                                onDismiss()
                            }
                            .padding(vertical = 8.dp),

                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {}


    )
}
@Composable
@Preview (showBackground = true)
fun SearchScreenPreview(){
    SearchFilterScreen(
        onClose = {}
    )
}