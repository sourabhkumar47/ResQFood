package com.project.resqfood.presentation.itemdetailscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.navOptions
import com.project.resqfood.R
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import com.project.resqfood.ui.theme.backgroundDark
import kotlinx.serialization.Serializable


@Serializable
object NavOrderConfirmScreen

@Composable
fun OrderConfirmScreen( navController: NavController){LocalContext.current
    val localContext = LocalContext.current
    Column {
        Row {
            Card(
                modifier = Modifier
                    .height(340.dp)
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(0.dp)
            ) {
                Column {
                    val imageId = R.drawable.trust6
                    Image(
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(0.dp)),
                        contentScale = ContentScale.Crop,
                        painter = painterResource(id = imageId),
                        contentDescription = null
                    )
                    Text(text = "Image Section")
                }
            }
        }
        Row{
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -50.dp),
                contentAlignment = Alignment.Center,

                ){
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
                ) {
                    Spacer(modifier = Modifier.height(30.dp))
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Row (
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            IconButton(
                                onClick = {
                                    navController.navigate(NavItemDetailScreen)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Return to previous page"
                                )
                            }
                            Text(
                                text = "Order Confirmation",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }


                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dumpling House",
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Text(text = "$5 for 1 item")
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.buttonColors( backgroundDark)
                            ) {
                                Text(text = "Payment method", color = Color.White)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.buttonColors( backgroundDark)
                                ) {
                                Text(text = "Direction to store", color = Color.White)
                            }
                        }

                        Spacer(modifier = Modifier.height(30.dp))
                        Button(onClick = {
                            Toast.makeText(localContext, "Order Placed", Toast.LENGTH_SHORT).show()
                            navController.navigate(NavMainScreen){
                                navOptions {
                                    popUpTo(NavMainScreen){
                                        inclusive = false
                                    }
                                }
                            }
                        },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "Place Order")
                        }
                    }


                }

            }
        }


    }


}