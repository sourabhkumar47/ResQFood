package com.project.resqfood.presentation.login.Screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Down
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AutoStories
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.resqfood.presentation.communityScreen.NavCommunityScreen
import com.project.resqfood.presentation.itemdetailscreen.NavItemDetailScreen
import com.project.resqfood.presentation.login.BottomNavigation.BottomNavigationItem
import com.project.resqfood.presentation.profilescreens.ProfileScreen
import com.project.resqfood.presentation.profilescreens.TopAppBarProfileScreen
import kotlinx.serialization.Serializable

@Serializable
object NavMainScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController
) {
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
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        Scaffold(
            modifier = if (selectedItemIndex == 0) Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
            else
                Modifier.fillMaxSize(),
            topBar = {
                if (selectedItemIndex == 1)
                    TopAppBarProfileScreen()
                else
                    LargeTopAppBar(
                        title = {
                            Text(text = "ResQFood")
                        },
                        navigationIcon = {
                            IconButton(onClick = { /*TODO*/ }) {
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
                            label = {
                                Text(text = item.title)
                            },
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
            AnimatedContent(targetState = selectedItemIndex,
                label = "",
                transitionSpec = {
                    slideIntoContainer(
                        animationSpec = tween(300, easing = EaseIn),
                        towards = Up
                    ).togetherWith(
                        slideOutOfContainer(
                            animationSpec = tween(300, easing = EaseOut),
                            towards = Down
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
                            CardsSection()
                            Button(modifier = Modifier.padding(top = 16.dp), onClick = {
//                                navController.navigate(NavItemDetailScreen)
                                navController.navigate(NavCommunityScreen)
                            }) {
                                Text(text = "View Item Details")
                            }
                        }
                    }

                    1 -> {
                        ProfileScreen(paddingValues, navController)
                    }
                }
            }

        }
    }

}




