package com.project.resqfood.domain.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.project.resqfood.model.RestaurantEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RestaurantDataRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getRestaurantData(restaurantId: String): RestaurantEntity? {
        return withContext(Dispatchers.IO) {
            try {
                db.collection("restaurants")
                    .document(restaurantId)
                    .get()
                    .await()?.toObject(RestaurantEntity::class.java)
            } catch (e: Exception) {
                Log.e("RestaurantDataRepository", "Error getting restaurant data", e)
                null
            }
        }
    }

    suspend fun setRestaurantData(restaurant: RestaurantEntity): Boolean {
        val restaurantSave = restaurant.copy(restaurantId = FirebaseAuth.getInstance().currentUser?.uid ?: "")
        return withContext(Dispatchers.IO) {
            try {
                db.collection("restaurants")
                    .document(restaurantSave.restaurantId)
                    .set(restaurantSave)
                    .await()
                true
            } catch (e: Exception) {
                Log.e("RestaurantDataRepository", "Error setting restaurant data", e)
                false
            }
        }
    }
}