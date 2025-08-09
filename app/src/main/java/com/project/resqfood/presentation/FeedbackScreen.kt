package com.project.resqfood.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class FeedbackEntry(val name: String = "", val email: String = "", val feedback: String = "")

@Composable
fun FeedbackScreen(userId: String) {
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var name by remember { mutableStateOf(TextFieldValue("")) }
    var feedback by remember { mutableStateOf(TextFieldValue("")) }
    var submittedFeedback by remember { mutableStateOf<FeedbackEntry?>(null) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        val doc = db.collection("users").document(userId).collection("feedback").document("entry").get().await()
        if (doc.exists()) {
            submittedFeedback = doc.toObject(FeedbackEntry::class.java)
        }
        loading = false
    }

    if (loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "We Value Your Feedback",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )

            AnimatedVisibility(visible = submittedFeedback != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("✔", color = Color.White, fontSize = 20.sp)
                        }

                        Text(
                            text = "Thank you! Feedback submitted successfully.",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(" ${submittedFeedback?.name}", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Text(" ${submittedFeedback?.email}", fontSize = 16.sp, color = Color.Gray)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = submittedFeedback?.feedback ?: "",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            if (submittedFeedback == null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = TextFieldValue(user?.email ?: ""),
                            onValueChange = {},
                            label = { Text("Email") },
                            singleLine = true,
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = feedback,
                            onValueChange = { feedback = it },
                            label = { Text("Your Feedback") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp)
                        )

                        Button(
                            onClick = {
                                val entry = FeedbackEntry(
                                    name = name.text,
                                    email = user?.email ?: "unknown",
                                    feedback = feedback.text
                                )
                                db.collection("users")
                                    .document(userId)
                                    .collection("feedback")
                                    .document("entry")
                                    .set(entry)
                                    .addOnSuccessListener {
                                        submittedFeedback = entry
                                    }
                            },
                            enabled = name.text.isNotBlank() && feedback.text.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            Text("Submit Feedback")
                        }
                    }
                }
            }
        }
    }
}
