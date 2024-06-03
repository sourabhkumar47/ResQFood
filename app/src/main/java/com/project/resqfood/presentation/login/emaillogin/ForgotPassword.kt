package com.project.resqfood.presentation.login.emaillogin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.resqfood.presentation.login.mainlogin.CircleImage
import kotlinx.serialization.Serializable

@Serializable
object NavForgotPassword
/**
 * ForgotPassword is a composable function that provides the UI for the password reset process.
 * It includes a message indicating that a password reset link has been sent, and a button for going back to the sign-in screen.
 *
 * @param navController The NavController used for navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPassword(navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Reset Password") }, navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
                }) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
            })
        }
    ) {
        Column(modifier = Modifier
            .padding(it)
            .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
            CircleImage(size = 80, imageVector = Icons.Default.MarkEmailUnread)
            Spacer(modifier = Modifier.height(16.dp))
            Text("Password reset link has been sent on mail")
            Spacer(modifier = Modifier.height(16.dp))
            Text("Please check your mail")
            Spacer(modifier = Modifier.height(32.dp))
            TextButton(onClick = {
                navController.popBackStack()
            }) {
                Text(text = "Go back to Sign In")
            }
        }
    }
}
