package com.project.resqfood.presentation.login

import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.project.resqfood.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhoneNumberSignIn {

    fun verifyPhoneNumberWithCode(auth: FirebaseAuth, context: Context, verificationId: String,
                                  code: String, onVerificationCompleted:() -> Unit,
                                  onInvalidOTP: () -> Unit = {},onVerificationFailed: () -> Unit){
        try {
            Log.d("PhoneSignIn", "verifyPhoneNumberWithCode: $verificationId and code is $code")
            val credential = PhoneAuthProvider.getCredential(verificationId,code)
            signInWithPhoneAuthCredential(auth, context, credential, onVerificationCompleted, onInvalidOTP, onVerificationFailed)
        }
        catch (e: Exception){
            Log.d("PhoneSignIn", "verifyPhoneNumberWithCode: ${e.message}")
            Toast.makeText(context, "Error,check Log", Toast.LENGTH_SHORT).show()
        }
    }

    fun onLoginClicked(auth: FirebaseAuth,
                       context: Context,
                       phoneNumber: String, viewModel: SignInViewModel, onCodeSent:() -> Unit
    ,onAutoVerify:() -> Unit, onInvalidRequest: () -> Unit,
                       onQuotaExceeded: () -> Unit,
                       onRecaptchaVerification: () -> Unit){
        auth.setLanguageCode("en")
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                viewModel.addVerificationIds(p0,p1)
                startCountdown()
                Log.d("PhoneSignIn", "onCodeSent: $p0")
                Log.d("InViewModel", "onCodeSent: ${MainActivity.storedVerificationId} ${MainActivity.forceResendingToken}")
                onCodeSent()
            }

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(auth,context, credential, onAutoVerify )
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                Log.d("PhoneSignIN", "onVerificationFailed: ${e.message}")
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                if (e is FirebaseAuthInvalidCredentialsException) {
                    onInvalidRequest()
                } else if (e is FirebaseTooManyRequestsException) {
                    onQuotaExceeded()
                } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                    onRecaptchaVerification()
                }else
                    Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                //Add functionality when code retrieval times out
                Toast.makeText(context, "Time Out, Try Resending", Toast.LENGTH_SHORT).show()
            }
        }
        val options = context.getActivity()?.let {
            PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(120L, java.util.concurrent.TimeUnit.SECONDS)
                .setActivity(it)
                .setCallbacks(callback)
                .build()
        }

        if(options != null){
            Log.d("phoneBook", options.toString())
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }

    private fun signInWithPhoneAuthCredential(auth: FirebaseAuth, context: Context, credential: PhoneAuthCredential,
                                              onVerificationCompleted: () -> Unit,
                                              onInvalidOTP: () -> Unit = {},
                                              onVerificationFailed: () -> Unit = {}){
        context.getActivity()?.let{
            auth.signInWithCredential(credential)
                .addOnCompleteListener(it){task->
                    if(task.isSuccessful){
                        //Sign In successful, update UI with signed-in user's information
                        val user = task.result?.user
                        Log.d("PhoneSignIn", "Success signInWithPhoneAuthCredential: user")
                        onVerificationCompleted()
                    }
                    else{
                        //Sign In failed, display message and update the UI
                        Log.d("PhoneSignIn", "signInWithPhoneAuthCredential: ${task.exception}")
                        if(task.exception is FirebaseAuthInvalidCredentialsException){
                            Toast.makeText(context, "Invalid OTP", Toast.LENGTH_SHORT).show()
                            onInvalidOTP()
                        }
                        Toast.makeText(context, "Sign In Failed", Toast.LENGTH_SHORT).show()
                        onVerificationFailed()
                    }
                }
        }
    }


    fun resendVerificationCode(auth: FirebaseAuth, context: Context, phoneNumber: String,viewModel: SignInViewModel
    ,onVerificationCompleted: () -> Unit, onRecaptchaVerification: () -> Unit, onQuotaExceeded: () -> Unit, onInvalidRequest: () -> Unit,
                               onCodeSent: () -> Unit){
        val callback = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)
                viewModel.addVerificationIds(p0,p1)
                onCodeSent()
                Log.d("PhoneSignIn", "onCodeSent: $p0")
                MainActivity.isFinishEnabled.value = false
                startCountdown()
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                Toast.makeText(context, "Verification Completed", Toast.LENGTH_SHORT).show()
                signInWithPhoneAuthCredential(auth,context, p0, onVerificationCompleted)

            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                Log.d("PhoneSignIN", "onVerificationFailed: ${p0.message}")
                if (p0 is FirebaseAuthInvalidCredentialsException) {
                    onInvalidRequest()
                } else if (p0 is FirebaseTooManyRequestsException) {
                    onQuotaExceeded()
                } else if (p0 is FirebaseAuthMissingActivityForRecaptchaException) {
                    // reCAPTCHA verification attempted with null Activity
                    onRecaptchaVerification()
                }
            }

            override fun onCodeAutoRetrievalTimeOut(p0: String) {
                super.onCodeAutoRetrievalTimeOut(p0)
                //Add functionality when code retrieval times out if user is not authenticated
                if(auth.currentUser == null)
                Toast.makeText(context, "Time Out, Try Resending", Toast.LENGTH_SHORT).show()
            }
        }
        val options = context.getActivity()?.let {
            MainActivity.forceResendingToken?.let { it1 ->
                PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(120L, java.util.concurrent.TimeUnit.SECONDS)
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

    fun startCountdown(){
        object : CountDownTimer(60000,1000){
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