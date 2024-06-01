package com.project.resqfood.domain.services

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

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
}