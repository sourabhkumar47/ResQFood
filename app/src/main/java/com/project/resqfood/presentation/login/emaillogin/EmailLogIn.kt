package com.project.resqfood.presentation.login.emaillogin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.project.resqfood.R
import com.project.resqfood.presentation.login.confirmPasswordCheck
import com.project.resqfood.presentation.login.emailCheck
import com.project.resqfood.presentation.login.mainlogin.DividerWithText
import com.project.resqfood.presentation.login.mainlogin.Wait
import com.project.resqfood.presentation.login.mainlogin.onSignInSuccessful
import com.project.resqfood.presentation.login.nameCheck
import com.project.resqfood.presentation.login.passwordCheck
import kotlinx.serialization.Serializable

@Serializable
object NavEmailSignIn

/**
 * SignInUsingEmail is a composable function that provides the UI for the sign-in process using email.
 * It includes fields for entering email and password, and buttons for signing in and switching to sign-up mode.
 *
 * @param navController The NavController used for navigation.
 */
@Composable
fun SignInUsingEmail(viewModel: EmailSignInViewModel, navController: NavController) {
    val uiState by viewModel.uiState
    val context = LocalContext.current
    if(!uiState.isNameValid)
        nameCheck(uiState.name, viewModel::onNameErrorStateChange)
    if(!uiState.isPasswordValid)
        passwordCheck(uiState.password,viewModel::onPasswordErrorStateChange)
    if(!uiState.isConfirmPasswordValid)
        confirmPasswordCheck(uiState.password, uiState.confirmPassword, viewModel::onConfirmPasswordErrorStateChange)
    if(!uiState.isEmailValid)
        emailCheck(uiState.email, viewModel::onEmailErrorStateChange)
    val snackbarHostState = remember{SnackbarHostState()}
    @Composable
    fun SignIn() {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
        ) {
            item {
                Text(text = "Welcome Back!", style = MaterialTheme.typography.headlineLarge)
                Text(text = "Sign In", style = MaterialTheme.typography.headlineMedium)
            }
            item {
                Spacer(modifier = Modifier.height(32.dp))
                LoginTextField(
                    modifier = Modifier,
                    placeholderText = "Enter Email",
                    icon = Icons.Default.Email,
                    value = uiState.email,
                    isError = !uiState.isEmailValid,
                    isPassword = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    errorText = uiState.emailError
                ) {
                    viewModel.onEmailChange(it)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LoginTextField(
                    modifier = Modifier,
                    placeholderText = "Enter Password",
                    icon = Icons.Default.Key,
                    value = uiState.password,
                    isError = !uiState.isPasswordValid,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    errorText = uiState.passwordError
                ) {
                    viewModel.onPasswordChange(it)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    enabled = !uiState.isLoading, onClick = {
                        //Implement password and email validity here
                        if (emailCheck(uiState.email, viewModel::onEmailErrorStateChange)) {
                            return@Button
                        }
                        if(passwordCheck(uiState.password, viewModel::onPasswordErrorStateChange)) {
                            return@Button
                        }
                        viewModel.login(snackbarHostState) {
                            onSignInSuccessful(navController)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Sign In")
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
                TextButton(enabled = !uiState.isLoading,
                    onClick = {
                        if(emailCheck(uiState.email, viewModel::onEmailErrorStateChange)) {
                            return@TextButton
                        }
                        viewModel.forgotPassword(snackbarHostState) {
                            navController.navigate(NavForgotPassword)
                        }
                    }) {
                    Text("Forgot Password?")
                }
            }
            item {
                Spacer(Modifier.height(8.dp))
                DividerWithText("or")
                Spacer(Modifier.height(8.dp))
            }
            item {
                OutlinedButton(onClick = {
                    viewModel.toggleSignInMode()
                }) {
                    Text("Create New Account")
                }
            }
        }
    }

    @Composable
    fun SignUp() {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
                .width(300.dp)
        ) {
            item {
                Text(text = "New User?", style = MaterialTheme.typography.headlineLarge)
                Text(text = "Sign Up", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(32.dp))
            }
            item {
                LoginTextField(
                    modifier = Modifier,
                    placeholderText = "Enter your name",
                    icon = Icons.Default.Person,
                    value = uiState.name,
                    isError = !uiState.isNameValid,
                    isPassword = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    errorText =uiState.nameError
                ) {
                    viewModel.onNameChange(it)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                LoginTextField(
                    modifier = Modifier,
                    placeholderText = "Enter you email",
                    icon = Icons.Default.Email,
                    value = uiState.email,
                    isError = !uiState.isEmailValid,
                    isPassword = false,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    errorText = uiState.emailError
                ) {
                    viewModel.onEmailChange(it)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LoginTextField(
                    modifier = Modifier,
                    placeholderText = "Create password",
                    icon = Icons.Default.Key,
                    value = uiState.password,
                    isError = !uiState.isPasswordValid,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    errorText = uiState.passwordError
                ) {
                    viewModel.onPasswordChange(it)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                LoginTextField(
                    modifier = Modifier,
                    placeholderText = "Confirm password",
                    icon = Icons.Default.Key,
                    value = uiState.confirmPassword,
                    isError = !uiState.isConfirmPasswordValid,
                    isPassword = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    errorText = uiState.confirmPasswordError
                ) {
                    viewModel.onConfirmPasswordChange(it)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    if(nameCheck(uiState.name, viewModel::onNameErrorStateChange)) {
                        return@Button
                    }
                    if(emailCheck(uiState.email, viewModel::onEmailErrorStateChange)) {
                        return@Button
                    }
                    if(passwordCheck(uiState.password, viewModel::onPasswordErrorStateChange)) {
                        return@Button
                    }
                    if(confirmPasswordCheck(uiState.password, uiState.confirmPassword, viewModel::onConfirmPasswordErrorStateChange)) {
                        return@Button
                    }
                    viewModel.createAccount(snackbarHostState){
                        onSignInSuccessful(navController)
                    }
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "Sign Up")
                }
            }
        }

    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
        ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()

            .background(color = if (!isSystemInDarkTheme()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer),

            ) {
            Spacer(modifier = Modifier.height(32.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    modifier = Modifier.padding(paddingValues),
                    painter = painterResource(id = R.drawable.logo_without_background),
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
                    shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
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
                        if (viewModel.isSignInMode.value)
                            SignIn()
                        else
                            SignUp()
                    }
                }
            }
        }
    }
}


@Composable
fun LoginTextField(
    modifier: Modifier, placeholderText: String,
    icon: ImageVector,
    value: String, isError: Boolean,
    isPassword: Boolean, keyboardOptions: KeyboardOptions,
    errorText: String, onValueChange: (String) -> Unit,
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    OutlinedTextField(value = value, onValueChange = {
        onValueChange(it)
    }, placeholder = {
        Text(
            text = placeholderText,
        )
    },
        keyboardOptions = keyboardOptions,
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        isError = isError,
        supportingText = { if (isError) Text(text = errorText) },
        leadingIcon = {
            Icon(
                imageVector = icon, contentDescription = null,
                modifier = Modifier
                    .width(20.dp)
                    .height(16.dp)
            )


        },
        trailingIcon = {
            if (isPassword)
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector =
                           if (showPassword) Icons.Default.VisibilityOff
                            else
                                Icons.Default.Visibility
                        , contentDescription = null
                    )
                }
        },
        modifier = modifier.width(316.dp)
    )
}