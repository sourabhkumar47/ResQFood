package com.project.resqfood.presentation.login.BottomNavigation

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import com.project.resqfood.presentation.Destinations

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val destinationId: Destinations = Destinations.MainScreen
)

