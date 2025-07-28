package com.project.resqfood.presentation.login.Screens


import android.content.Intent
import android.util.Log

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.project.resqfood.model.TopOffersViewModel
import com.project.resqfood.presentation.itemdetailscreen.NavItemDetailScreen
import com.project.resqfood.presentation.login.BottomNavigation.BottomNavigationItem
import com.project.resqfood.presentation.profilescreens.ProfileScreen
import com.project.resqfood.presentation.profilescreens.TopAppBarProfileScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.ui.graphics.Color
import com.project.resqfood.presentation.MainActivity
import timber.log.Timber

@Serializable
object NavMainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val isRequestDrawer = rememberSaveable { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        BottomNavigationItem(
            title = "Order",
            selectedIcon = Icons.Filled.AutoStories,
            unselectedIcon = Icons.Outlined.AutoStories
        ),
        BottomNavigationItem(
            title = "You",
            selectedIcon = Icons.Filled.AccountCircle,
            unselectedIcon = Icons.Outlined.AccountCircle
        ),
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column {
                    Box(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "ResQFood",
                            fontSize = 48.sp,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    HorizontalDivider()
                    // Add navigation items
                    NavigationDrawerItem(
                        label = { Text("Home") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItemIndex = 0
                        },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) }
                    )
                    NavigationDrawerItem(
                        label = { Text("Profile") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItemIndex = 1
                        },
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) }
                    )
                    NavigationDrawerItem(
                        label = { Text("view Map") },
                        selected = false,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItemIndex = 2
                        },
                        icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) }
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, end = 16.dp, top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dark Mode",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = MainActivity.isDarkModeEnabled.value,
                            onCheckedChange = { isChecked ->
                                MainActivity.isDarkModeEnabled.value = isChecked
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color(0xFFFFD600),       // Bright Yellow thumb
                                checkedTrackColor = Color(0xFFFFF59D),       // Soft Yellow track
                                uncheckedThumbColor = Color(0xFFB0BEC5),     // Grey thumb when off
                                uncheckedTrackColor = Color(0xFFECEFF1)      // Light grey track
                            )
                        )
                    }
                    // Add more items as needed
                }
            }
        },
        content = {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
                // --- ADD VIEWMODEL INJECTION ---
                val topOffersViewModel: TopOffersViewModel = viewModel()
                Scaffold(
                    modifier = if (selectedItemIndex == 0) Modifier
                        .fillMaxSize()
                        .nestedScroll(scrollBehavior.nestedScrollConnection)
                    else Modifier.fillMaxSize(),
                    topBar = {
                        if (selectedItemIndex == 1)
                            TopAppBarProfileScreen()
                        else
                            LargeTopAppBar(
                                title = { Text(text = "ResQFood") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                actions = {
                                    IconButton(onClick = { /*TODO*/ }) {
                                        Icon(
                                            imageVector = Icons.Rounded.ShoppingCart,
                                            contentDescription = "Cart"
                                        )
                                    }
                                },
                                scrollBehavior = scrollBehavior,
                            )
                    },
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndex == index,
                                    onClick = {
                                        selectedItemIndex = index
                                    },
                                    label = { Text(text = item.title) },
                                    alwaysShowLabel = false,
                                    icon = {
                                        Icon(
                                            imageVector = if (index == selectedItemIndex) {
                                                item.selectedIcon
                                            } else {
                                                item.unselectedIcon
                                            },
                                            contentDescription = item.title
                                        )
                                    }
                                )
                            }
                        }
                    }
                ) { paddingValues ->
                    AnimatedContent(
                        targetState = selectedItemIndex,
                        label = "ScreenAnimation",
                        transitionSpec = {
                            val direction = if (targetState > initialState) {
                                AnimatedContentTransitionScope.SlideDirection.Left
                            } else {
                                AnimatedContentTransitionScope.SlideDirection.Right
                            }
                            slideIntoContainer(
                                towards = direction,
                                animationSpec = tween(300, easing = EaseIn)
                            ).togetherWith(
                                slideOutOfContainer(
                                    towards = direction,
                                    animationSpec = tween(300, easing = EaseOut)
                                )
                            )
                        }

                        ) { targetState ->
                        when (targetState) {
                            0 -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(paddingValues),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    // --- CATEGORY CHIP ROW UI ---
                                    CategoryChipRow(
                                        categories = topOffersViewModel.categories,
                                        selected = topOffersViewModel.selectedCategories,
                                        onChipClick = { topOffersViewModel.onCategoryChipClick(it) }
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    CardsSection(offers = topOffersViewModel.filteredOffers)
                                    Button(modifier = Modifier.padding(top = 16.dp), onClick = {
                                        navController.navigate(NavItemDetailScreen)
                                    }) {
                                        Text(text = "View Item Details")
                                    }
                                }
                            }
                            1 -> {
                                ProfileScreen(paddingValues, navController)
                            }
                            2 -> {
                                @Suppress("WrongTimberUsage")

                                Timber.tag("MainScreen").d("View Map")
                                val context = LocalContext.current
                                selectedItemIndex = 0
                                val intent = Intent(context, MapScreen::class.java)
                                context.startActivity(intent)
                            }
                        }
                    }
                }
            }
        })
    }

/**
 * CategoryChipRow renders a horizontal list of selectable category chips.
 * @param categories List of all category names (including 'All').
 * @param selected Set of currently selected categories (show highlighted).
 * @param onChipClick Lambda invoked with category when chip is clicked.
 */
@Composable
fun CategoryChipRow(
    categories: List<String>,
    selected: Set<String>,
    onChipClick: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .padding(top = 14.dp, bottom = 0.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        items(categories) { cat ->
            val isSelected = selected.contains(cat)
            FilterChip(
                selected = isSelected,
                onClick = { onChipClick(cat) },
                label = {
                    Text(cat, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                },
                modifier = Modifier,
                shape = RoundedCornerShape(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    val context = LocalContext.current
    MainScreen(navController = NavController(context = context))
}
