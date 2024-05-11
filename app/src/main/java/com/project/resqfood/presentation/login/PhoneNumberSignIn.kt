package com.project.resqfood.presentation.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

class PhoneNumberSignIn {

    fun verifyPhoneNumberWithCode(auth: FirebaseAuth, context: Context, verificationId: String, code: String){
        val credential = PhoneAuthProvider.getCredential(verificationId,code)
        signInWithPhoneAuthCredential(auth, context, credential)
    }

    fun onLoginClicked(auth: FirebaseAuth, context: Context,
                       phoneNumber: String, viewModel: SignInViewModel, onCodeSent:() -> Unit){
        auth.setLanguageCode("en")
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)//TODO("Just check if there is any effect of this")
                viewModel.storedVerificationId = p0
                onCodeSent()
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(auth,context, credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                Log.d("PhoneSignIN", "onVerificationFailed: ${p0.message}")
            }
        }
        val options = context.getActivity()?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(callback)
                .build()
        }

        if(options != null){
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithPhoneAuthCredential(auth: FirebaseAuth, context: Context, credential: PhoneAuthCredential){
        context.getActivity()?.let{
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it){task->
                    if(task.isSuccessful){
                        //Sign In successful, update UI with signed-in user's information
                        val user = task.result?.user
                        Log.d("PhoneSignIn", "Success signInWithPhoneAuthCredential: user")
                    }
                    else{
                        //Sign In failed, display message and update the UI
                        Log.d("PhoneSignIn", "signInWithPhoneAuthCredential: ${task.exception}")
                        if(task.exception is FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                        }
                    }

                    //Update UI
                }
        }
    }



}