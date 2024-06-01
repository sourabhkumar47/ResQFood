package com.project.resqfood.presentation.login

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import com.project.resqfood.R
import com.project.resqfood.model.UserEntity
import com.project.resqfood.presentation.MainActivity
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
object NavPersonalDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDetails(navigationAfterCompletion: () -> Unit = {},
                    onBackButtonClick: () -> Unit = {}){
    val coroutineScope  = rememberCoroutineScope()
    var userEntity by remember {
        mutableStateOf(MainActivity.userEntity ?: UserEntity(
            gender = "Select Gender"
        ))
    }
    var expanded by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf(userEntity.name) }
    var email by remember { mutableStateOf(userEntity.email) }
    var phoneNumber by remember { mutableStateOf(userEntity.phoneNumber) }
    var selectedGender by remember { mutableStateOf(userEntity.gender) }
    var houseNumber by remember { mutableStateOf(userEntity.houseNumber) }
    var streetName by remember { mutableStateOf(userEntity.street) }
    var locality by remember { mutableStateOf(userEntity.locality) }
    var city by remember { mutableStateOf(userEntity.city) }
    var state by remember { mutableStateOf(userEntity.state) }
    var pinCode by remember { mutableStateOf(userEntity.pincode) }
    var profilePictureUrl by remember { mutableStateOf(userEntity.profileUrl) }

    var isProfilePictureChanged = false
    val context = LocalContext.current
    var uri: Uri? = null
    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uriRef->
        if (uriRef != null) {
            uri = uriRef
            profilePictureUrl = uri.toString()
            isProfilePictureChanged = true
        }
    }
    val auth = FirebaseAuth.getInstance()
    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null && MainActivity.userEntity == null) {
                name = user.displayName ?: ""
                email = user.email ?: ""
                phoneNumber = user.phoneNumber ?: ""
                profilePictureUrl = user.photoUrl.toString()
            }
        }
        auth.addAuthStateListener(authStateListener)
        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
    @Composable
    fun TopAppBarComposable() {
        TopAppBar(title = {
            Text(text = "Personal Details")
        }, colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if(!isSystemInDarkTheme())MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            )

        )
    }

    @Composable
    fun ProfileImage() {
        Box(
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .clickable {
                        pickImageLauncher.launch("image/*")
                    }) {
                if (profilePictureUrl == "null" || profilePictureUrl.isEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.person),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                shape = CircleShape
                            )
                    )
                } else {
                    AsyncImage(
                        model = profilePictureUrl,
                        contentDescription = "profile",
                        imageLoader = ImageLoader(context),
                        placeholder = painterResource(id = R.drawable.user),
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                shape = CircleShape
                            )
                    )
                }
                Icon(
                    imageVector = Icons.Filled.Edit,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
    val onClickSave = {
        if(name.isEmpty())
            Toast.makeText(context,"Name cannot be empty", Toast.LENGTH_SHORT).show()
        else if(!isValidEmail(email))
            Toast.makeText(context,"Invalid Email", Toast.LENGTH_SHORT).show()
        else if(selectedGender == "Select Gender")
            Toast.makeText(context, "Select Gender", Toast.LENGTH_SHORT).show()
        else if(houseNumber.isEmpty())
            Toast.makeText(context, "House Number cannot be empty", Toast.LENGTH_SHORT).show()
        else if(city.isEmpty())
            Toast.makeText(context, "City cannot be empty", Toast.LENGTH_SHORT).show()
        else if(state.isEmpty())
            Toast.makeText(context, "State cannot be empty", Toast.LENGTH_SHORT).show()
        else if(pinCode.isEmpty())
            Toast.makeText(context, "Pin Code cannot be empty", Toast.LENGTH_SHORT).show()
        else if(phoneNumber.length == 10 && phoneNumber.isDigitsOnly())
        coroutineScope.launch {
            isLoading = true
            val userEntityUpdated = UserEntity(
                uid = auth.currentUser?.uid ?: "",
                email = email,
                name = name,
                phoneNumber = phoneNumber,
                gender = selectedGender,
                isEmailVerified = false,
                isPhoneVerified = false,
                profileUrl = profilePictureUrl,
                houseNumber = houseNumber,
                street = streetName,
                city = city,
                state = state,
                pincode = pinCode
            )
            saveDetails(userEntityUpdated,
                onSuccess = {
                    isLoading = false
                    Toast.makeText(context, "Details saved successfully", Toast.LENGTH_SHORT).show()
                    navigationAfterCompletion()
                    MainActivity.userEntity = userEntityUpdated
                },
                onFailure = {
                    Log.e("PersonalDetails", "Failed to save details", it)
                    Toast.makeText(context, "Failed to save details", Toast.LENGTH_SHORT).show()
                    isLoading = false
                })
        }
        else
            Toast.makeText(context, "Invalid Phone Number", Toast.LENGTH_SHORT).show()
    }

    @Composable
    fun GenderSelector() {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85F)
                    .height(56.dp)
                    .clickable {
                        expanded = true
                    }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurface,
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Row {
                    Text(text = selectedGender
                        , modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.CenterVertically))
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .align(Alignment.CenterVertically)
                    ) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "Select Gender",
                        modifier = Modifier.align(Alignment.CenterEnd))
                    }
                }
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(text = {
                    Text(text = "Male")
                }, onClick = {
                    selectedGender = "Male"
                    expanded = false
                })
                DropdownMenuItem(text = {
                    Text("Female")
                }, onClick = {
                    selectedGender = "Female"
                    expanded = false
                })
                DropdownMenuItem(text = {
                    Text("Other")
                }, onClick = {
                    selectedGender = "Other"
                    expanded = false
                })
            }
        }
    }
    @Composable
    fun SavingDialog(showDialog: Boolean, onDismiss: () -> Unit) {
        if (showDialog) {
            BasicAlertDialog(
                onDismissRequest = onDismiss,
                content = {
                    Layout(
                        content = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center

                            ) {
                                Column {

                                    CircularProgressIndicator()
                                    Text("Saving..")
                                }
                            }
                        },
                        measurePolicy = { measurables, constraints ->
                            val placeables = measurables.map {
                                it.measure(constraints)
                            }
                            layout(constraints.maxWidth, constraints.maxHeight) {
                                placeables.forEach {
                                    it.place(0, 0)
                                }
                            }
                        }
                    )
                },
                modifier = Modifier
                    .width(200.dp)
                    .height(100.dp) // Set a specific width and height for the dialogue box
            )
        }
    }
    Scaffold(
        topBar = {
            TopAppBarComposable()
        },
        containerColor = if(!isSystemInDarkTheme())MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
    ) { paddingValues ->
        if(isLoading){
            SavingDialog(showDialog = true, onDismiss = {})
        }
        Column(
            Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        , horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(50.dp))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Card(
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomEnd = 0.dp, bottomStart = 0.dp),
                        modifier = Modifier.width(400.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
                    ) {
                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(1f)
                        ) {
                            item{
                            Spacer(modifier = Modifier.height(120.dp))
                            OutlinedTextField(value = name,
                                onValueChange = { name = it },
                                label = { Text("Name*") },
                                modifier = Modifier.fillMaxWidth(0.85F))
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(value = email,
                                onValueChange = { email = it },
                                label = { Text("Email*") },
                                modifier = Modifier.fillMaxWidth(0.85F))
                            Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    modifier = Modifier.fillMaxWidth(0.85F),
                                    placeholder = { Text("Phone Number*")},
                                    prefix = {
                                        Row {
                                            Image(
                                                painterResource(id = R.drawable.india),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .height(24.dp)
                                                    .width(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(text = "+91")
                                            Spacer(modifier = Modifier.width(8.dp))
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                        }
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            GenderSelector()
                            Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = houseNumber,
                                    onValueChange = { houseNumber = it },
                                    modifier = Modifier.fillMaxWidth(0.85F),
                                    label = { Text("House/Flat/Building Number*") }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = streetName,
                                    onValueChange = { streetName = it },
                                    modifier = Modifier.fillMaxWidth(0.85F),
                                    label = { Text("Street Name") }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = locality,
                                    onValueChange = { locality = it },
                                    modifier = Modifier.fillMaxWidth(0.85F),
                                    label = { Text("Locality") }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = city,
                                    onValueChange = { city = it },
                                    modifier = Modifier.fillMaxWidth(0.85F),
                                    label = { Text("City*") }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = state,
                                    onValueChange = { state = it },
                                    modifier = Modifier.fillMaxWidth(0.85F),
                                    label = { Text("State*") }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = pinCode,
                                    onValueChange = { pinCode = it },
                                    label = { Text("Pin Code*") },
                                    modifier = Modifier.fillMaxWidth(0.85F),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                )
                                Spacer(modifier = Modifier.height(48.dp))
                            Button(onClick = {
                                onClickSave()
                            }, enabled = !isLoading,
                                modifier = Modifier.fillMaxWidth(0.85F)
                                ) {
                                Text("Save")
                            }
                                Spacer(modifier = Modifier.height(120.dp))
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.25f)
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            ProfileImage()
        }
    }
}

suspend fun saveDetails(userEntity: UserEntity, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    userEntity.apply {
        try {
            withContext(Dispatchers.IO) {
                val auth = FirebaseAuth.getInstance()
                val user = auth.currentUser

                val downloadUrl: String?
                Log.i("PersonalDetails", "profileURI: ${user?.photoUrl}")
                if (profileUrl != "null" && profileUrl.isNotEmpty() && profileUrl != user?.photoUrl.toString()) {
                    val storageRef =
                        Firebase.storage.reference.child("profile_pictures/${user?.uid}")
                    val uploadTask = storageRef.putFile(profileUrl.toUri())
                    val taskSnapshot = uploadTask.await()
                    downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl?.await().toString()

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(downloadUrl))
                        .build()
                    user?.updateProfile(profileUpdates)?.await()
                } else {
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .build()

                    user?.updateProfile(profileUpdates)?.await()
                }
                userEntity.profileUrl = user?.photoUrl.toString()
                // Save gender and phone number to your database
                val database = FirebaseFirestore.getInstance()
                val userRef = database.collection("users").document("${user?.uid}")
                userRef.set(userEntity).await()
            }

            withContext(Dispatchers.Main) {
                onSuccess()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                onFailure(e)
            }
        }
    }
}

fun isNewUser(isNew: ()-> Unit, isOld: ()-> Unit, navController: NavController){
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid
    if (uid != null) {
        val docRef = firestore.collection("users").document(uid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // The user's personal details exist
                    Log.d("Firestore", "DocumentSnapshot data: ${document.data}")
                    isOld()
                } else {
                    // The user's personal details do not exist
                    Log.d("Firestore", "No such document")
                    isNew()
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Firestore", "get failed with ", exception)
                isNew()
            }
    }
}

@Serializable
object NavWaitScreen

@Composable
fun WaitScreen(navController: NavController){
    val auth = FirebaseAuth.getInstance()
    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                isNewUser(
                    isNew = {
                        navController.navigate(NavPersonalDetails)
                    },
                    isOld = {
                        navController.popBackStack(navController.graph.startDestinationId, true)
                        navController.navigate(NavMainScreen)
                    },
                    navController = navController
                )
            }
        }
        auth.addAuthStateListener(authStateListener)
        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    Surface {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading your details..")
            }
        }
    }
}
