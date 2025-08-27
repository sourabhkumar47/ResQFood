package com.project.resqfood.presentation


import NavOrderingPickup
import OrderingPickup
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute

import com.google.firebase.auth.FirebaseAuth
import com.project.resqfood.R
import com.project.resqfood.domain.services.AccountService
import com.project.resqfood.domain.services.AccountServiceImpl
import com.project.resqfood.model.UserEntity
import com.project.resqfood.presentation.address.AddAddressScreen
import com.project.resqfood.presentation.address.AddressScreen
import com.project.resqfood.presentation.communityScreen.CommunityScreen
import com.project.resqfood.presentation.communityScreen.NavCommunityScreen
import com.project.resqfood.presentation.itemdetailscreen.AddingLeftovers
import com.project.resqfood.presentation.itemdetailscreen.ItemDetailScreen
import com.project.resqfood.presentation.itemdetailscreen.NavAddingLeftovers
import com.project.resqfood.presentation.itemdetailscreen.NavItemDetailScreen
import com.project.resqfood.presentation.itemdetailscreen.NavOrderConfirmScreen
import com.project.resqfood.presentation.itemdetailscreen.OrderConfirmScreen
import com.project.resqfood.presentation.itemdetailscreen.SuccessScreen
import com.project.resqfood.presentation.login.NavPersonalDetails
import com.project.resqfood.presentation.login.NavWaitScreen
import com.project.resqfood.presentation.login.PersonalDetails
import com.project.resqfood.presentation.login.Screens.MainScreen
import com.project.resqfood.presentation.login.Screens.NavMainScreen
import com.project.resqfood.presentation.login.Screens.NavRoleSelectScreen
import com.project.resqfood.presentation.login.Screens.ResqFoodUserSelection
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
import com.project.resqfood.presentation.login.mainlogin.NavOTPVerificationUI
import com.project.resqfood.presentation.login.mainlogin.NavSignInUI
import com.project.resqfood.presentation.login.mainlogin.OTPVerificationUI
import com.project.resqfood.presentation.login.mainlogin.SignInUI
import com.project.resqfood.presentation.onboardingProcess.NavOnboarding
import com.project.resqfood.presentation.onboardingProcess.Onboarding
import com.project.resqfood.presentation.postScreen.NavPostScreen
import com.project.resqfood.presentation.postScreen.PostScreen
import com.project.resqfood.presentation.restaurantonboarding.ListingRestaurantScreen
import com.project.resqfood.presentation.restaurantonboarding.ListingViewModel
import com.project.resqfood.presentation.restaurantonboarding.ListingViewModelFactory
import com.project.resqfood.presentation.restaurantonboarding.NavListingRestaurant
import com.project.resqfood.ui.theme.AppTheme
//import com.project.resqfood.presentation.login.Screens.ShoppingCartScreen
import com.project.resqfood.presentation.restaurantDashboard.NavRestaurantDashboardScreen
import com.project.resqfood.presentation.restaurantDashboard.RestaurantDashboardScreen
import com.project.resqfood.presentation.restaurantDashboard.RestaurantViewModel
import com.project.resqfood.presentation.searchFilter.FilterSortBottomSheetUI
import com.project.resqfood.presentation.searchFilter.FilterSearchViewModel
import com.project.resqfood.presentation.searchFilter.NavSearchScreen
import com.project.resqfood.presentation.searchFilter.SearchScreen


class MainActivity : ComponentActivity() {


