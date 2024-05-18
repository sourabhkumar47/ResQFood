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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAddAlt1
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.project.resqfood.presentation.Destinations
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun PersonalDetails(navigationAfterCompletion: () -> Unit = {},
                    onBackButtonClick: () -> Unit = {}){
    val coroutineScope  = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedGender by remember { mutableStateOf("Select Gender") }
    val context = LocalContext.current
    var name by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var phoneNumber by remember {
        mutableStateOf("")
    }
    var profilePictureUrl by remember {
        mutableStateOf("")
    }
    var uri: Uri? = null
    val pickImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {uriRef->
        if (uriRef != null) {
            uri = uriRef
            profilePictureUrl = uri.toString()
        }
    }
    val auth = FirebaseAuth.getInstance()
    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
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
//                Toast.makeText(context, "profileUrl: $profilePictureUrl", Toast.LENGTH_SHORT).show()
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
    val emailAuthentication = EmailAuthentication()
    val onClickSave = {
        if(name.isEmpty())
            Toast.makeText(context,"Name cannot be empty", Toast.LENGTH_SHORT).show()
        else if(!emailAuthentication.isValidEmail(email))
            Toast.makeText(context,"Invalid Email", Toast.LENGTH_SHORT).show()
        else if(selectedGender == "Select Gender")
            Toast.makeText(context, "Select Gender", Toast.LENGTH_SHORT).show()
        else if(phoneNumber.length == 10 && phoneNumber.isDigitsOnly())
        coroutineScope.launch {
            isLoading = true
            saveDetails(name, selectedGender, email, phoneNumber, uri,
                onSuccess = {
                    isLoading = false
                    Toast.makeText(context, "Details saved successfully", Toast.LENGTH_SHORT).show()
                    navigationAfterCompletion()
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
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text(selectedGender)
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
                            Spacer(modifier = Modifier.height(180.dp))
                            OutlinedTextField(value = name,
                                onValueChange = { name = it },
                                label = { Text("Name") })
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") })
                            Spacer(modifier = Modifier.height(16.dp))
                                OutlinedTextField(
                                    value = phoneNumber,
                                    onValueChange = { phoneNumber = it },
                                    placeholder = { Text("Enter Phone Number")},
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
                            Button(onClick = {
                                onClickSave()
                            }, enabled = !isLoading,
                                modifier = Modifier.fillMaxWidth(0.75f)
                                ) {
                                Text("Save")
                            }
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

suspend fun saveDetails(name: String, gender: String, email: String, phoneNumber: String,
                        photoURI: Uri? = null, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
    try {
        withContext(Dispatchers.IO) {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser

            val downloadUrl: String?
            Log.i("PersonalDetails", "profileURI: $photoURI")
            if(photoURI != null){
                val storageRef = Firebase.storage.reference.child("profile_pictures/${user?.uid}")
                val uploadTask = storageRef.putFile(photoURI)
                val taskSnapshot = uploadTask.await()
                downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl?.await().toString()

                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(downloadUrl))
                    .build()
                user?.updateProfile(profileUpdates)?.await()
            }
            else {
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()

                user?.updateProfile(profileUpdates)?.await()
            }
            val userEntity = UserEntity(
                uid = user?.uid ?: "",
                email = email,
                name = name,
                phoneNumber = phoneNumber,
                gender = gender,
                isEmailVerified = false,
                isPhoneVerified = false,
                profileUrl = user?.photoUrl.toString()
            )
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

@Composable
fun WaitScreen(navController: NavController){
    val auth = FirebaseAuth.getInstance()
    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                isNewUser(
                    isNew = {
                        navController.navigate(Destinations.PersonalDetails.route)
                    },
                    isOld = {
                        navController.navigate(Destinations.MainScreen.route)
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
            Column {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Please wait..")
            }
        }
    }
}
