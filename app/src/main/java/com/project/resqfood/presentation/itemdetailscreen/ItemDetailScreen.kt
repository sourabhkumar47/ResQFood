package com.project.resqfood.presentation.itemdetailscreen


import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.project.resqfood.R
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import com.project.resqfood.ui.theme.backgroundDark
import kotlinx.serialization.Serializable


private val backgroundColor = Color(49, 52, 58)
private val primaryColor = Color(68, 71, 70)
private val secondaryColor = Color(68, 71, 70)
private val selectedColor = Color(104, 220, 255, 255)

@Serializable
object NavItemDetailScreen

@Composable
fun ItemDetailScreen(navController: NavController){
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
                                              navController.navigate(NavMainScreen)
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
                            var selectedHour: Int by remember { mutableIntStateOf(0) }
                            var selectedMinute: Int by remember { mutableIntStateOf(0) }
                            var isTimeSelected: Boolean by remember{ mutableStateOf(false) }
                            TimePicker(selectedMinute = selectedMinute, selectedHour = selectedHour, isTimeSelected = isTimeSelected){ hours, minutes ->
                                selectedHour = hours
                                selectedMinute = minutes
                                isTimeSelected = true
                                Log.d("Selected time: ", "$hours:$minutes")
                            }

                            Spacer(modifier = Modifier.height(30.dp))
                            Button(onClick = {
                                if(isTimeSelected){
                                    navController.navigate(NavOrderConfirmScreen)
                                }
                                else{
                                    Toast.makeText(localContext, "Please select timing to proceed", Toast.LENGTH_SHORT).show()
                                }
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
fun TimePicker(selectedHour: Int, selectedMinute: Int, isTimeSelected: Boolean, onSelectionClock: (Int, Int)-> Unit) {
    var clockState = rememberSheetState()
    ClockDialog(
        state = clockState,
        selection = ClockSelection.HoursMinutes { hours, minutes ->
            onSelectionClock(hours, minutes)
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
            if (isTimeSelected) {
                Text(text = "Selected Time: ${formatTime(selectedHour, selectedMinute)}")
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
