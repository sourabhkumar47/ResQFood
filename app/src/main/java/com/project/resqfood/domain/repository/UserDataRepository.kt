package com.project.resqfood.domain.repository

import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.project.resqfood.model.UserEntity
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getUserData(uid: String): UserEntity? {
        return withContext(Dispatchers.IO) {
            try {
                db.collection("users")
                    .document(uid)
                    .get()
                    .await()?.toObject(UserEntity::class.java)
            } catch (e: Exception) {
                Log.e("UserDataRepository", "Error getting user data", e)
                null
            }
        }
    }

    suspend fun setUserData(user: UserEntity): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                db.collection("users")
                    .document(user.uid)
                    .set(user)
                    .await()
                true
            } catch (e: Exception) {
                Log.e("UserDataRepository", "Error setting user data", e)
                false
            }
        }
    }
}