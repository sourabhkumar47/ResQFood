package com.project.resqfood.presentation.login.mainlogin

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.resqfood.R
import com.project.resqfood.domain.services.AccountService
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID

class MainSignInViewModel(val auth: FirebaseAuth, val accountService: AccountService): ViewModel() {
    var uiState = mutableStateOf(MainSignInUIState())
    private set

    fun setIsLoading(isLoading: Boolean) {
        uiState.value = uiState.value.copy(isLoading = isLoading)
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        uiState.value = uiState.value.copy(phoneNumber = newPhoneNumber)
    }

    fun onPhoneNumberErrorStateChange(isError: Boolean, errorText: String) {
        uiState.value = uiState.value.copy(isPhoneNumberValid = !isError, phoneNumberError = errorText)
    }

    fun googleLogIn(context: Context, onSuccess: () -> Unit){
        uiState.value = uiState.value.copy(isLoading = true)
        val credentialManager = CredentialManager.create(context)
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setNonce(generateRandomNonce())
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(/* credentialOption = */ googleIdOption)
            .build()

        viewModelScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential

                val googleIdTokenCredential = GoogleIdTokenCredential
                    .createFrom(credential.data)

                val googleIdToken = googleIdTokenCredential.idToken

                val authCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
                accountService.googleSignIn(auth, authCredential) { task ->
                    uiState.value = uiState.value.copy(isLoading = false)
                    if (task.isSuccessful) {
                        onSuccess()
                    } else if (task.exception != null) {
                        Toast.makeText(context, task.exception!!.message ?: "Failed to login", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to login", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            catch (e: Exception){
                uiState.value = uiState.value.copy(isLoading = false)
                Toast.makeText(context, e.message ?: "Failed to login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun generateRandomNonce(): String {
        val ranNonce = UUID.randomUUID().toString()

        val bytes = ranNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
        return hashedNonce
    }

}

class MainSignInViewModelFactory(val auth:FirebaseAuth, val accountService: AccountService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainSignInViewModel::class.java))
            return MainSignInViewModel(auth, accountService) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}