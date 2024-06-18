package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.project.resqfood.R

@Composable
fun RestaurantSuccessScreen(){
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.add_success))
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Restaurant added successfully!",
            style = MaterialTheme.typography.headlineMedium
        )
        LottieAnimation(composition = composition, iterations = 1000,
            modifier = Modifier.size(400.dp))
    }
}

@Composable
fun RestaurantLoadingScreen(){
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loading_animation))
    Column(modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Adding restaurant...",
            style = MaterialTheme.typography.headlineMedium
            )
        LottieAnimation(composition = composition, iterations = 1000,
            modifier = Modifier.size(400.dp))
    }
}