package com.project.resqfood.presentation.login

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.project.resqfood.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class GoogleAuthUIClient(
    private val context: Context,
    //This sign in client will show dialogue to sign In
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth

    //Suspend can take time that is why we use suspend function so that we can use it in a coroutine
    //This function returns the Intent Sender which will be used to show the dialogue to sign in
    suspend fun signInWithGoogle(): IntentSender?{
        val result = try{
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        }catch (e: Exception){
            e.printStackTrace()
            if(e is CancellationException)
                throw e
            null
        }
        return result?.pendingIntent?.intentSender
        //When we send this intent, we get an intent back having information about user sign in
    }


    //This function will be called when we get the intent back from the signInWithGoogle function
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
            if(e is CancellationException)
                throw e
            GoogleSignInResult(null,e.message?:"Unknown Error")
        }
    }

    suspend fun signOut(){
        try {
            auth.signOut()
        }
        catch (e:Exception){
            e.printStackTrace()
            if(e is CancellationException)
                throw e
        }
    }

    fun getSignedInUser() = auth.currentUser?.run{
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest{
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.default_web_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()
    }
}