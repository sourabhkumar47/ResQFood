package com.project.resqfood.presentation.restaurantonboarding

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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

/*@Serializable
object NavListingRestaurant*/


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class NavListingRestaurant(val entryPoint: String)



@Composable
fun ListingRestaurantScreen(
    restaurantListingViewModel: ListingViewModel,
    navController: NavHostController,
    entryPoint: String
) {
    val scrollState = rememberScrollState()
    val data by restaurantListingViewModel.listingData
    val uiState by restaurantListingViewModel.uiState
    val snackbarHostState = remember {
        SnackbarHostState()
    }
//    if(!restaurantListingViewModel.isStart)
//    restaurantListingViewModel.validateRestaurantDetails(snackbarHostState)
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }

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
                    .padding(12.dp),
                    isFirst = (uiState == ListingUIState.RESTAURANT_SUCCESS_SCREEN || uiState == ListingUIState.RESTAURANT_DETAILS_SCREEN),
                    onNext = { restaurantListingViewModel.onNextClick(navController = navController, snackbarHostState = snackbarHostState,entryPoint = entryPoint) }) {
                    restaurantListingViewModel.onBackClick()
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(scrollState)
                            .width(460.dp)
                            .padding(top = 32.dp, bottom = 32.dp, start = 32.dp, end = 32.dp)
                    ) {
                        AnimatedContent(
                            targetState = uiState,
                            label = "",
//                            transitionSpec = {
//                                slideIntoContainer(
//                                    animationSpec = tween(300, easing = EaseIn),
//                                    towards = AnimatedContentTransitionScope.SlideDirection.Left
//                                ).togetherWith(
//                                    slideOutOfContainer(
//                                        animationSpec = tween(300, easing = EaseOut),
//                                        towards = AnimatedContentTransitionScope.SlideDirection.Left
//                                    )
//                                )
//                            }
                        ) { targetState ->
                            when (targetState) {
                                ListingUIState.RESTAURANT_DETAILS_SCREEN -> RestaurantDetailScreen(
                                    data = data,
                                    restaurantListingViewModel = restaurantListingViewModel
                                )

                                ListingUIState.RESTAURANT_CONTACT_DETAILS_SCREEN -> RestaurantContactScreen(
                                    data = data,
                                    restaurantListingViewModel = restaurantListingViewModel
                                )

                                ListingUIState.RESTAURANT_OWNER_DETAILS_SCREEN -> RestaurantOwnerDetailScreen(
                                    data = data,
                                    restaurantListingViewModel = restaurantListingViewModel
                                )

                                ListingUIState.RESTAURANT_TYPE_SCREEN -> RestaurantTypeScreen(
                                    data = data,
                                    restaurantListingViewModel = restaurantListingViewModel
                                )

                                ListingUIState.RESTAURANT_WORKING_WEEK_SCREEN -> RestaurantOpenDaysScreen(
                                    data = data,
                                    restaurantListingViewModel = restaurantListingViewModel
                                )

                                ListingUIState.RESTAURANT_WORKING_HOURS_SCREEN -> RestaurantOperationalHoursScreen(
                                    data = data,
                                    restaurantListingViewModel = restaurantListingViewModel
                                )

                                ListingUIState.RESTAURANT_IMAGES_SCREEN -> AddRestaurantImages(
                                    data = data,
                                    restaurantListingViewModel = restaurantListingViewModel
                                )

                                ListingUIState.RESTAURANT_LOADING_SCREEN -> RestaurantLoadingScreen()
                                ListingUIState.RESTAURANT_SUCCESS_SCREEN -> RestaurantSuccessScreen()
                            }
                        }
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
        if (!isFirst) {
            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth(0.5f)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Back")
            }
            Spacer(modifier = Modifier.width(16.dp))
        }
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth(if (isFirst) 1f else 1.0f)
        ) {
            Text(text = "Next")
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}