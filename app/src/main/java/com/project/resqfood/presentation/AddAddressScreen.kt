package com.project.resqfood.presentation.address

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.project.resqfood.model.Address

@Composable
fun AddAddressScreen(
    userId: String,
    onSaveSuccess: () -> Unit // this will be called after saving to navigate back
) {
    val db = FirebaseFirestore.getInstance()

    var city by remember { mutableStateOf("") }
    var building by remember { mutableStateOf("") }
    var state by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Enter Address", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = building,
            onValueChange = { building = it },
            label = { Text("Building") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = state,
            onValueChange = { state = it },
            label = { Text("State") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (city.isNotBlank() && building.isNotBlank() && state.isNotBlank()) {
                    isSaving = true
                    val address = Address(city = city, state = state, building = building)

                    db.collection("users")
                        .document(userId)
                        .collection("address")
                        .add(address)
                        .addOnSuccessListener {
                            isSaving = false
                            onSaveSuccess() // <-- navigate back after saving
                        }
                        .addOnFailureListener {
                            isSaving = false
                            // Handle error if needed
                        }
                }
            },
            enabled = !isSaving,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isSaving) "Saving..." else "Save Address")
        }
    }
}
