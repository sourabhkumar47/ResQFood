package com.project.resqfood.presentation.login.BottomNavigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Warehouse
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.project.resqfood.presentation.BottomNavigation.BottomNavigationItem

@Composable
fun MainScreen(
    navController: NavController
) {
    val items = listOf(
        BottomNavigationItem(
            title = "Support",
            selectedIcon = Icons.Filled.Handshake,
            unselectedIcon = Icons.Outlined.Handshake
        ),
        BottomNavigationItem(
            title = "Trusts",
            selectedIcon = Icons.Filled.Warehouse,
            unselectedIcon = Icons.Outlined.Warehouse
        ),
        BottomNavigationItem(
            title = "Messages",
            selectedIcon = Icons.Filled.Message,
            unselectedIcon = Icons.Outlined.Message,
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
        Scaffold(
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
                                    imageVector = if (index == selectedItemIndex){
                                        item.selectedIcon
                                    } else{
                                        item.unselectedIcon
                                    },
                                    contentDescription =item.title )

                            }


                        )


                    }

                }
            }
        ) { PaddingValues ->
            Column(
                modifier = Modifier.padding(top = PaddingValues.calculateTopPadding())
            ) {
                Text(text = "Hello")

            }

        }

    }
}