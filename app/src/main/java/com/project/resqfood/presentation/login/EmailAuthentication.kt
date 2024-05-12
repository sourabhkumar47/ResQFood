package com.project.resqfood.presentation.login

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class EmailAuthentication {
    fun signUpWithEmail(email: String, password: String, context: Context){
        val auth = FirebaseAuth.getInstance()
        context.getActivity()?.let {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }
        }
    }

    fun signInWithEmail(email: String, password: String, context: Context){
        val auth = FirebaseAuth.getInstance()
        context.getActivity()?.let {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            context,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                        updateUI(null)
                    }
                }
        }
    }

    fun forgotPassword(email: String,
                       onSuccessfullySend: () -> Unit,onFailed: () -> Unit){
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccessfullySend()
                    Log.d(TAG, "Email sent.")
                }
            }
            .addOnFailureListener {e ->
                onFailed()
                Log.w(TAG, "sendPasswordResetEmail:failure", e)
            }

    }

    fun validatePassword(password: String): String? {
        if (password.length < 8) {
            return "Password should be at least 8 characters long"
        }
        if (!password.any { it.isUpperCase() }) {
            return "Password should contain at least one uppercase letter"
        }
        if (!password.any { it.isLowerCase() }) {
            return "Password should contain at least one lowercase letter"
        }
        if (!password.any { it.isDigit() }) {
            return "Password should contain at least one digit"
        }
        if (!password.contains(Regex("[@#$%^&+=]"))) {
            return "Password should contain at least one special character (@, #, $, %, ^, &, +, =)"
        }
        return null
    }

    fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }
    fun updateUI(user: FirebaseUser?) {

    }
}