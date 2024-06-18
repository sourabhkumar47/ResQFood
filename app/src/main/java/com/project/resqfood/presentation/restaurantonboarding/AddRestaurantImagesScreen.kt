package com.project.resqfood.presentation.restaurantonboarding

import android.content.ContentResolver
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun AddRestaurantImages(
    data: ListingUIStateData,
    restaurantListingViewModel: ListingViewModel
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            restaurantListingViewModel.addImage(it)
        }
    }
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
                .heightIn(min = 120.dp), borderColor = MaterialTheme.colorScheme.outline,
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
                    imagePickerLauncher.launch("image/*")
                }
                Spacer(modifier = Modifier.height(16.dp))
                data.restaurantImages.forEachIndexed { index, uri ->
                    ImageRow(uri) {
                        restaurantListingViewModel.removeImage(uri)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
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
    DottedBorder(
        modifier = Modifier
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

@Composable
private fun ImageRow(uri: Uri, onRemove: () -> Unit) {
    val contentResolver = LocalContext.current.contentResolver
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = uri, contentDescription = null, modifier = Modifier
                    .size(70.dp)
                    .padding(8.dp)
            )
            Text(
                text = getImageName(contentResolver, uri) ?: "Image",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(110.dp)
            )
            Text(
                text = "${getImageFileSizeFromUri(contentResolver, uri)}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .width(70.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { onRemove() }) {
                Icon(
                    imageVector = Icons.Default.RemoveCircleOutline, contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}


private fun getImageName(contentResolver: ContentResolver, uri: Uri): String? {
    var imageName: String? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
    if (cursor != null && cursor.moveToFirst()) {
        val columnIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        imageName = cursor.getString(columnIndex)
    }
    cursor?.close()
    return imageName
}

fun getImageFileSizeFromUri(contentResolver: ContentResolver, uri: Uri): String? {
    var fileSize: Long? = null
    val cursor = contentResolver.query(uri, null, null, null, null)
    if (cursor != null && cursor.moveToFirst()) {
        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
        fileSize = cursor.getLong(sizeIndex)
    }
    cursor?.close()

    // Convert the file size to KB, MB, and GB
    val fileSizeInKB = fileSize?.div(1024.0)
    val fileSizeInMB = fileSizeInKB?.div(1024.0)
    val fileSizeInGB = fileSizeInMB?.div(1024.0)

    // Format the file size string
    return when {
        fileSizeInGB != null && fileSizeInGB > 1 -> String.format("%.2f GB", fileSizeInGB)
        fileSizeInMB != null && fileSizeInMB > 1 -> String.format("%.2f MB", fileSizeInMB)
        fileSizeInKB != null && fileSizeInKB > 1 -> String.format("%.2f KB", fileSizeInKB)
        else -> "$fileSize bytes"
    }
}