    companion object {
        var userEntity: UserEntity? = null
        var isUserAnonymous = mutableStateOf(false)
        var isDarkModeEnabled = mutableStateOf(false) //darkthemeoffhere
        const val NavAddressScreen = "address"
        const val NavAddAddressScreen = "add_address"

    }


    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val auth = FirebaseAuth.getInstance()
        val accountService: AccountService = AccountServiceImpl()
        val alreadyLoggedIn = FirebaseAuth.getInstance().currentUser != null
        val restaurantListingViewModel: ListingViewModel = ListingViewModelFactory().create(ListingViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            AppTheme (useDarkTheme = isDarkModeEnabled.value){
                val navController = rememberNavController()
                val viewModel: SignInDataViewModel = viewModel()
                val mainSignInViewModel: MainSignInViewModel =
                    viewModel(factory = MainSignInViewModelFactory(auth, accountService))
                if (alreadyLoggedIn)
                    viewModel.getUserData(FirebaseAuth.getInstance().currentUser!!.uid)
                NavHost(
                    navController = navController,
                    startDestination =
                    if (alreadyLoggedIn)
                        NavMainScreen  else NavOnboarding
                ) {
                    composable<NavSignInUI>(
                        enterTransition = {
                            slideHorizontallyAnimation()
                        },
                    ) {
                        SignInUI(navController = navController, mainSignInViewModel)
                    }
                    composable<NavSearchScreen>(
                        enterTransition = {
                            slideHorizontallyAnimation()
                        },
                    ){
                        val filterViewModel : FilterSearchViewModel = viewModel()
                        SearchScreen(filterViewModel)
                    }
                    composable<NavOTPVerificationUI>(
                        enterTransition = {
                            slideHorizontallyAnimation()
                        },
                    ) {
                        OTPVerificationUI(
                            navController = navController,
                            mainSignInViewModel = mainSignInViewModel
                        )
                    }
                    composable<NavRestaurantDashboardScreen> {
                        val restaurantViewModel: RestaurantViewModel = viewModel()
                        RestaurantDashboardScreen(
                            navController = navController,
                            restaurantViewModel = restaurantViewModel
                        )
                    }
                    composable<NavEmailSignIn>(
                        enterTransition = {
                            slideHorizontallyAnimation()
                        },
                    ) {
                        val emailViewModel: EmailSignInViewModel =
                            viewModel(factory = EmailSignInViewModelFactory(auth, accountService))
                        SignInUsingEmail(emailViewModel, navController = navController)
                    }
                    composable<NavForgotPassword>(
                        enterTransition = {
                            slideHorizontallyAnimation()
                        },
                    ) {
                        ForgotPassword(navController = navController)
                    }
                    composable<NavMainScreen>(
                        enterTransition = {
                            slideHorizontallyAnimation()
                        },
                    ) {
                        MainScreen(navController = navController)
                    }
                    composable<NavPersonalDetails>(
                        enterTransition = {
                            slideHorizontallyAnimation()
                        },
                    ) {
                        PersonalDetails(navigationAfterCompletion = {
                            navController.popBackStack(navController.graph.startDestinationId, true)
                            navController.navigate(NavRoleSelectScreen)
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
                    composable<NavAddingLeftovers> {
                        AddingLeftovers(navController)
                    }
                    composable<NavOrderingPickup> {
                        OrderingPickup(navController)
                    }
                    composable<SuccessScreen> {
                        SuccessScreen(
                            buttonText = "Continue",
                            onClick = {
                                navController.navigate(NavMainScreen) {
                                    popUpTo(NavMainScreen) {
                                        inclusive = false
                                    }
                                }
                            },
                            animationJsonResId = R.raw.successanimation
                        )
                    }
                    composable<NavCommunityScreen> {
                        CommunityScreen(navigateBackToHome = {
                            navController.navigate(NavMainScreen) {
                                popUpTo(NavMainScreen) {
                                    inclusive = false
                                }
                            }
                        }) {
                            navController.navigate(NavPostScreen)
                        }
                    }
                    composable<NavPostScreen> {
                        PostScreen {
                            navController.popBackStack()
                        }


                        composable<NavOnboarding> {
                            Onboarding(navController = navController)
                        }
                    }
                    composable<NavOnboarding>{
                        Onboarding(navController = navController)
                    }
                    composable<NavRoleSelectScreen>{
                        ResqFoodUserSelection(navController = navController)
                    }
            /*        composable<NavListingRestaurant> {
                        ListingRestaurantScreen(restaurantListingViewModel, navController)
                    }*/

                    composable<NavListingRestaurant> { backStackEntry ->
                        val args = backStackEntry.toRoute<NavListingRestaurant>()
                        ListingRestaurantScreen(
                            restaurantListingViewModel,
                            navController,
                            args.entryPoint // pass it down
                        )
                    }



                    //added the shopping cart screen
                    composable<ShoppingCartScreen>(
                        enterTransition = { slideHorizontallyAnimation() }
                    ) {
                        ShoppingCartScreen(
                            onBackClick = { navController.popBackStack() },
                            padding = PaddingValues()
                        )
                    }
                    composable("address_screen") {
                        AddressScreen(
                            userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                            onAddClick = { navController.navigate("add_address_screen")
                            }
                        )
                    }


                    composable("add_address_screen") {
                        AddAddressScreen(
                            userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                            onSaveSuccess = {
                                navController.popBackStack() // go back to AddressScreen
                            }
                        )
                    }
                    //forfedback
                    composable("feedback/{userId}") { backStackEntry ->
                        val userId = backStackEntry.arguments?.getString("userId") ?: ""
                        FeedbackScreen(userId = userId)
                    }





                }
            }
        }
    }


    private fun slideHorizontallyAnimation(): EnterTransition {
        return slideInHorizontally(animationSpec = tween(300),
            initialOffsetX = { fullWidth -> fullWidth }
        )
    }
}

