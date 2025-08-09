package com.project.resqfood.presentation.restaurantDashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.recaptcha.internal.zzip
import com.project.resqfood.domain.repository.RestaurantOrderStats
import com.project.resqfood.model.RestaurantFoodSavedData
import com.project.resqfood.presentation.restaurantonboarding.AddressEntity
import com.project.resqfood.ui.theme.errorLight
import com.project.resqfood.ui.theme.onErrorContainerLight
import com.project.resqfood.ui.theme.onSurfaceLight
import com.project.resqfood.ui.theme.onSurfaceVariantLight
import com.project.resqfood.ui.theme.outlineLight
import com.project.resqfood.ui.theme.primaryContainerLight
import com.project.resqfood.ui.theme.primaryLight
import com.project.resqfood.ui.theme.surfaceLight
import com.project.resqfood.ui.theme.surfaceVariantLight
import com.project.resqfood.ui.theme.tertiaryContainerLight
import kotlinx.serialization.Serializable

@Serializable
object NavRestaurantDashboardScreen

@Composable
fun RestaurantDashboardScreen(
    navController: NavController,
    restaurantViewModel: RestaurantViewModel
){
    val restaurantName = restaurantViewModel.restaurantName
    val restaurantAddress = restaurantViewModel.restaurantAddress

    val foodGoal = restaurantViewModel.goal
    val foodSaved = restaurantViewModel.saved
    val foodRemaining = restaurantViewModel.remaining

    val order = restaurantViewModel.summary

    val userName = restaurantViewModel.userName
    val userReview = restaurantViewModel.userReview

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surfaceLight)
            .padding(18.dp),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ){
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "ResQFood",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = restaurantName,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = restaurantAddress.formatAddress(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        RestaurantFoodSavedCard(
            stats = RestaurantFoodSavedData(foodGoal, foodSaved, foodRemaining),
            onClick = {}
        )

        Spacer(modifier = Modifier.height(30.dp))

        RestaurantOrderStatsCard(
            stats = RestaurantOrderStats(
                order.completed,
                order.pending,
                order.cancelled
            ),
            onClick = {}
        )
        Spacer(modifier = Modifier.height(50.dp))
        AddReview(
            reviewClick = { showDialog = true }
        )
        if (showDialog){
            AddReviewDialog(
                title = "ADD REVIEW",
                label1 = "Username",
                label2 = "Review",
                onDismiss = { showDialog = false },
                onConfirm = {
                            name, review ->
                    restaurantViewModel.addReview(name, review)
                    showDialog = false
                }
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        LazyRow (
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ){
            items(restaurantViewModel.restaurantReviews){
                review ->
                ReviewCard(
                    userName = review.userName,
                    review = review.userReview
                )
            }
        }
    }
}

private fun AddressEntity.formatAddress(): String {
    val parts = listOfNotNull(
        street.takeIf { it.isNotBlank() },
        city.takeIf { it.isNotBlank() },
        state.takeIf { it.isNotBlank() },
        postalCode.takeIf { it.isNotBlank() }
    )
    return parts.joinToString(", ")
}


@Composable
fun RestaurantFoodSavedCard(
    stats: RestaurantFoodSavedData,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp),
    containerColor: Color = primaryContainerLight.copy(0.4f),
    contentColor: Color = primaryLight,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    onClick: () -> Unit = {}
) {
    var isCollapsed by rememberSaveable { mutableStateOf(true) }

    val progress = stats.saved.toFloat() / stats.goal.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(1000)
    )

    Card(
        modifier = modifier,
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = elevation,
        border = border,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(30.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AnimatedVisibility(
                visible = !isCollapsed,
                modifier = Modifier
                    .padding(end = 40.dp)
                    .animateContentSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text("Goal: ${stats.goal}", style = MaterialTheme.typography.bodyLarge)
                    Text("Saved: ${stats.saved}", style = MaterialTheme.typography.bodyLarge)
                    Text("Remaining: ${stats.remaining}", style = MaterialTheme.typography.bodyLarge)
                }
            }


            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxHeight()
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Canvas(modifier = Modifier.size(150.dp)) {
                        drawArc(
                            color = errorLight.copy(0.8f),
                            sweepAngle = 360f,
                            startAngle = -90f,
                            useCenter = false,
                            size = size,
                            style = Stroke(width = 20f)
                        )
                        drawArc(
                            color = onSurfaceLight.copy(0.8f),
                            sweepAngle = 360f * animatedProgress,
                            startAngle = -90f,
                            useCenter = false,
                            size = size,
                            style = Stroke(width = 20f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${stats.saved}/${stats.goal}",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                        Text(
                            text = "Food Saved",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(onClick = { isCollapsed = !isCollapsed }) {
                    Box(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(0.6f)),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(
                            imageVector = if (isCollapsed) Icons.Default.ChevronRight else Icons.Default.ChevronLeft,
                            contentDescription = "Toggle Details",
                            tint = surfaceVariantLight
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RestaurantOrderStatsCard(
    stats : RestaurantOrderStats,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    containerColor: Color = Color.Transparent,
    elevation: CardElevation = CardDefaults.cardElevation(),
    onClick: () -> Unit
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor
        ),
        elevation = elevation,
        border = BorderStroke(4.dp, outlineLight)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatsItem(
                label = "Completed",
                value = stats.completed.toString(),
                onSurfaceVariantLight
            )
            StatsItem(
                label = "Pending",
                value = stats.pending.toString(),
                primaryLight
            )
            StatsItem(
                label = "Cancelled",
                value = stats.cancelled.toString(),
                errorLight
            )
        }
    }
}

@Composable
fun StatsItem(
    label : String,
    value : String,
    color : Color
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = color,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = color,
        )
    }
}

@Composable
fun ReviewCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    containerColor: Color = tertiaryContainerLight,
    contentColor: Color = onErrorContainerLight,
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    userName : String,
    review : String
){
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = elevation,
        border = border,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )

            }

            Text(
                text = review,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                softWrap = true,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@Composable
fun AddReview(
    reviewClick : () -> Unit,
    shape: Shape = RoundedCornerShape(2.dp),
    elevation: Dp = 4.dp
){
    Button(
        modifier = Modifier.fillMaxWidth(),
        shape = shape,
        elevation = ButtonDefaults.elevatedButtonElevation(elevation),
        onClick = reviewClick
    ){
        Text(
            text = "ADD REVIEW",
            modifier = Modifier.padding(5.dp)
        )
    }
}

@Composable
fun AddReviewDialog(
    title: String,
    label1: String,
    label2: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var input1 by rememberSaveable { mutableStateOf("") }
    var input2 by rememberSaveable { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = input1,
                    onValueChange = { input1 = it },
                    label = { Text(label1) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = input2,
                    onValueChange = { input2 = it },
                    label = { Text(label2) },
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(input1.trim(), input2.trim())
                },
                enabled = input1.isNotBlank() && input2.isNotBlank()
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun RestaurantOrderStatsCardPreview(){
//    RestaurantOrderStatsCard(
//        stats = RestaurantOrderStats(10, 5, 2)
//    )
//    RestaurantDashboardScreen()
}