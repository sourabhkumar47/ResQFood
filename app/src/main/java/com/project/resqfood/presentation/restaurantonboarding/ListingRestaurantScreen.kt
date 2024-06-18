package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.project.resqfood.R
import kotlinx.serialization.Serializable

@Serializable
object NavListingRestaurant


@Composable
fun ListingRestaurantScreen(
    restaurantListingViewModel: ListingViewModel,
    navController: NavHostController
) {
    val scrollState = rememberScrollState()
    val data by restaurantListingViewModel.listingData
    val uiState by restaurantListingViewModel.uiState
    Scaffold(
        modifier = Modifier
            .fillMaxSize()

    ) {
        val padding = it
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
                )
                .statusBarsPadding()
        ) {
            AsyncImage(
                model = R.drawable.safe_food, contentDescription = null,
                modifier = Modifier
                    .height(130.dp)
                    .align(Alignment.TopEnd)
                    .padding(0.dp, 16.dp, 16.dp, 0.dp)
            )
            Column(
                modifier = Modifier.padding(16.dp, 48.dp, 16.dp, 0.dp)
            ) {
                Text(
                    text = "List your\nrestaurant",
                    style = TextStyle(
                        color = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onPrimaryContainer,
                        fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
                        fontSize = 24.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Join our rich community of\n restaurants",
                    style = TextStyle(
                        color = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.outlineVariant
                        else MaterialTheme.colorScheme.outline,
                        fontStyle = MaterialTheme.typography.bodySmall.fontStyle,
                        fontSize = 14.sp,
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .fillMaxHeight(0.75f)
                    .clip(RoundedCornerShape(30.dp, 30.dp, 0.dp, 0.dp))
                    .background(color = MaterialTheme.colorScheme.background)
                    .navigationBarsPadding()
            ) {
                BottomScreen(modifier = Modifier
                    .align(
                        Alignment.BottomEnd
                    )
                    .padding(12.dp), onNext = { /*TODO*/ }) {

                }
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 64.dp)) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .width(360.dp)
                            .padding(top = 32.dp, bottom = 32.dp, start = 32.dp, end = 32.dp)
                    ) {
//                    MapsScreen()
//                        RestaurantOwnerDetailScreen(
//                            data = data,
//                            restaurantListingViewModel = restaurantListingViewModel
//                        )
//                        RestaurantDetailScreen(data, restaurantListingViewModel)
//                        RestaurantContactScreen(data = data, restaurantListingViewModel = restaurantListingViewModel)
//                        RestaurantTypeScreen(data = data, restaurantListingViewModel =restaurantListingViewModel)
//                        RestaurantOpenDaysScreen(data = data, restaurantListingViewModel = restaurantListingViewModel)
//                        RestaurantOperationalHoursScreen(
//                            data = data,
//                            restaurantListingViewModel = restaurantListingViewModel
//                        )
                        AddRestaurantImages(data = data, restaurantListingViewModel = restaurantListingViewModel)
                    }
                }

            }
        }
    }
}

//@Composable
//fun MapsScreen(){
//    val newDelhi = LatLng(28.6139, 77.2090)
//    val cameraPositionState = rememberCameraPositionState {
//        position = CameraPosition.fromLatLngZoom(newDelhi, 10f)
//    }
//    Column {
//        Text(text = "Choose Location",
//            style = TextStyle(
//                color = MaterialTheme.colorScheme.onBackground,
//                fontSize = 20.sp
//            ),
//            modifier = Modifier.padding(16.dp)
//        )
//        GoogleMap(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp),
//            cameraPositionState = cameraPositionState
//        )
//    }
//}

@Composable
private fun BottomScreen(
    modifier: Modifier = Modifier,
    isFirst: Boolean = true,
    onNext: () -> Unit,
    onBack: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        if (isFirst) {
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth(0.5f)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(if (isFirst) 1f else 0.5f)
        ) {
            Text(text = "Next")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}