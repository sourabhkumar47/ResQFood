package com.project.resqfood.presentation.login.BottomNavigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Warehouse
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Handshake
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Warehouse
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.resqfood.R
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
        ) { PaddingValues ->
            Column(
                modifier = Modifier
                    .padding(top = PaddingValues.calculateTopPadding())
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.primaryContainer)
                        .fillMaxWidth()
                        .padding(25.dp)
                        .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)),

                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column (

                    ){
                        Text(
                            maxLines = 1,
                            text = "When we give Cheerfully",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            maxLines = 1,
                            text = "and accept gratefully,",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            maxLines = 1,
                            text = "everyone is blessed.",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            maxLines = 1,
                            text = "Your help will bring smiles to",
                            fontSize = 15.sp
                        )
                        Text(
                            maxLines = 1,
                            text = "the people who are in need :) ",
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {  }) {
                            Text(text = "Let's Help")
                            Icon(
                                modifier = Modifier
                                    .size(16.dp)
                                    ,
                                painter = painterResource(id = R.drawable.support),
                                contentDescription = "Button")

                        }

                    }
                    Box( ){
                        Image(
                            modifier = Modifier.padding(top = 50.dp),
                            painter = painterResource(id = R.drawable.charity), contentDescription ="image" )

                    }


                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically


                        ){
                    Text(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        text = "Charities near you"
                    )
                    Icon(
                        modifier = Modifier.size(25.dp),
                        imageVector = Icons.Rounded.Search,
                        contentDescription ="Search",

                    )

                }

                    Trust()





            }

        }

    }
}