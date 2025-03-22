package com.example.eventmanagement.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.eventmanagement.R
import com.example.eventmanagement.repository.SessionManager
import com.example.eventmanagement.ui.theme.latoFontFamily
import com.google.firebase.auth.FirebaseAuth

fun signIn(email: String, password: String, context: Context, onResult: (Boolean, String?) -> Unit) {
    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                SessionManager(context).saveLoginState(true)  // Save login state
                onResult(true, null)
            } else {
                onResult(false, task.exception?.message)
            }
        }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun LoginPage(navController: NavController) {

    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    Scaffold(
        content = {
            Column(
                modifier = Modifier
                    .padding()
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            start = 0.035 * screenWidth,
                            end = 0.035 * screenWidth,
                            top = 0.035 * screenWidth,
                            bottom = 0.1 * screenWidth
                        )
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)),
                ){
                    Column(
                        modifier = Modifier.align(Alignment.TopStart),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFFC2C2C2).copy(alpha = 0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = "Login",
                                modifier = Modifier
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 5.dp
                                    ),
                                fontSize = 20.sp,
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.W500
                            )
                        }
                        Spacer(modifier = Modifier.height(0.025 * screenHeight))
                        Text(
                            text = "Let's login",
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 28.sp,
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Welcome back, login now to continue",
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 18.sp,
                            color = Color(0xFFC2C2C2),
                            fontFamily = latoFontFamily,
                            fontWeight = FontWeight.Normal
                        )
                        Spacer(modifier = Modifier.height(0.03 * screenHeight))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                        ){
                            Text(
                                text = "Name",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp),
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = name,
                                onValueChange = { name = it },
                                placeholder = { Text("abc@xyz.com") },
                                singleLine = true,
                                shape = RoundedCornerShape(20.dp),
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
                            Spacer(modifier = Modifier.height(0.025 * screenHeight))
                            Text(
                                text = "Phone",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp),
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = phone,
                                onValueChange = { phone = it },
                                placeholder = { Text("abc@xyz.com") },
                                singleLine = true,
                                shape = RoundedCornerShape(20.dp),
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
                            Spacer(modifier = Modifier.height(0.025 * screenHeight))
                            Text(
                                text = "Email Id",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp),
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = { Text("abc@xyz.com") },
                                singleLine = true,
                                shape = RoundedCornerShape(20.dp),
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
                            Spacer(modifier = Modifier.height(0.025 * screenHeight))
                            Text(
                                text = "Password",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 5.dp),
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                placeholder = { Text("8+ Characters") },
                                singleLine = true,
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFFe8b225),
                                    focusedLabelColor = Color(0xFF000000),
                                    focusedTextColor = Color(0xFF000000),
                                    unfocusedContainerColor = Color(0xFFC2C2C2).copy(alpha = 0.2f)
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = if (passwordVisible) KeyboardType.Text else KeyboardType.Password,
                                    imeAction = ImeAction.Done
                                ),
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.Warning else Icons.Default.Done,
                                            contentDescription = if (passwordVisible) "Hide password" else "Show password"
                                        )
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(0.007 * screenHeight))
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ){
                        FloatingActionButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(shape = RoundedCornerShape(50)),
                            onClick = {
                                signIn(email, password, navController.context) { success, message ->
                                    if (success) {
                                        val sessionManager = SessionManager(context)
                                        sessionManager.saveUserSession(name, email, phone)
                                        navController.navigate("home") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        Toast.makeText(navController.context, message ?: "Login failed. Please try again.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            },
                            containerColor = Color(0xFF6298F0),
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                                color = Color.White,
                                text = "Login",
                                fontSize = 20.sp,
                                fontFamily = latoFontFamily
                            )
                        }
                        Spacer(modifier = Modifier.height(0.01 * screenHeight))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ){
                            Text(
                                text = "Don't have an account?",
                                modifier = Modifier
                                    .padding(start = 5.dp),
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = " Sign Up",
                                modifier = Modifier
                                    .padding(start = 5.dp)
                                    .clickable {
                                        navController.navigate("signup")
                                    },
                                fontSize = 16.sp,
                                color = Color(0xFF6298F0),
                                fontFamily = latoFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    LoginPage(rememberNavController())
}