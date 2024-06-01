package com.project.resqfood.presentation.login.emaillogin

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.project.resqfood.domain.services.AccountService
import com.project.resqfood.domain.services.AccountServiceImpl
import kotlinx.coroutines.launch

class EmailSignInViewModel(val auth: FirebaseAuth, val accountServiceImpl: AccountService) :
    ViewModel() {
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

    fun showSnackBar(snackbarHostState: SnackbarHostState, message: String) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    fun forgotPassword(snackbarHostState: SnackbarHostState, onSuccess: () -> Unit) {
        if (uiState.value.isLoading) return
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            AccountServiceImpl().sendPasswordResetEmail(auth, uiState.value.email) { task ->
                uiState.value = uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    onSuccess()
                    showSnackBar(snackbarHostState, "Password reset email sent")
                } else if (task.exception != null) {
                    showSnackBar(snackbarHostState, task.exception!!.message ?: "Failed to send email")
                } else {
                    showSnackBar(snackbarHostState, "Something went wrong")
                }
            }
        }
    }


    fun login(
        snackbarHostState: SnackbarHostState,
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
                if (task.isSuccessful) {
                    onSuccess()
                } else if (task.exception != null) {
                    showSnackBar(
                        snackbarHostState,
                        task.exception!!.message ?: "Failed to login"
                    )
                } else {
                    showSnackBar(
                        snackbarHostState,
                        "Failed to login. Something went wrong"
                    )
                }
            }
        }
    }

    private fun addNameInAccount(onSuccess: () -> Unit, snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            AccountServiceImpl().updateUserProfile(auth, uiState.value.name) { task ->
                uiState.value = uiState.value.copy(isLoading = false)
                if (task.isSuccessful) {
                    onSuccess()
                } else if (task.exception != null) {
                    showSnackBar(
                        snackbarHostState,
                        task.exception!!.message ?: "Failed to add name in account"
                    )
                    Log.e("CreateAccountViewModel", "Failed to add name in account", task.exception)
                } else {
                    showSnackBar(snackbarHostState, "Failed to add name in account")
                    Log.e("CreateAccountViewModel", "Failed to add name in account")
                }
            }
        }
    }

    fun createAccount(snackbarHostState: SnackbarHostState, onSuccess: () -> Unit) {
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
                    addNameInAccount(onSuccess, snackbarHostState)
                } else if (task.exception != null) {
                    uiState.value = uiState.value.copy(isLoading = false)
                    showSnackBar(
                        snackbarHostState,
                        task.exception!!.message ?: "Failed to create account"
                    )
                    Log.e("CreateAccountViewModel", "Failed to create account", task.exception)
                } else {
                    uiState.value = uiState.value.copy(isLoading = false)
                    showSnackBar(snackbarHostState, "Failed to create account")
                    Log.e("CreateAccountViewModel", "Failed to create account")

                }
            }
        }
    }
}

class EmailSignInViewModelFactory(val auth: FirebaseAuth, val accountServiceImpl: AccountService) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmailSignInViewModel::class.java))
            return EmailSignInViewModel(auth, accountServiceImpl) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}