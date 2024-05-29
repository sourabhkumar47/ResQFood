package com.project.resqfood.presentation.itemdetailscreen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation

@Composable
fun AddImagesScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    viewModel: LeftOverViewModel
) {
    var images by remember { mutableStateOf(listOf<Uri>()) }
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            images = images + it
        }
    }

            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(modifier = Modifier.fillMaxWidth(0.8f), onClick = { /*TODO*/
                    viewModel.leftOverItem.images = images
                navController.navigate("success")}) {
                    Text("Submit")
                }
                OutlinedButton(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Upload Image")
                }
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(0.8f)
                        .fillMaxHeight(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(images.size) { index ->
                        val imageUri = images[index]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        ImageRequest.Builder(
                                            LocalContext.current
                                        ).data(data = imageUri).apply(block = fun ImageRequest.Builder.() {
                                            transformations(CircleCropTransformation())
                                        }).build()
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Text("Image ${index + 1}")
                                    Spacer(modifier = Modifier.height(4.dp))

                                }

                                Spacer(modifier = Modifier.weight(1f))

                                IconButton(
                                    onClick = { images = images - imageUri }
                                ) {
                                    Icon(
                                        painter = painterResource(id = android.R.drawable.ic_delete),
                                        contentDescription = "Delete Image",
                                        tint = Color.Red
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }