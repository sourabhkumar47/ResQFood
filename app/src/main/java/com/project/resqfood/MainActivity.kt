package com.project.resqfood

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.presentation.login.Destinations
import com.project.resqfood.presentation.login.GoogleAuthUIClient
import com.project.resqfood.presentation.login.OTPVerificationUI
import com.project.resqfood.presentation.login.SignInUI
import com.project.resqfood.presentation.login.SignInViewModel
import com.project.resqfood.ui.theme.ResQFoodTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUIClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    companion object{
        var phoneNumber: String = ""
        var storedVerificationId = ""
        var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
        var countDownTime = MutableStateFlow(6000)
        var isFinishEnabled = MutableStateFlow(false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResQFoodTheme {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Destinations.SignIn.route) {

                        composable(Destinations.SignIn.route){
                            val viewModel = viewModel<SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            //First we need to check if the user is already signed in
                            LaunchedEffect(Unit) {
                                if(googleAuthUIClient.getSignedInUser()!= null){
                                    //Implement what to do if user is already signed in
                                    //That is proceed to the dashboard
                                }
                            }

                            //Declaring launcher for google sign In
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult= {
                                    if(it.resultCode == RESULT_OK){
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUIClient.signInWithIntent(
                                                intent = it.data ?: return@launch
                                            )
                                            viewModel.onGoogleSignInResult(signInResult)
                                        }
                                    }
                                }
                            )
                            //Handling the sign in success state
                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if(state.isSignInSuccessful){
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign In Successful",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    //Implement what to do after sign in is successful
                                    //TODO("Navigate to relevant composable depending on the user type")

                                    viewModel.resetSate()
                                }
                            }
                            //Declaring the launcher for google sign In
                            SignInUI(state = state,navController = navController,
                                onGoogleSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntent = googleAuthUIClient.signInWithGoogle()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntent?: return@launch
                                            ).build()
                                        )
                                    }
                                })
                        }
                        composable(Destinations.OtpVerification.route){
                            OTPVerificationUI(navController = navController)
                        }

                    }
                }
            }
        }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ResQFoodTheme {
        Greeting("Android")
    }
}