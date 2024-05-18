package com.project.resqfood.presentation

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.project.resqfood.presentation.login.BottomNavigation.MainScreen
import com.project.resqfood.presentation.login.ForgotPassword
import com.project.resqfood.presentation.login.OTPVerificationUI
import com.project.resqfood.presentation.login.PersonalDetails
import com.project.resqfood.presentation.login.SignInUI
import com.project.resqfood.presentation.login.SignInUsingEmail
import com.project.resqfood.presentation.login.WaitScreen
import com.project.resqfood.presentation.login.isNewUser
import com.project.resqfood.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    companion object{
        var phoneNumber: String = ""
        var storedVerificationId = ""
        var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
        var countDownTime = MutableStateFlow(60000)
        var isResendButtonEnabled = MutableStateFlow(false)
    }
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val alreadyLoggedIn = FirebaseAuth.getInstance().currentUser != null
        enableEdgeToEdge()
        setContent {
            AppTheme {
                    val navController = rememberNavController()

                    NavHost(navController = navController,
                        startDestination = if(alreadyLoggedIn)
                    Destinations.MainScreen.route else Destinations.SignIn.route) {
                        composable(Destinations.SignIn.route){
                            SignInUI(navController = navController)
                        }
                        composable(Destinations.OtpVerification.route){
                            OTPVerificationUI(navController = navController)
                        }
                        composable(Destinations.EmailSignIn.route){
                            SignInUsingEmail(navController = navController)
                        }
                        composable(Destinations.ForgotPassword.route){
                            ForgotPassword(navController = navController)
                        }
                        composable(Destinations.MainScreen.route){
                            MainScreen(navController = navController)
                        }
                        composable(Destinations.PersonalDetails.route){
                            PersonalDetails(navigationAfterCompletion = {
                                navController.popBackStack(navController.graph.startDestinationId, true)
                                navController.navigate(Destinations.MainScreen.route)
                            })
                        }
                        composable(Destinations.WaitScreen.route){
                            WaitScreen(navController)
                        }
                    }
                }
            }
        }
}

