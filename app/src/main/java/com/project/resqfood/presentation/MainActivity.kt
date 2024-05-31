package com.project.resqfood.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.model.UserEntity
import com.project.resqfood.presentation.itemdetailscreen.AddingLeftovers
import com.project.resqfood.presentation.itemdetailscreen.ItemDetailScreen
import com.project.resqfood.presentation.itemdetailscreen.NavAddingLeftovers
import com.project.resqfood.presentation.itemdetailscreen.NavItemDetailScreen
import com.project.resqfood.presentation.itemdetailscreen.NavOrderConfirmScreen
import com.project.resqfood.presentation.itemdetailscreen.OrderConfirmScreen
import com.project.resqfood.presentation.login.BottomNavigation.MainScreen
import com.project.resqfood.presentation.login.BottomNavigation.NavMainScreen
import com.project.resqfood.presentation.login.ForgotPassword
import com.project.resqfood.presentation.login.NavEmailSignIn
import com.project.resqfood.presentation.login.NavForgotPassword
import com.project.resqfood.presentation.login.NavOTPVerificationUI
import com.project.resqfood.presentation.login.NavPersonalDetails
import com.project.resqfood.presentation.login.NavSignInUI
import com.project.resqfood.presentation.login.NavWaitScreen
import com.project.resqfood.presentation.login.OTPVerificationUI
import com.project.resqfood.presentation.login.PersonalDetails
import com.project.resqfood.presentation.login.SignInUI
import com.project.resqfood.presentation.login.SignInUsingEmail
import com.project.resqfood.presentation.login.SignInViewModel
import com.project.resqfood.presentation.login.WaitScreen
import com.project.resqfood.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    companion object {
        var userEntity: UserEntity? = null
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
                val viewModel: SignInViewModel = viewModel()
                if (alreadyLoggedIn)
                    viewModel.getUserData(FirebaseAuth.getInstance().currentUser!!.uid)
                NavHost(
                    navController = navController, 
                    startDestination = if (alreadyLoggedIn)
                        NavMainScreen else NavSignInUI
                ) {

                    composable<NavSignInUI> {
                        SignInUI(navController = navController)
                    }
                    composable<NavOTPVerificationUI> {
                        OTPVerificationUI(navController = navController)
                    }
                    composable<NavEmailSignIn> {
                        SignInUsingEmail(navController = navController)
                    }
                    composable<NavForgotPassword> {
                        ForgotPassword(navController = navController)
                    }
                    composable<NavMainScreen> {
                        MainScreen(navController = navController)
                    }
                    composable<NavPersonalDetails> {
                        PersonalDetails(navigationAfterCompletion = {
                            navController.popBackStack(navController.graph.startDestinationId, true)
                            navController.navigate(NavMainScreen)
                        })
                    }
                    composable<NavWaitScreen> {
                        WaitScreen(navController)
                    }
                    composable<NavItemDetailScreen> {
                        ItemDetailScreen(navController)
                    }
                    composable<NavOrderConfirmScreen> {
                        OrderConfirmScreen(navController)
                    }
                    composable<NavAddingLeftovers>{
                        AddingLeftovers(navController)
                    }
                }
            }
        }
    }
}