package com.project.resqfood.domain.services

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.project.resqfood.presentation.login.mainlogin.getActivity

class AccountServiceImpl: AccountService {
    override fun authenticateWithEmail(
        auth: FirebaseAuth,
        email: String,
        password: String,
        onTask: (Task<AuthResult>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { onTask(it)
            }
    }

    override fun createAccountWithEmail(
        auth: FirebaseAuth,
        email: String,
        password: String,
        name: String,
        onTask: (Task<AuthResult>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener { onTask(it)
            }
    }

    override fun sendEmailVerificationLink(
        auth: FirebaseAuth,
        email: String,
        onTask: (Task<Void>) -> Unit
    ) {
        auth.currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { onTask(it)
            }
    }

    override fun updateUserProfile(auth: FirebaseAuth, name: String, onTask: (Task<Void>) -> Unit) {
        val profileUpdates = userProfileChangeRequest{
            displayName = name
        }
        auth.currentUser?.updateProfile(profileUpdates)
            ?.addOnCompleteListener {onTask(it)
            }
    }

    override fun sendPasswordResetEmail(
        auth: FirebaseAuth,
        email: String,
        onTask: (Task<Void>) -> Unit
    ) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { onTask(it)
            }

    }

    override fun signOut() {
        Firebase.auth.signOut()
    }

    override fun reauthenticateAfterEmailVerification(
        auth: FirebaseAuth,
        email: String,
        password: String,
        onTask: (Task<Void>) -> Unit
    ) {
        val user = auth.currentUser
        if(user == null){
            Log.e(TAG,"No user is logged in to authenticate")
            return
        }
        val credential = EmailAuthProvider
            .getCredential(email,password)

        user.reauthenticate(credential)
            .addOnCompleteListener {
                onTask(it)
            }
    }

    override fun googleSignIn(
        auth: FirebaseAuth,
        authCredential: AuthCredential,
        onTask: (Task<AuthResult>) -> Unit
    ) {
        auth.signInWithCredential(authCredential)
            .addOnCompleteListener { onTask(it)
            }
    }

    override fun sendCallback(onCodeTimeOut:() -> Unit,
                     onCodeSent:(verificationId:String,token:PhoneAuthProvider.ForceResendingToken)->Unit,
                        onVerificationCompleted:(credential:PhoneAuthCredential)->Unit,
                        onVerificationFailed:(e:FirebaseException)->Unit
                     )=object:PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
        override fun onCodeAutoRetrievalTimeOut(p0: String) {
            super.onCodeAutoRetrievalTimeOut(p0)
            onCodeTimeOut()
        }

        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            super.onCodeSent(p0, p1)
            onCodeSent(p0,p1)
        }

        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
            onVerificationCompleted(p0)
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            onVerificationFailed(p0)
        }

    }

    override fun sendOTP(context: Context, auth: FirebaseAuth, phoneNumber: String, timeOut: Long,
                callbacks: OnVerificationStateChangedCallbacks){
        val options = context.getActivity()?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeOut, java.util.concurrent.TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(callbacks)
                .build()
        }

        if(options != null){
            //This means try to verify automatically else send otp
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override fun reSendOTP(
        context: Context,
        auth: FirebaseAuth,
        phoneNumber: String,
        timeOut: Long,
        callbacks: OnVerificationStateChangedCallbacks,
        forceResendingToken: ForceResendingToken
    ) {
        val options = context.getActivity()?.let {
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(timeOut, java.util.concurrent.TimeUnit.SECONDS)
                    .setActivity(it)
                    .setForceResendingToken(forceResendingToken)
                    .setCallbacks(callbacks)
                    .build()
        }

        if(options != null){
            //This means try to verify automatically else send otp
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    override fun phoneNumberSignInWithCredential(auth: FirebaseAuth, credential: PhoneAuthCredential, onTask: (Task<AuthResult>) -> Unit){
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                task->
                onTask(task)
            }
    }

    override fun signInAnonymously(context: Context,auth: FirebaseAuth,onTask: (Task<AuthResult>) -> Unit){
        context.getActivity()?.let {
            auth.signInAnonymously()
                .addOnCompleteListener(it) { task ->
                    onTask(task)
                }
        }
    }

}