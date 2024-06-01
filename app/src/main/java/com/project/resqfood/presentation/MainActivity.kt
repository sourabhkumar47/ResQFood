package com.project.resqfood.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.domain.services.AccountService
import com.project.resqfood.domain.services.AccountServiceImpl
import com.project.resqfood.model.UserEntity
import com.project.resqfood.presentation.itemdetailscreen.AddingLeftovers
import com.project.resqfood.presentation.itemdetailscreen.ItemDetailScreen
import com.project.resqfood.presentation.itemdetailscreen.NavAddingLeftovers
import com.project.resqfood.presentation.itemdetailscreen.NavItemDetailScreen
import com.project.resqfood.presentation.itemdetailscreen.NavOrderConfirmScreen
import com.project.resqfood.presentation.itemdetailscreen.OrderConfirmScreen
import com.project.resqfood.presentation.login.Screens.MainScreen
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import com.project.resqfood.presentation.login.mainlogin.NavOTPVerificationUI
import com.project.resqfood.presentation.login.NavPersonalDetails
import com.project.resqfood.presentation.login.mainlogin.NavSignInUI
import com.project.resqfood.presentation.login.NavWaitScreen
import com.project.resqfood.presentation.login.mainlogin.OTPVerificationUI
import com.project.resqfood.presentation.login.PersonalDetails
import com.project.resqfood.presentation.login.mainlogin.SignInUI
import com.project.resqfood.presentation.login.SignInDataViewModel
import com.project.resqfood.presentation.login.WaitScreen
import com.project.resqfood.presentation.login.emaillogin.EmailSignInViewModel
import com.project.resqfood.presentation.login.emaillogin.EmailSignInViewModelFactory
import com.project.resqfood.presentation.login.emaillogin.ForgotPassword
import com.project.resqfood.presentation.login.emaillogin.NavEmailSignIn
import com.project.resqfood.presentation.login.emaillogin.NavForgotPassword
import com.project.resqfood.presentation.login.emaillogin.SignInUsingEmail
import com.project.resqfood.presentation.login.mainlogin.MainSignInViewModel
import com.project.resqfood.presentation.login.mainlogin.MainSignInViewModelFactory
import com.project.resqfood.presentation.onboardingProcess.NavOnboarding
import com.project.resqfood.presentation.onboardingProcess.Onboarding
import com.project.resqfood.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    companion object {
        var userEntity: UserEntity? = null
        var isUserAnonymous = mutableStateOf(false)
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()
        val accountService: AccountService = AccountServiceImpl()
        val alreadyLoggedIn = FirebaseAuth.getInstance().currentUser != null
        enableEdgeToEdge()
        setContent {
            AppTheme {
                val navController = rememberNavController()
                val viewModel: SignInDataViewModel = viewModel()
                val mainSignInViewModel:MainSignInViewModel = viewModel(factory = MainSignInViewModelFactory(auth, accountService))
                if (alreadyLoggedIn)
                    viewModel.getUserData(FirebaseAuth.getInstance().currentUser!!.uid)
                NavHost(
                    navController = navController, 
                    startDestination = if (alreadyLoggedIn)
                        NavMainScreen else NavOnboarding
                ) {
                    composable<NavSignInUI>(
                        enterTransition = {
                            slideInHorizontally(animationSpec = tween(300),
                                initialOffsetX = { fullWidth->fullWidth }
                            )
                        },
                    ) {
                         SignInUI(navController = navController, mainSignInViewModel)
                    }
                    composable<NavOTPVerificationUI>(
                        enterTransition = {
                            slideInHorizontally(animationSpec = tween(300),
                                initialOffsetX = { fullWidth->fullWidth }
                            )
                        },
                    ) {
                        OTPVerificationUI(navController = navController, mainSignInViewModel = mainSignInViewModel)
                    }
                    composable<NavEmailSignIn>(
                        enterTransition = {
                            slideInHorizontally(animationSpec = tween(300),
                                initialOffsetX = { fullWidth->fullWidth }
                            )
                        },
                    ) {
                        val emailViewModel: EmailSignInViewModel = viewModel(factory = EmailSignInViewModelFactory(auth, accountService))
                        SignInUsingEmail(emailViewModel,navController = navController)
                    }
                    composable<NavForgotPassword>(
                        enterTransition = {
                            slideInHorizontally(animationSpec = tween(300),
                                initialOffsetX = { fullWidth->fullWidth }
                            )
                        },
                    ) {
                        ForgotPassword(navController = navController)
                    }
                    composable<NavMainScreen>(
                        enterTransition = {
                            slideInHorizontally(animationSpec = tween(300),
                                initialOffsetX = { fullWidth->fullWidth }
                            )
                        },
                    ) {
                        MainScreen(navController = navController)
                    }
                    composable<NavPersonalDetails>(
                        enterTransition = {
                            slideInHorizontally(animationSpec = tween(300),
                                initialOffsetX = { fullWidth->fullWidth }
                            )
                        },
                    ) {
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
                    composable<NavOnboarding>{
                        Onboarding(navController = navController)
                    }
                }
            }
        }
    }
}