package com.project.resqfood.presentation.additemscreen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.clock.ClockDialog
import com.maxkeppeler.sheets.clock.models.ClockSelection
import com.project.resqfood.R
import com.project.resqfood.presentation.Destinations
import com.project.resqfood.presentation.uploadscreen.ImageUploadScreen
import com.project.resqfood.ui.theme.backgroundDark


@Composable
fun PickTimeScreen(navController: NavController){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(
                    onClick = {
                        navController.navigate(Destinations.SetPriceScreen.route)
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return to previous page"
                    )
                }
                Spacer(modifier = Modifier.width(5.dp))
                Text(
//                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = stringResource(id = R.string.pick_time),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.displaySmall
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                text = stringResource(id = R.string.menu_courses),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            MenuCourses()

            Spacer(modifier = Modifier.padding(8.dp))

            TimePicker()

            Spacer(modifier = Modifier.padding(8.dp))

            ImageUploadScreen()

            Spacer(modifier = Modifier.padding(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = {
                    navController.navigate(Destinations.MainScreen.route)
                }
            ) {
                Text(
                    text = stringResource(id = R.string.confirm_save_item),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }

    }
}

@Composable
fun MenuCourses(){
    val selectedValue = remember { mutableStateOf("") }
    val isSelectedItem: (String) -> Boolean = { selectedValue.value == it}
    val onChangeState: (String) -> Unit = { selectedValue.value = it}
    val items = listOf("Appetizers", "Main Courses", "Desserts", "Others")
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(text = "Selected value: ${selectedValue.value.ifEmpty { "None" }}")
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .selectable(
                        selected = isSelectedItem(item),
                        onClick = { onChangeState(item) },
                        role = Role.RadioButton
                    )
                    .padding(
                        8.dp
                    )
            ) {
                RadioButton(
                    selected = isSelectedItem(item),
                    onClick = null
                )
                Text(
                    text = item,
                    modifier = Modifier.fillMaxWidth()
                )
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
