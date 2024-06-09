package com.project.resqfood.presentation.itemdetailscreen


import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.project.resqfood.R
import com.project.resqfood.model.FoodCategory
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.Calendar




@Serializable
object NavAddingLeftovers


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddingLeftovers(outerNavController: NavController) {
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = "Adding Leftovers") },
            navigationIcon = {
                IconButton(onClick = { outerNavController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { paddingValues ->
        val navController = rememberNavController()
        val viewModel: LeftOverViewModel = viewModel()
        NavHost(
            navController = navController,
            startDestination = "screen1",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(
                "screen1",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                }
            ) {
                Screen1(paddingValues, viewModel, navController)
            }


            composable(
                "screen2/{itemName}/{description}/{category}",
                arguments = listOf(
                    navArgument("itemName") { type = NavType.StringType },
                    navArgument("description") { type = NavType.StringType },
                    navArgument("category") { type = NavType.StringType }  // Assuming FoodCategory can be represented as a string
                ),
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                }
            ) { backStackEntry ->
                val itemName = backStackEntry.arguments?.getString("itemName") ?: ""
                val description = backStackEntry.arguments?.getString("description") ?: ""
                val category = backStackEntry.arguments?.getString("category") ?: ""
                Screen2(paddingValues,navController, viewModel, itemName,description, category)
            }


            composable(
                "screen3",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                }
            ) {
                AddImagesScreen(
                    paddingValues = paddingValues,
                    navController = navController,
                    viewModel = viewModel
                )
            }
            composable(
                "success",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                exitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                },
                popEnterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { fullWidth -> -fullWidth },
                        animationSpec = tween(700)
                    ) + fadeIn(animationSpec = tween(700))
                },
                popExitTransition = {
                    slideOutHorizontally(
                        targetOffsetX = { fullWidth -> fullWidth },
                        animationSpec = tween(700)
                    ) + fadeOut(animationSpec = tween(700))
                }
            ) {
                SuccessScreen(buttonText = "Next", onClick = {
                    outerNavController.navigate(
                        NavMainScreen
                    )
                }, animationJsonResId = R.raw.successanimation)
            }
        }
    }
}


