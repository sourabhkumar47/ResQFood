package com.project.resqfood.presentation.searchFilter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.resqfood.R
import com.project.resqfood.ui.theme.primaryContainerLight
import com.project.resqfood.ui.theme.primaryLight
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
object NavSearchScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: FilterSearchViewModel
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var isSheetVisible by viewModel.isSheetVisible

    val activeTab by viewModel.activeTab.collectAsState()

    if (isSheetVisible) {
        FilterSortBottomSheetUI(
            activeTab = activeTab,
            onTabSwitched = viewModel::switchTab,
            onSortSelected = viewModel::selectSortOption,
            sheetState = sheetState,
            onDismiss = { isSheetVisible = false },
            viewModel = viewModel
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryContainerLight.copy(0.2f))
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppSearchBar(
                modifier = Modifier.weight(1f),
                appLabel = "ResQ",
                searchLabel = "Search the Food",
                searchText = viewModel.searchText,
                onSearchTextChange = viewModel::onSearchTextChange,
                isSearching = viewModel.isSearching,
                onSearchingToggle = viewModel::toggleSearching,
                onSearchItemClick = viewModel::onSearchQuerySubmit
            )

            Spacer(modifier = Modifier.width(12.dp))

            ResetIcon(
                onClick = {
                    isSheetVisible = true
                    coroutineScope.launch {
                        sheetState.show()
                    }
                }
            )
        }

        SearchSuggestions(
            recentSearches = viewModel.recentSearches.take(3),
            fallbackSuggestions = viewModel.fallbackSuggestions.take(3),
            onSearchClicked = viewModel::onSearchQuerySubmit,
            viewModel = viewModel
        )
    }
}

@Composable
fun AppSearchBar(
    modifier: Modifier = Modifier,
    appLabel: String = "ResQ",
    searchLabel: String = "Search the Food",
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    isSearching: Boolean,
    onSearchingToggle: (Boolean) -> Unit,
    onSearchItemClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .background(Color.Transparent)
            .border(
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.tertiary),
                shape = RoundedCornerShape(50)
            )
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(end = 8.dp)
        )

        if (isSearching) {
            BasicTextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Black),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp),
                decorationBox = { innerTextField ->
                    if (searchText.isEmpty()) {
                        Text(
                            text = searchLabel,
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.Gray)
                        )
                    }
                    innerTextField()
                }
            )
        } else {
            val focusRequester = remember { FocusRequester() }
            val coroutineScope = rememberCoroutineScope()

            Text(
                text = appLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onSearchingToggle(true)
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(50)
                            focusRequester.requestFocus()
                        }
                    }
                    .padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun SearchSuggestions(
    recentSearches: List<String>,
    fallbackSuggestions: List<String>,
    onSearchClicked: (String) -> Unit,
    viewModel: FilterSearchViewModel
) {
    val (title, suggestions) = viewModel.suggestionTitleWithItems

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Spacer(Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        suggestions.forEachIndexed { index, item ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSearchClicked(item) }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_click),
                        contentDescription = "Search Suggestion Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (index != suggestions.lastIndex) {
                    Divider(modifier = Modifier.padding(horizontal = 16.dp))
                }
            }
        }
    }
}
@Composable
fun ResetIcon(
    painter : Painter = painterResource(R.drawable.filter),
    iconDescription : String = "Circular Icon",
    boxSize : Dp = 46.dp,
    boxShape : Shape = CircleShape,
    boxColor : Color = primaryLight,
    iconPadding : Dp = 11.dp,
    iconSize : Dp = 22.dp,
    onClick : () -> Unit = {},
    tint : Color = Color.White
){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(boxSize)
            .clip(boxShape)
            .background(boxColor)
            .clickable { onClick() }
    ){
        Icon(
            painter = painter,
            contentDescription = iconDescription,
            modifier = Modifier
                .padding(iconPadding)
                .size(iconSize),
            tint = tint
        )
    }
}
