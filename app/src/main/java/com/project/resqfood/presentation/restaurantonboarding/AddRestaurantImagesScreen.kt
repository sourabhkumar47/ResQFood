package com.project.resqfood.presentation.restaurantonboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddRestaurantImages(
    data: ListingUIStateData,
    restaurantListingViewModel: ListingViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Restaurant Images",
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.height(4.dp))
        SecondaryText(text = "Your images will be directly visible to the customers")
        Spacer(modifier = Modifier.height(48.dp))
        DottedBorder(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), borderColor = MaterialTheme.colorScheme.outline,
            alignment = Alignment.TopStart,
            cornerRadius = CornerRadius(50.0f)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddImageButton {
                    //on Click
                }
            }
        }
    }
}

@Composable
fun DottedBorder(
    modifier: Modifier, cornerRadius: CornerRadius = CornerRadius(0.0f),
    alignment: Alignment = Alignment.Center,
    borderColor: Color, content: @Composable() (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier
            .drawWithContent {
                drawContent()
                drawPath(
                    path = Path().apply {
                        addRoundRect(
                            RoundRect(
                                0f,
                                0f,
                                size.width,
                                size.height,
                                cornerRadius = cornerRadius
                            )
                        )
                    },
                    brush = Brush.linearGradient(
                        colors = listOf(borderColor, borderColor),
                        start = Offset.Zero,
                        end = Offset(size.width, size.height),
                        tileMode = TileMode.Mirror
                    ),
                    style = Stroke(
                        width = 2f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                )
            },
        contentAlignment = alignment,
        content = content
    )
}


@Composable
private fun AddImageButton(onClick: () -> Unit) {
    DottedBorder(modifier = Modifier
        .clickable { onClick() },
        cornerRadius = CornerRadius(32.0f),
        borderColor = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = "Add Image",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 12.sp
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}