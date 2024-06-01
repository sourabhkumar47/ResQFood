package com.project.resqfood.presentation.login


import android.content.ContentValues.TAG
import android.credentials.GetCredentialException
import androidx.credentials.CredentialManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.resqfood.R
import com.project.resqfood.presentation.login.mainlogin.CircleImage
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.UUID


class GoogleAuthUIClient{
    fun generateRandomNonce(): String {
        val ranNonce = UUID.randomUUID().toString()

        val bytes = ranNonce.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        val hashedNonce = digest.fold("") { str, it -> str + "%02x".format(it) }
        return hashedNonce
    }
}


@RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
@Composable
fun GoogleSignInButton(onClickUI: () -> Unit, onFailureUI: () -> Unit, onSuccess: () -> Unit){
    val context = LocalContext.current
    val googleClinet = GoogleAuthUIClient()
    val coroutineScope = rememberCoroutineScope()

    val onClick:() -> Unit = {
        val credentialManager = CredentialManager.create(context)
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(context.getString(R.string.default_web_client_id))
            .setNonce(googleClinet.generateRandomNonce())
            .build()

        val request: GetCredentialRequest = GetCredentialRequest.Builder()
            .addCredentialOption(/* credentialOption = */ googleIdOption)
            .build()

        coroutineScope.launch {
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
                FirebaseAuth.getInstance().signInWithCredential(authCredential)

                Log.i(TAG, googleIdToken)
                onSuccess()
                Toast.makeText(context, "SignInSuccessful $googleIdToken", Toast.LENGTH_SHORT)
                    .show()

            }catch (e: GetCredentialException){
                onFailureUI()
                Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            }catch (e: GoogleIdTokenParsingException) {
                onFailureUI()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            catch (e: Exception) {
                onFailureUI()
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}