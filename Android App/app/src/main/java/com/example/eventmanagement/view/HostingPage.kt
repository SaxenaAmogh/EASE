package com.example.eventmanagement.view

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.eventmanagement.ui.theme.latoFontFamily
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getCurrentDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(Date())
}

fun generateGoogleCalendarLink(eventName: String, startDate: String, endDate: String, description: String, location: String): String {
    val baseUrl = "https://www.google.com/calendar/render?action=TEMPLATE"
    return "$baseUrl&text=${eventName.replace(" ", "+")}" +
            "&dates=$startDate/$endDate" +
            "&details=${description.replace(" ", "+")}" +
            "&location=${location.replace(" ", "+")}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HostingPage(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var eventName by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var locationLink by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var insta by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Hackathons", "Workshops", "Conference", "Meetup", "Fest")
    var selectedOption by remember { mutableStateOf(options[0]) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        imageUri = uri
    }

    val calendarLink = generateGoogleCalendarLink(eventName, startDate, endDate, desc, location)

    Column(
        modifier = Modifier
            .padding(bottom = 30.dp)
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .verticalScroll(rememberScrollState())
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
        ) {
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
                text = "Event Start Date",
                fontFamily = latoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(0.025 * screenWidth))
            OutlinedTextField(
                value = startDate,
                onValueChange = { startDate = it },
                placeholder = { Text("YYYYMMDDTHHmmSSZ") },
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
                text = "Event End Date",
                fontFamily = latoFontFamily,
                fontSize = 16.sp,
                fontWeight = FontWeight.W500
            )
            Spacer(modifier = Modifier.height(0.025 * screenWidth))
            OutlinedTextField(
                value = endDate,
                onValueChange = { endDate = it },
                placeholder = { Text("YYYYMMDDTHHmmSSZ") },
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

            Button(onClick = { imagePicker.launch("image/*") }) {
                Text("Pick an Image")
            }
        }

        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Image",
                modifier = Modifier.height(200.dp).fillMaxWidth()
            )
        }

        FloatingActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(50))
                .padding(
                start = 0.035 * screenWidth,
                end = 0.035 * screenWidth),
            onClick = {
                val newEvent = hashMapOf(
                    "name" to eventName,
                    "date" to startDate,
                    "location" to location,
                    "locationLink" to locationLink,
                    "desc" to desc,
                    "instagram" to insta,
                    "type" to selectedOption,
                    "calendar" to calendarLink,
                    "created" to getCurrentDate()
                )

                db.collection("events").add(newEvent)
                    .addOnSuccessListener { documentRef ->
                        val latestEventId = documentRef.id

                        imageUri?.let { uri ->
                            uploadImageToFirebase(uri, latestEventId) { url ->
                                storeImageUrlInFirestore(latestEventId, url)
                                imageUrl = url
                            }
                        }

                        Toast.makeText(context, "Event Hosted Successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate("home") { popUpTo("hosting") { inclusive = true } }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error Hosting Event", Toast.LENGTH_SHORT).show()
                        Log.e("Firestore", "Error adding event", e)
                    }
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

// Function to upload image to Firebase Storage
fun uploadImageToFirebase(imageUri: Uri, eventId: String, onSuccess: (String) -> Unit) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("event_images/$eventId.jpg")

    imageRef.putFile(imageUri)
        .addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
        .addOnFailureListener { e ->
            Log.e("Firebase", "Image Upload Failed", e)
        }
}

// Function to store image URL in Firestore
fun storeImageUrlInFirestore(eventId: String, imageUrl: String) {
    val db = FirebaseFirestore.getInstance()

    db.collection("events").document(eventId)
        .update("image", imageUrl)
        .addOnSuccessListener {
            Log.d("Firestore", "Image URL stored successfully")
        }
        .addOnFailureListener { e ->
            Log.e("Firestore", "Error storing image URL", e)
        }
}
