package com.example.eventmanagement.view

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.eventmanagement.repository.SessionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun UserHackathonPage(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserEmail()
    val db = FirebaseFirestore.getInstance()

    var registeredEvents by remember { mutableStateOf<List<String>>(emptyList()) }
    var organizedEvents by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(userId) {
        userId?.let {
            fetchUserHackathons(db, it) { registered, organized ->
                registeredEvents = registered
                organizedEvents = organized
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Your Hackathons", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(10.dp))

        if (registeredEvents.isEmpty() && organizedEvents.isEmpty()) {
            Text(text = "No hackathons found")
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                if (registeredEvents.isNotEmpty()) {
                    item { SectionTitle("Registered Hackathons") }
                    items(registeredEvents) { eventName ->
                        EventCard(eventName)
                    }
                }
                if (organizedEvents.isNotEmpty()) {
                    item { SectionTitle("Organized Hackathons") }
                    items(organizedEvents) { eventName ->
                        EventCard(eventName)
                    }
                }
            }
        }
    }
}

// 🔥 Function to Fetch Registered & Organized Hackathons
fun fetchUserHackathons(
    db: FirebaseFirestore,
    userId: String,
    onResult: (List<String>, List<String>) -> Unit
) {
    db.collection("users").document(userId)
        .get()
        .addOnSuccessListener { document ->
            if (document.exists()) {
                val registeredEvents = (document["registeredEvents"] as? Map<String, Boolean>)?.keys?.toList() ?: emptyList()
                val organizedEvents = (document["organizedEvents"] as? Map<String, Boolean>)?.keys?.toList() ?: emptyList()

                Log.d("Firestore", "Registered: $registeredEvents, Organized: $organizedEvents")

                fetchEventNames(db, registeredEvents) { regNames ->
                    fetchEventNames(db, organizedEvents) { orgNames ->
                        onResult(regNames, orgNames)
                    }
                }
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error fetching user events", e)
        }
}

// 🔥 Function to Fetch Event Names Using Event IDs
fun fetchEventNames(db: FirebaseFirestore, eventIds: List<String>, onResult: (List<String>) -> Unit) {
    if (eventIds.isEmpty()) {
        onResult(emptyList())
        return
    }

    db.collection("events")
        .whereIn("id", eventIds)
        .get()
        .addOnSuccessListener { documents ->
            val eventNames = documents.mapNotNull { it.getString("name") }
            onResult(eventNames)
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error fetching event names", e)
        }
}

// 🔥 Composable for Section Titles
@Composable
fun SectionTitle(title: String) {
    Text(text = title, style = MaterialTheme.typography.headlineSmall, modifier = Modifier.padding(vertical = 8.dp))
}

// 🔥 Composable for Displaying Event Names
@Composable
fun EventCard(eventName: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(modifier = Modifier.padding(16.dp)) {
            Text(text = eventName, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
