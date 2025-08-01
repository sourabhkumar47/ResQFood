package com.project.resqfood.presentation.address

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.project.resqfood.model.Address

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressScreen(userId: String, onAddClick: () -> Unit) {
    val db = FirebaseFirestore.getInstance()
    var addressList by remember { mutableStateOf<List<Address>>(emptyList()) }

    // Load addresses
    LaunchedEffect(userId) {
        db.collection("users")
            .document(userId)
            .collection("address")
            .get()
            .addOnSuccessListener { result ->
                val addresses = result.documents.mapNotNull { it.toObject(Address::class.java) }
                addressList = addresses
            }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Address")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Saved Addresses") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                if (addressList.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("No addresses found.", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Tap + to add a new address.", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(addressList) { address ->
                            ElevatedCard(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(8.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("City: ${address.city}", style = MaterialTheme.typography.titleMedium)
                                    Text("Building: ${address.building}", style = MaterialTheme.typography.bodyMedium)
                                    Text("State: ${address.state}", style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
