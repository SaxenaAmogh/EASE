package com.example.eventmanagement.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventmanagement.repository.SessionManager
import com.example.eventmanagement.ui.theme.latoFontFamily
import com.google.firebase.firestore.FirebaseFirestore

//fun addAttendeeToEvent(db: FirebaseFirestore, eventId: String, name: String, email: String, phone: String) {
//    val attendeeData = hashMapOf(
//        "name" to name,
//        "email" to email,
//        "phone" to phone
//    )
//
//    // Generate a unique attendee ID
//    val attendeeRef = db.collection("attendees").document(eventId).collection("attendeesList").document()
//
//    attendeeRef.set(attendeeData)
//        .addOnSuccessListener {
//            Log.d("Firestore", "Attendee added under event: $eventId with ID: ${attendeeRef.id}")
//        }
//        .addOnFailureListener { e ->
//            Log.e("Firestore", "Error adding attendee", e)
//        }
//}

fun registerUserForEvent(
    db: FirebaseFirestore,
    context: Context,
    userEmail: String,
    eventId: String
) {
    val userId = userEmail.replace(".", "_") // 🔥 Replace '.' to avoid Firestore key issues

    db.collection("users").document(userId)
        .update("registeredEvents.$eventId", true) // 🔥 Adds event ID under registeredEvents
        .addOnSuccessListener {
            Toast.makeText(context, "Registered for event successfully!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterPage(navController: NavController, id: String) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val db = FirebaseFirestore.getInstance()
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    name = sessionManager.getUserName() ?: "Unknown"
    email = sessionManager.getUserEmail() ?: "Unknown"
    phone = sessionManager.getUserPhone() ?: "Unknown"

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .padding()
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 0.035 * screenWidth,
                            end = 0.035 * screenWidth,
                            top = 0.035 * screenWidth,
                            bottom = 0.1 * screenWidth
                        )
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)),
                ) {
                    Text(
                        text = "Register Now$id",
                        fontFamily = latoFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.05 * screenWidth))
                    Text(
                        text = "Name",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = { Text("") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFe8b225),
                            focusedLabelColor = Color(0xFF000000),
                            focusedTextColor = Color(0xFF000000),
                            unfocusedContainerColor = Color(0xFFC2C2C2).copy(alpha = 0.2f)
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(0.025 * screenWidth))
                    Text(
                        text = "Email Id",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFe8b225),
                            focusedLabelColor = Color(0xFF000000),
                            focusedTextColor = Color(0xFF000000),
                            unfocusedContainerColor = Color(0xFFC2C2C2).copy(alpha = 0.2f)
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(0.025 * screenWidth))
                    Text(
                        text = "Phone",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = { Text("") },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFe8b225),
                            focusedLabelColor = Color(0xFF000000),
                            focusedTextColor = Color(0xFF000000),
                            unfocusedContainerColor = Color(0xFFC2C2C2).copy(alpha = 0.2f)
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(0.15 * screenWidth))
                    FloatingActionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(50)),
                        onClick = {
//                            addAttendeeToEvent(db, id, name, email, phone)
                            registerUserForEvent(db, context, email, id)
                            navController.navigate("home"){
                                popUpTo("home") {
                                    inclusive = true}
                            }
                        },
                        containerColor = Color(0xFF6298F0),
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                            color = Color.White,
                            text = "Register",
                            fontSize = 20.sp,
                            fontFamily = latoFontFamily
                        )
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun RegisterPagePreview() {
    RegisterPage(rememberNavController(),"")
}