@Composable
private fun Screen1(
    paddingValues: PaddingValues,
    viewModel: LeftOverViewModel,
    navController: NavController
) {
    var itemName by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var category by remember {
        mutableStateOf<FoodCategory?>(null)
    }
    var isExpanded by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {


                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    label = { Text("Item Name") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description...") },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(100.dp)
                )
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
                OutlinedTextField(
                    value = category?.name ?: "",
                    readOnly = true,
                    onValueChange = {},
                    trailingIcon = {
                        IconButton(onClick = {
                            isExpanded = true
                        }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = "Back")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clickable { /*TODO*/ },
                    label = { Text("Category") })
            }
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    DropdownMenu(
                        expanded = isExpanded, onDismissRequest = { isExpanded = false },
                        modifier = Modifier
                            .align(Alignment.BottomCenter) // Align dropdown with top left corner of TextField
                            .offset(
                                x = 0.dp,
                                y = 50.dp
                            ) // Offset the dropdown slightly below the TextField
                            .heightIn(max = 200.dp) // Limit dropdown menu height
//                    .verticalScroll(rememberScrollState())// Offset the dropdown slightly below the TextField
                    ) {
                        FoodCategory.entries.forEach {
                            DropdownMenuItem(text = {
                                Text(it.name)
                            }, onClick = {
                                category = it
                                isExpanded = false
                            })
                        }
                    }
                }
            }
        }


        Button(
            onClick = {
                if (itemName.isEmpty() || description.isEmpty()) {
                    Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (category == null) {
                    Toast.makeText(context, "Please select the category", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                viewModel.leftOverItem.name = itemName
                viewModel.leftOverItem.description = description
                viewModel.leftOverItem.category = category as FoodCategory
                navController.navigate("screen2/$itemName/$description/${category?.name}")
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Next")
        }
    }
}


@Composable
private fun Screen2(
    paddingValues: PaddingValues,
    navController: NavController,
    viewModel: LeftOverViewModel,
    itemName: String,
    description: String,
    category: String
) {
    var quantity by remember { mutableStateOf("") }
    val db = Firebase.firestore
    var estimatedPrice by remember { mutableStateOf("") }
    var isFree by remember { mutableStateOf(false) }
    var deliveryStartTime by remember { mutableStateOf(getCurrentTime()) }
    var deliveryEndTime by remember { mutableStateOf(getCurrentTime()) }
    var deliveryStartDate by remember {
        mutableStateOf(getCurrentDate())
    }
    var deliveryEndDate by remember {
        mutableStateOf(getCurrentDate())
    }
    Log.d("Screen2", "Item Name: $itemName, Description: $description, Category: $category")


    val context = LocalContext.current
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // Quantity Input
        OutlinedTextField(
            value = quantity,
            onValueChange = { quantity = it },
            label = { Text("Quantity") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )


        Spacer(modifier = Modifier.height(16.dp))


        // Free Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = isFree,
                onCheckedChange = { isFree = it }
            )
            Text("Donate for free.")
        }


        Spacer(modifier = Modifier.height(16.dp))
        AnimatedVisibility(visible = !isFree) {
            // Estimated Price Input
            OutlinedTextField(
                value = estimatedPrice,
                onValueChange = { estimatedPrice = it },
                label = { Text("Estimated Price") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )


        }
        Spacer(modifier = Modifier.height(16.dp))
        // Delivery Time Window Inputs


        DatePickerDialog(label = "Pick Start Date", selectedDate = deliveryStartDate) {
            deliveryStartDate = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        TimePickerDialog(label = "Pick Start Time", selectedTime = deliveryStartTime) {
            deliveryStartTime = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        DatePickerDialog(label = "Pick End Date", selectedDate = deliveryEndDate) {
            deliveryEndDate = it
        }
        Spacer(modifier = Modifier.height(16.dp))
        TimePickerDialog(label = "Pick End Time", selectedTime = deliveryEndTime) {
            deliveryEndTime = it
        }
        Spacer(modifier = Modifier.height(16.dp))


        // Submit Button
        Button(
            onClick = {
                if (estimatedPrice.isEmpty() && !isFree) {
                    // Show error message
                    Toast.makeText(context, "Please enter the estimated price", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }
                if ((!estimatedPrice.isDigitsOnly()) && !isFree) {
                    // Show error message
                    Toast.makeText(context, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                // Handle form submission logic
                val startDateTime = parseDateTime(deliveryStartDate, deliveryStartTime)
                val endDateTime = parseDateTime(deliveryEndDate, deliveryEndTime)
                if (isFirstBeforeSecond(startDateTime, endDateTime)) {
                    // Show error message
                    Toast.makeText(context, "End time must be after start time", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }
                viewModel.leftOverItem.pickUpTimeWindow = Pair(startDateTime, endDateTime)
                if (!isFree)
                    viewModel.leftOverItem.estimatedPrice = estimatedPrice.toInt()
                else
                    viewModel.leftOverItem.estimatedPrice = 0


                val leftOverFood = hashMapOf(
                    "name" to itemName,
                    "description" to description,
                    "category" to category,
                    "quantity" to quantity,
                    "estimatedPrice" to estimatedPrice,
                    "isFree" to isFree,
                    "deliveryStartTime" to deliveryStartTime,
                    "deliveryEndTime" to deliveryEndTime,
                    "deliveryStartDate" to deliveryStartDate,
                    "deliveryEndDate" to deliveryEndDate
                )
                try {
                    db.collection("leftOverFood")
                        .add(leftOverFood)
                        .addOnSuccessListener { documentReference ->
                            Log.d("Added!@#$", "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w("Failure", "Error adding document", e)
                        }
                }catch (e:Exception){
                    Log.d("Error",e.toString())
                }


                navController.navigate("screen3")
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit")
        }
    }
}




@Composable
fun DatePickerDialog(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    var showDialog by remember { mutableStateOf(false) }


    OutlinedTextField(
        value = selectedDate,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = "Select Date")
            }
        }
    )


    if (showDialog) {
        val datePickerDialog = android.app.DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val selected = "$dayOfMonth/${month + 1}/$year"
                onDateSelected(selected)
                showDialog = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}


@Composable
fun TimePickerDialog(
    label: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()


    var showDialog by remember { mutableStateOf(false) }


    OutlinedTextField(
        value = selectedTime,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        trailingIcon = {
            IconButton(onClick = { showDialog = true }) {
                Icon(imageVector = Icons.Default.AccessTime, contentDescription = "Select Time")
            }
        }
    )


    if (showDialog) {
        val timePickerDialog = android.app.TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selected = String.format("%02d:%02d", hourOfDay, minute)
                onTimeSelected(selected)
                showDialog = false
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }
}


fun getCurrentDate(): String {
    val calendar = Calendar.getInstance()
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
    val month = calendar.get(Calendar.MONTH) + 1 // Adding 1 to get the correct month
    val year = calendar.get(Calendar.YEAR)
    return "$dayOfMonth/$month/$year"
}


fun getCurrentTime(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY) // 24-hour format
    val minute = calendar.get(Calendar.MINUTE)
    return "%02d:%02d".format(hour, minute)
}


fun parseDateTime(dateStr: String, timeStr: String): LocalDateTime {
    // Parse date
    val dateParts = dateStr.split("/")
    val dayOfMonth = dateParts[0].toInt()
    val month = dateParts[1].toInt()
    val year = dateParts[2].toInt()


    // Parse time
    val timeParts = timeStr.split(":")
    val hour = timeParts[0].toInt()
    val minute = timeParts[1].toInt()


    // Construct LocalDateTime object
    return LocalDateTime.of(year, month, dayOfMonth, hour, minute)
}


fun isFirstBeforeSecond(firstDateTime: LocalDateTime, secondDateTime: LocalDateTime): Boolean {
    return firstDateTime.isBefore(secondDateTime)
}



