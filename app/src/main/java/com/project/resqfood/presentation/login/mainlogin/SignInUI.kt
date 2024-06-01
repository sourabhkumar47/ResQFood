package com.project.resqfood.presentation.login.mainlogin

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.resqfood.R
import com.project.resqfood.presentation.login.NavWaitScreen
import com.project.resqfood.presentation.login.emaillogin.NavEmailSignIn
import com.project.resqfood.presentation.login.phoneNumberCheck
import kotlinx.serialization.Serializable

@Serializable
object NavSignInUI
/**
 * SignInUI is a composable function that provides the UI for the sign-in process.
 * It includes fields for entering phone number and OTP, and buttons for sending OTP, verifying OTP, and signing in with Google.
 * It also provides the UI for switching to sign-up mode.
 *
 * @param navController The NavController used for navigation.
 */
@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun SignInUI(
    navController: NavController,
    mainSignInViewModel: MainSignInViewModel
) {
   val uiState by mainSignInViewModel.uiState
    val context = LocalContext.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    if(!uiState.isPhoneNumberValid)
        phoneNumberCheck(uiState.phoneNumber, mainSignInViewModel::onPhoneNumberErrorStateChange)
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        paddingValues->
        Box(modifier = Modifier
            .fillMaxSize()
            .background(
                color = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            ) ){
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_without_background),
                    modifier = Modifier.padding(paddingValues),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.75f),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.background
                    ),
                ) {
                    if (uiState.isLoading)
                        Wait()
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(300.dp)
                        ) {

                            Text(
                                text = stringResource(id = R.string.LoginIntro),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(48.dp))
                            DividerWithText("Log in or sign up")
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedTextField(
                                value = if(uiState.phoneNumber.length > 3)uiState.phoneNumber.substring(3) else "",
                                onValueChange = mainSignInViewModel::onPhoneNumberChange,
                                placeholder = { Text("Enter Phone Number") },
                                modifier = Modifier.fillMaxWidth(),
                                isError = !uiState.isPhoneNumberValid,
                                supportingText = {
                                    if(!uiState.isPhoneNumberValid){
                                        Text(text = uiState.phoneNumberError)
                                    }
                                },
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
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if(phoneNumberCheck(phoneNumber = uiState.phoneNumber, mainSignInViewModel::onPhoneNumberErrorStateChange))
                                        return@Button
                                    mainSignInViewModel.sendOTPbutton(
                                        context,snackbarHostState,navController
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !uiState.isLoading
                            ) {
                                Text(text = "Send OTP")
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            DividerWithText(text = "or")
                            Spacer(modifier = Modifier.height(16.dp))
                            Row {
                                CircleImage(painterId = R.drawable.google, size = 40,
                                    onClick = {
                                        mainSignInViewModel.googleLogIn(context, snackbarHostState) {
                                            onSignInSuccessful(navController)
                                        }
                                    })

                                Spacer(modifier = Modifier.width(48.dp))
                                CircleImage(imageVector = Icons.Default.Email, size = 40,
                                    onClick = {
                                        if (!uiState.isLoading)
                                            navController.navigate(NavEmailSignIn)
                                    })

                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            DividerWithText("or just")
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(modifier = Modifier.fillMaxWidth(), onClick = {
                                mainSignInViewModel.loginAnonymously(snackbarHostState,context,navController)
                            }) {
                                Text("Login as guest")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DividerWithText(text: String = "Or"){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
                .height(1.dp),
            color = MaterialTheme.colorScheme.outline
        )
    }
}



/**
 * CircleImage is a composable function that displays an image in a circular shape.
 *
 * @param painterId The resource ID of the image to be displayed. This is optional and defaults to null.
 * @param imageVector The vector image to be displayed. This is optional and defaults to null.
 * @param size The size of the image in dp.
 * @param onClick A function to be invoked when the image is clicked. This is optional and defaults to an empty function.
 */
@Composable
fun CircleImage(
    painterId: Int? = null,
    imageVector: ImageVector? = null,
    size: Int,
    onClick: () -> Unit = {}
){
    Box(contentAlignment =
    Alignment.Center,modifier = Modifier
        .size(size.dp)
        .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
        .clickable(onClick = onClick)
    ) {
        if(painterId != null)
            Image(
                painter = painterResource(painterId),
                contentDescription = null,
                modifier = Modifier
                    .height((size - 8).dp)
                    .width((size - 8).dp)
                    .clip(CircleShape)
            )
        else if(imageVector != null){
            Image(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier
                    .height((size - 8).dp)
                    .width((size - 8).dp)
                    .clip(CircleShape),

                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
            )
        }
    }
}

fun Context.getActivity(): Activity ?= when(this){
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}


/**
 * Wait is a composable function that displays a linear progress indicator.
 */
@Composable
fun Wait(){
    LinearProgressIndicator(modifier = Modifier.fillMaxWidth()
        )
}

fun onSignInSuccessful(navController: NavController){
    navController.navigate(NavWaitScreen)
}

