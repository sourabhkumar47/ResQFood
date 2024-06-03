package com.project.resqfood.domain.services

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

interface AccountService {
    fun authenticateWithEmail(auth: FirebaseAuth = FirebaseAuth.getInstance(), email: String, password: String, onTask: (Task<AuthResult>) ->Unit)
    fun createAccountWithEmail(auth: FirebaseAuth = FirebaseAuth.getInstance(), email: String, password: String, name: String, onTask: (Task<AuthResult>) -> Unit)
    fun sendEmailVerificationLink(auth: FirebaseAuth = FirebaseAuth.getInstance(), email: String, onTask: (Task<Void>) ->Unit)
    fun sendPasswordResetEmail(auth: FirebaseAuth = FirebaseAuth.getInstance(), email: String, onTask: (Task<Void>) ->Unit)
    fun signOut()

    fun updateUserProfile(auth: FirebaseAuth = FirebaseAuth.getInstance(), name: String, onTask: (Task<Void>) ->Unit)
    fun reauthenticateAfterEmailVerification(
        auth: FirebaseAuth,
        email: String,
        password: String,
        onTask: (Task<Void>) -> Unit
    )
    fun googleSignIn(auth: FirebaseAuth, authCredential: AuthCredential, onTask: (Task<AuthResult>) -> Unit)
    fun sendCallback(
        onCodeTimeOut: () -> Unit,
        onCodeSent: (verificationId: String, token: PhoneAuthProvider.ForceResendingToken) -> Unit,
        onVerificationCompleted: (credential: PhoneAuthCredential) -> Unit,
        onVerificationFailed: (e: FirebaseException) -> Unit
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks

    fun sendOTP(
        context: Context,
        auth: FirebaseAuth,
        phoneNumber: String,
        timeOut: Long,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    )

    fun phoneNumberSignInWithCredential(
        auth: FirebaseAuth,
        credential: PhoneAuthCredential,
        onTask: (Task<AuthResult>) -> Unit
    )

    fun reSendOTP(
        context: Context,
        auth: FirebaseAuth,
        phoneNumber: String,
        timeOut: Long,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        forceResendingToken: PhoneAuthProvider.ForceResendingToken
    )

    fun signInAnonymously(context: Context, auth: FirebaseAuth, onTask: (Task<AuthResult>) -> Unit)
}