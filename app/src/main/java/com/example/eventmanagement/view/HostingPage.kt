package com.example.eventmanagement.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventmanagement.model.Event
import com.example.eventmanagement.ui.theme.latoFontFamily
import com.example.eventmanagement.viewmodel.EventViewModel
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

fun addAttendeeForLatestEvent(db: FirebaseFirestore) {
    db.collection("events")
        .orderBy("created", Query.Direction.DESCENDING) // Order by timestamp
        .limit(1)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val latestEvent = documents.documents[0]
                val latestEventId = latestEvent.id

                // 🔥 Update the event with its own ID field
                db.collection("events").document(latestEventId)
                    .update("id", latestEventId)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Event ID added to event document: $latestEventId")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error updating event with ID", e)
                    }

                // ✅ Add attendee under `attendees/{eventID}/{attendeeID}`
                val attendeeData = hashMapOf(
                    "name" to "INIT",
                    "email" to "INIT",
                    "phone" to "INIT"
                )

                // Generate a unique attendee ID
                val attendeeRef = db.collection("attendees")
                    .document(latestEventId)
                    .collection("attendeesList")
                    .document()

                attendeeRef.set(attendeeData)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Attendee added under event ID: $latestEventId with Attendee ID: ${attendeeRef.id}")
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Error adding attendee", e)
                    }
            } else {
                Log.e("Firestore", "No events found")
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error fetching latest event", e)
        }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ViewModelConstructorInComposable")
@Composable
fun HostingPage(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val viewModel = EventViewModel()
    var eventName by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var locationLink by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }
    var insta by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Hackathons", "Workshops", "Conference", "Meetup", "Fest")
    var selectedOption by remember { mutableStateOf(options[0]) }

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
//                            bottom = 0.1 * screenWidth
                        )
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)),
                ){
                    Text(
                        text = "Create New Event",
                        fontFamily = latoFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.05 * screenWidth))
                    Text(
                        text = "Event Name",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = eventName,
                        onValueChange = { eventName = it },
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
                        text = "Event Description",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        placeholder = { Text("") },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFe8b225),
                            focusedLabelColor = Color(0xFF000000),
                            focusedTextColor = Color(0xFF000000),
                            unfocusedContainerColor = Color(0xFFC2C2C2).copy(alpha = 0.2f)
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = "Event Location",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = location,
                        onValueChange = { location = it },
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
                        text = "Event Location Link",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = locationLink,
                        onValueChange = { locationLink = it },
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
                        text = "Event Date",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.025 * screenWidth))
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        placeholder = { Text("DD-MM-YYYY") },
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
                        text = "Instagram Link",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))
                    OutlinedTextField(
                        value = insta,
                        onValueChange = { insta = it },
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
                        text = "Event Type",
                        fontFamily = latoFontFamily,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.01 * screenWidth))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = selectedOption,
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                label = { Text("Select an Item") },
                                trailingIcon = {
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "Dropdown"
                                    )
                                },
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFe8b225),
                                    focusedLabelColor = Color(0xFF000000),
                                    focusedTextColor = Color(0xFF000000),
                                    unfocusedContainerColor = Color(0xFFC2C2C2).copy(alpha = 0.2f)
                                ),
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                options.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            selectedOption = item
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(0.05 * screenWidth))
                    FloatingActionButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(50)),
                        onClick = {
                            val newEvent = Event(
                                name = eventName,
                                date = date,
                                location = location,
                                locationLink = locationLink,
                                desc = desc,
                                instagram = insta,
                                type = selectedOption,
                                calendar = "",
                                created = getCurrentDate()
                            )
                            viewModel.addNewEvent(newEvent)
                            addAttendeeForLatestEvent(db)
                            navController.navigate("home")
                        },
                        containerColor = Color(0xFF6298F0),
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                            color = Color.White,
                            text = "Host",
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
fun HostingPagePreview() {
    HostingPage(rememberNavController())
}