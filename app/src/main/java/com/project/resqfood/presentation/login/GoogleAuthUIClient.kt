package com.project.resqfood.presentation.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.project.resqfood.MainActivity
import com.project.resqfood.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await


/**
 * GoogleAuthUIClient is a class that handles Google sign-in operations.
 *
 * @param context The context used to access resources and services.
 * @param oneTapClient The SignInClient used to initiate the sign-in process.
 */
class GoogleAuthUIClient(
    private val context: Context,
    //This sign in client will show dialogue to sign In
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    /**
     * signInWithGoogle is a suspend function that initiates the Google sign-in process.
     * It returns an IntentSender that can be used to start the sign-in intent.
     *
     * @return An IntentSender for the sign-in intent, or null if an error occurred.
     */suspend fun signInWithGoogle(): IntentSender?{
        val result = try{
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch (e: Exception){
            e.printStackTrace()
            MainActivity.googleSignInError.value = true
            Log.e("GoogleAuthUIClient", "Error in signInWithGoogle: ${e.message}")
            Toast.makeText(context, "Some Error occurred", Toast.LENGTH_SHORT).show()
            if(e is CancellationException)
                throw e
            null
        }
        return result?.pendingIntent?.intentSender
        //When we send this intent, we get an intent back having information about user sign in
    }


    /**
     * signInWithIntent is a suspend function that handles the sign-in intent returned by the Google sign-in process.
     * It signs in the user with the Google sign-in credential obtained from the intent.
     *
     * @param intent The sign-in intent returned by the Google sign-in process.
     * @return A GoogleSignInResult containing the user's data if the sign-in was successful, or an error message otherwise.
     */
    suspend fun signInWithIntent(intent: Intent): GoogleSignInResult{
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            GoogleSignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        }
        catch (e:Exception){
            e.printStackTrace()
            MainActivity.googleSignInError.value = true
            if(e is CancellationException)
                throw e
            GoogleSignInResult(null,e.message?:"Unknown Error")
        }
    }


    /**
     * buildSignInRequest is a private function that builds a BeginSignInRequest for the Google sign-in process.
     *
     * @return A BeginSignInRequest for the Google sign-in process.
     */
    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("726557887682-ol2lmm1t0ce4p20l0gc010bhomqcrs7t.apps.googleusercontent.com")
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }
}