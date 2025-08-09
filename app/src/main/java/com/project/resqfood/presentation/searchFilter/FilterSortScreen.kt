package com.project.resqfood.presentation.searchFilter

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.presentation.searchFilter.model.CuisineType
import com.project.resqfood.presentation.searchFilter.model.SortType
import com.project.resqfood.presentation.searchFilter.model.Tab
import com.project.resqfood.ui.theme.primaryLight
import com.project.resqfood.ui.theme.tertiaryLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSortBottomSheetUI(
    viewModel: FilterSearchViewModel,
    activeTab: Tab,
    onTabSwitched: (Tab) -> Unit,
    onSortSelected: (SortType) -> Unit,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    val tabItems by viewModel.tabItems.collectAsState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(12.dp),
        containerColor = Color.White
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabItems.forEach { tabItem ->
                    val background by animateColorAsState(
                        if (tabItem.isSelected) primaryLight.copy(alpha = 0.2f) else Color.Transparent,
                        label = ""
                    )
                    Text(
                        text = tabItem.tab.name,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(background)
                            .clickable { onTabSwitched(tabItem.tab) }
                            .padding(vertical = 6.dp, horizontal = 14.dp),
                        color = if (tabItem.isSelected) primaryLight else Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            when (activeTab) {
                Tab.FILTER -> FilterTabContent(viewModel)
                Tab.SORT -> FilterSortTabs(
                    viewModel = viewModel,
                    onOptionSelected = onSortSelected
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(1f),
                    onClick = {
                      viewModel.resetFilters()
                    }
                ) {
                    Text("Reset")
                }
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.hideSheet()
                    }
                ) {
                    Text("Apply")
                }
            }
        }
    }
}
@Composable
fun FilterSortTabs(
    viewModel: FilterSearchViewModel,
    onOptionSelected: (SortType) -> Unit
) {
    val sortOptions by viewModel.sort.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sortOptions.forEach { option ->
            val isSelected = option.isSelected
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) tertiaryLight.copy(alpha = 0.5f) else Color.Transparent,
                label = "SortItemBackground"
            )
            val textColor = if (isSelected) Color.Gray else Color.Black.copy(0.5f)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.dp))
                    .background(backgroundColor)
                    .clickable { onOptionSelected(option.type) }
                    .padding(vertical = 8.dp, horizontal = 8.dp)
            ) {
                Text(
                    text = option.type.displayName,
                    fontSize = 16.sp,
                    color = textColor,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Divider(thickness = 1.dp, color = Color.LightGray)

        }
    }
}
@Composable
fun LocationInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onLocationToggle: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            label = { Text("Location") },
            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = onLocationToggle) {
                    Icon(Icons.Default.MyLocation, contentDescription = "Use current location")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CuisineChipsSelector(
    selected: Set<String>,
    options: List<String>,
    onToggle: (String) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { cuisine ->
            FilterChip(
                selected = cuisine in selected,
                onClick = { onToggle(cuisine) },
                label = {
                    Text(cuisine, fontSize = 13.sp)
                }
            )
        }
    }
}

@Composable
fun PriceRangeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1000f
) {
    Column {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = 4,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PickupWindowSliderField(
    range: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        RangeSlider(
            value = range,
            onValueChange = { newRange ->
                if (newRange.endInclusive - newRange.start >= 1f) {
                    onRangeChange(newRange)
                }
            },
            valueRange = 0f..24f,
            steps = 23,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = "${range.start.toInt()} – ${range.endInclusive.toInt()} hrs",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 4.dp)
        )
    }
}

@Composable
fun FilterTabContent(
    viewModel: FilterSearchViewModel
) {
    val location by viewModel.location.collectAsState()
    val selectedCuisines by viewModel.selectedCuisines.collectAsState()
    val priceRange by viewModel.priceRange.collectAsState()
    val pickupWindow by viewModel.pickupWindow.collectAsState()

    Column(
        modifier = Modifier
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Location", style = MaterialTheme.typography.labelLarge)
        LocationInputField(
            query = location,
            onQueryChange = viewModel::updateLocation,
            onLocationToggle = viewModel::useCurrentLocation
        )

        Text("Cuisine", style = MaterialTheme.typography.labelLarge)
        CuisineChipsSelector(
            selected = selectedCuisines.map { it.description }.toSet(),
            options = CuisineType.values().map { it.description },
            onToggle = viewModel::toggleCuisineSelection
        )

        Text("Max Price: ₹${priceRange.endInclusive.toInt()}", style = MaterialTheme.typography.labelLarge)
        PriceRangeSlider(
            value = priceRange.endInclusive,
            onValueChange = {
                viewModel.updatePriceRange(priceRange.start..it)
            }
        )

        Text("Pickup Window", style = MaterialTheme.typography.labelLarge)
        PickupWindowSliderField(
            range = pickupWindow,
            onRangeChange = viewModel::updatePickupWindow
        )
    }
}
