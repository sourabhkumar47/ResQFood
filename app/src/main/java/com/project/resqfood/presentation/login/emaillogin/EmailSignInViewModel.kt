package com.project.resqfood.presentation.login.emaillogin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.project.resqfood.domain.services.AccountService
import com.project.resqfood.domain.services.AccountServiceImpl
import kotlinx.coroutines.launch

class EmailSignInViewModel(val auth:FirebaseAuth, val accountServiceImpl: AccountService): ViewModel() {
    var uiState = mutableStateOf(EmailUIState())
    private set

    var isSignInMode = mutableStateOf(true)
        private set

    fun toggleSignInMode() {
        isSignInMode.value = !isSignInMode.value
    }

    fun onEmailChange(newEmail: String) {
        uiState.value = uiState.value.copy(email = newEmail)
    }

    fun onConfirmPasswordChange(newConfirmPassword: String) {
        uiState.value = uiState.value.copy(confirmPassword = newConfirmPassword)
    }

    fun onEmailErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value = uiState.value.copy(isEmailValid = !isError, emailError = errorText)
    }

    fun onPasswordErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value = uiState.value.copy(isPasswordValid = !isError, passwordError = errorText)
    }

    fun onConfirmPasswordErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value =
            uiState.value.copy(isConfirmPasswordValid = !isError, confirmPasswordError = errorText)
    }

    fun onPasswordChange(newPassword: String) {
        uiState.value = uiState.value.copy(password = newPassword)
    }

    fun onNameChange(newName: String) {
        uiState.value = uiState.value.copy(name = newName)
    }

    fun onNameErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value = uiState.value.copy(isNameValid = !isError, nameError = errorText)
    }

    fun forgotPassword(context: Context, navController: NavController, onSuccess: () -> Unit){
        if (uiState.value.isLoading) return
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            AccountServiceImpl().sendPasswordResetEmail(auth, uiState.value.email) { task ->
                uiState.value = uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    onSuccess()
                    Toast.makeText(context, "Password reset email sent", Toast.LENGTH_SHORT).show()
                } else if (task.exception != null) {
                    Toast.makeText(context, task.exception!!.message ?: "Failed to send email", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    fun login(
        context: Context,
        navController: NavController,
        onSuccess: () -> Unit
    ) {
        if (uiState.value.isLoading) return
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            accountServiceImpl.authenticateWithEmail(
                auth,
                uiState.value.email,
                uiState.value.password
            ) { task ->
                uiState.value = uiState.value.copy(isLoading = false)
                if (task.isSuccessful){
                    onSuccess()
                } else if (task.exception != null) {
                    Toast.makeText(context, task.exception!!.message ?: "Failed to login", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun addNameInAccount(onSuccess: () -> Unit, context: Context) {
        viewModelScope.launch {
            AccountServiceImpl().updateUserProfile(auth, uiState.value.name) { task ->
                uiState.value = uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    onSuccess()
                } else if (task.exception != null) {
                    Toast.makeText(
                        context,
                        task.exception!!.message ?: "Failed to add name in account",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("CreateAccountViewModel", "Failed to add name in account", task.exception)
                } else {
                    Toast.makeText(context, "Failed to add name in account", Toast.LENGTH_SHORT).show()
                    Log.e("CreateAccountViewModel", "Failed to add name in account")
                }
            }
        }
    }

    fun createAccount(context: Context, onSuccess: () -> Unit) {
        if (uiState.value.isLoading)
            return
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            accountServiceImpl.createAccountWithEmail(
                auth,
                uiState.value.email,
                uiState.value.password,
                uiState.value.name
            ) { task ->
                if (task.isSuccessful) {
                    addNameInAccount(onSuccess, context)
                } else if (task.exception != null) {
                    uiState.value = uiState.value.copy(isLoading = false)
                    Toast.makeText(
                        context,
                        task.exception!!.message ?: "Failed to create account",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("CreateAccountViewModel", "Failed to create account", task.exception)
                } else {
                    uiState.value = uiState.value.copy(isLoading = false)
                    Toast.makeText(context, "Failed to create account", Toast.LENGTH_SHORT).show()
                    Log.e("CreateAccountViewModel", "Failed to create account")

                }
            }
        }
    }
}

class EmailSignInViewModelFactory(val auth: FirebaseAuth, val accountServiceImpl: AccountService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailSignInViewModel::class.java))
            return EmailSignInViewModel(auth, accountServiceImpl) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}