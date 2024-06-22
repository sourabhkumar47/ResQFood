package com.project.resqfood.presentation.itemdetailscreen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import com.project.resqfood.R
import kotlinx.serialization.Serializable


@Serializable
object SuccessScreen


@Composable
fun SuccessScreen(
    buttonText: String,
    onClick: () -> Unit,
    animationJsonResId: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie Animation
        val composition by rememberLottieComposition(
            spec = LottieCompositionSpec.RawRes(animationJsonResId)
        )
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = Int.MAX_VALUE
        )
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier
                .padding(16.dp)
                .size(200.dp)
        )


        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Success!", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))


        // Button
        Button(
            onClick = onClick,
            modifier = Modifier.padding(16.dp)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text(text = buttonText, color = Color.White)
        }
    }
}


@Preview
@Composable
fun PreviewSuccessScreen() {
    SuccessScreen(
        buttonText = "Continue",
        onClick = {},
        animationJsonResId = R.raw.successanimation
    )
}

