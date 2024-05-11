package com.project.resqfood.presentation.login

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhoneNumberSignIn {

    fun verifyPhoneNumberWithCode(auth: FirebaseAuth, context: Context, verificationId: String, code: String){
        try {
            Log.d("PhoneSignIn", "verifyPhoneNumberWithCode: $verificationId and code is $code")
            val credential = PhoneAuthProvider.getCredential(verificationId,code)
            signInWithPhoneAuthCredential(auth, context, credential)
        }
        catch (e: Exception){
            Log.d("PhoneSignIn", "verifyPhoneNumberWithCode: ${e.message}")
            Toast.makeText(context, "Error,check Log", Toast.LENGTH_SHORT).show()
        }
    }

    fun onLoginClicked(auth: FirebaseAuth, context: Context,
                       phoneNumber: String, viewModel: SignInViewModel, onCodeSent:() -> Unit){
        auth.setLanguageCode("en")
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                viewModel.addVerificationIds(p0,p1)
                startCountdown(viewModel)
                Log.d("PhoneSignIn", "onCodeSent: $p0")
                Log.d("InViewModel", "onCodeSent: ${MainActivity.storedVerificationId} ${MainActivity.forceResendingToken}")
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

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                //Add functionality when code retrieval times out
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


    fun resendVerificationCode(auth: FirebaseAuth, context: Context, phoneNumber: String,viewModel: SignInViewModel){
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                viewModel.addVerificationIds(p0,p1)
                Log.d("PhoneSignIn", "onCodeSent: $p0")
                MainActivity.isFinishEnabled.value = false
                startCountdown(viewModel)
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(auth,context, p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                Log.d("PhoneSignIN", "onVerificationFailed: ${p0.message}")
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                //Add functionality when code retrieval times out
            }
        }
        val options = context.getActivity()?.let {
            MainActivity.forceResendingToken?.let { it1 ->
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
                    .setActivity(it)
                    .setCallbacks(callback)
                    .setForceResendingToken(it1)
                    .build()
            }
        }

        if(options != null){
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    fun startCountdown(viewModel: SignInViewModel){
        object : CountDownTimer(6000,1000){
            override fun onTick(millisUntilFinished: Long){
                MainActivity.countDownTime.value = (millisUntilFinished/1000).toInt()
            }
            override fun onFinish(){
                CoroutineScope(Dispatchers.Main).launch(){
                MainActivity.isFinishEnabled.value = true
                Log.d("PhoneSignIn", "onFinish: ${MainActivity.isFinishEnabled}")
                }
            }
        }.start()
    }

}