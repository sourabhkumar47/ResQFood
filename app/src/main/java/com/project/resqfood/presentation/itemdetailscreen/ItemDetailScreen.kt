package com.project.resqfood.presentation.itemdetailscreen


import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.project.resqfood.R
import com.project.resqfood.presentation.Destinations
import com.project.resqfood.ui.theme.backgroundDark
import com.project.resqfood.ui.theme.backgroundLight
import java.util.Calendar
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


private val backgroundColor = Color(49, 52, 58)
private val primaryColor = Color(68, 71, 70)
private val secondaryColor = Color(68, 71, 70)
private val selectedColor = Color(104, 220, 255, 255)
@Composable
fun ItemDetailScreen(navController: NavController){

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
                            modifier = Modifier.fillMaxSize(),
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
                                Text(
                                    text = "Chicken Salad",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.width(70.dp))
                                IconButton(
                                    onClick = {
                                              navController.navigate(Destinations.MainScreen.route)
                                    },
                                    modifier = Modifier
                                        .size(50.dp)
                                    ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = "Close this window"
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(30.dp))
                            Text(text = "Spit-roasted chicken with harissa-spiked yogurt, cucumber" +
                                    "radish, pickled red onion, and feta cheese over kale and romaine.")
                            Spacer(modifier = Modifier.height(30.dp))
                            TimePicker()
                            Spacer(modifier = Modifier.height(30.dp))
                            Button(onClick = {
                                navController.navigate(Destinations.OrderConfirmScreen.route)
                            },
                                modifier = Modifier.fillMaxWidth()
                                ) {
                                Text(text = "Claim at $5")
                            }
                        }


                    }

                }
            }


    }


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker() {
    var selectedHour: Int ? by remember { mutableStateOf(null) }
    var selectMinute: Int ? by remember { mutableStateOf(null) }
    var clockState = rememberSheetState()

    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            selectedHour = hours
            selectMinute = minutes
            Log.d("Selected time: ", "$hours:$minutes")
        }
    )
    Column {
        Row(
//        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
            Button(onClick = {
                clockState.show()
            }, colors = ButtonDefaults.buttonColors(backgroundDark)) {
                Text(text = "Pick Time", color = Color.White)
            }
            Spacer(modifier = Modifier.width(20.dp))
            if (selectedHour != null && selectMinute != null) {
                Text(text = "Selected Time: ${formatTime(selectedHour!!, selectMinute!!)}")
            } else {
                Text(text = "No Time selected yet")
            }
        }
    }
}

private fun formatTime(hours: Int, minutes: Int): String{
    var formattedHours = String.format("%02d", hours)
    var formattedMinutes = String.format("%02d", minutes)
    return "$formattedHours:$formattedMinutes"
}
