package com.project.resqfood.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Preview (showBackground = true)
@Composable
fun PersonalDetails() {
    val context = LocalContext.current
    val user = FirebaseAuth.getInstance().currentUser
    if(user == null){
        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
        //Proceed to sign in screen
        return
    }
    var name by remember {
        mutableStateOf("")
    }
    val email by remember {
        mutableStateOf("")
    }
    val phoneNumber by remember {
        mutableStateOf("")
    }
    val profilePictureUrl by remember {
        mutableStateOf("")
    }
    @Composable
    fun TopAppBarComposable(){
        TopAppBar(title = {
            Text(text = "Personal Details")
        }
        , navigationIcon = {
            //Back Button
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button")
            })
    }
    @Composable
    fun ProfileImage(){

    }
    Scaffold(
        topBar = {
            TopAppBarComposable()
        }
    ) { paddingValues ->
        LazyColumn(Modifier.padding(paddingValues)) {
            item {
                //Profile Picture
                ProfileImage()
            }
            item {
                OutlinedTextField(value = name?:"", onValueChange = {name = it}, label = {Text("Name")})
            }
            item {
                OutlinedTextField(value = email?:"", onValueChange = {}, label = {Text("Email")})
            }
            item {
                OutlinedTextField(value = phoneNumber?:"", onValueChange = {}, label = {Text("Phone Number")})
            }
        }
    }
}