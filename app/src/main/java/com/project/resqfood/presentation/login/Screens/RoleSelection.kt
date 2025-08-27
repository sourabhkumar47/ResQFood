package com.project.resqfood.presentation.login.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.resqfood.presentation.restaurantonboarding.NavListingRestaurant
import kotlinx.serialization.Serializable

@Serializable
object NavRoleSelectScreen
@Composable
fun ResqFoodUserSelection(navController: NavHostController?) {
        Card(
            modifier = Modifier
                .fillMaxSize(),
            colors = CardDefaults.cardColors(
                containerColor = Color.Magenta.copy(alpha = 0.95f)
            ),
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF5E5E),
                                Color(0xFFFFC78F)
                            ),
                            start = Offset(80f, 0f),
                            end = Offset(900f, 820f) // ~60° angle
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header Section
                    Spacer(modifier = Modifier.height(48.dp))

                    Text(
                        text = "ResqFood",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Reduce Food Waste, Save Money",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(48.dp))

                    // Restaurant Option
                    UserTypeCard(
                        icon = {
                            RestaurantIcon()
                        },
                        title = "Restaurant",
                        description = "List your leftover food and reduce waste while earning extra revenue",
                        onClick = {  navController?.navigate(NavListingRestaurant(entryPoint =  "fromRegistration")) }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Customer Option
                    UserTypeCard(
                        icon = {
                            CustomerIcon()
                        },
                        title = "Customer",
                        description = "Find delicious food at discounted prices and help reduce food waste",
                        onClick = { navController?.navigate(NavMainScreen) }
                    )

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

}

@Composable
fun UserTypeCard(
    icon: @Composable () -> Unit,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            icon()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E2E2E)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun RestaurantIcon() {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                color = Color(0xFF4CAF50),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Simple restaurant building icon
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Restaurant, // 🍴 restaurant icon
                contentDescription = "Restaurant",
                tint = Color.White,
                modifier = Modifier.size(40.dp) // adjust size
            )
        }
    }
}

@Composable
fun CustomerIcon() {
    Box(
        modifier = Modifier
            .size(64.dp)
            .background(
                color = Color(0xFFFF7043),
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Simple person icon
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Head
            Icon(
                imageVector = Icons.Default.Person, // 👈 built-in person icon
                contentDescription = "User",
                tint = Color.White,
                modifier = Modifier.size(40.dp) // adjust size as needed
            )
        }
    }
}

/*
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResqFoodUserSelectionScreenPreview() {
    MaterialTheme {
        ResqFoodUserSelectionScreen()
    }
}*/
