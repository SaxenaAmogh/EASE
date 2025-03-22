package com.example.eventmanagement.view

import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.launch

fun logout(context: Context) {
    FirebaseAuth.getInstance().signOut()
    SessionManager(context).logout()
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomePage(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController) // Sidebar content
        }
    ) {
        Scaffold(
            content = {
                Column(
                    modifier = Modifier
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
                            )
                            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Welcome to EASE!",
                                fontFamily = latoFontFamily,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Icon(
                                Icons.Default.AccountCircle,
                                contentDescription = "profile",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        scope.launch {
                                            drawerState.open() // Open sidebar on click
                                        }
                                    }
                            )
                        }
                        Spacer(modifier = Modifier.height(0.2 * screenWidth))
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.homeimage),
                                contentDescription = "mainpage",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(18.dp))
                                    .height(0.38 * screenHeight)
                                    .fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(0.025 * screenWidth))
                            Text(
                                text = "One Stop Solution for all your Event Management Needs!!",
                                fontFamily = latoFontFamily,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.W500,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(0.05 * screenWidth))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Image(
                                    painter = painterResource(R.drawable.host),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable {
                                            navController.navigate("hosting")
                                        }
                                )
                                Image(
                                    painter = painterResource(R.drawable.attend),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f)
                                        .clip(RoundedCornerShape(20.dp))
                                        .clickable {
                                            navController.navigate("attend")
                                        }
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun DrawerContent(navController: NavController) {

    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Top))
            .padding(
                start = 16.dp,
                end = 16.dp,
                top = 20.dp
            )
    ) {
        Text(
            "Profile",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            fontFamily = latoFontFamily
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "profile",
                modifier = Modifier
                    .size(90.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Amogh Saxena",
                        fontSize = 20.sp,
                        fontFamily = latoFontFamily
                    )
                    Text(
                        "amoghsaxena@gmail.com",
                        fontSize = 16.sp,
                        fontFamily = latoFontFamily
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "View Profile",
                    fontSize = 18.sp,
                    color = Color(0xFF6298F0),
                    fontFamily = latoFontFamily
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "My Events",
            fontSize = 18.sp,
            fontFamily = latoFontFamily,
            modifier = Modifier.clickable{
                //navController.navigate("myEvents")
            })
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Registered Events",
            fontSize = 18.sp,
            fontFamily = latoFontFamily,
            modifier = Modifier.clickable{
                //navController.navigate("myEvents")
            })
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "My Events",
            fontSize = 18.sp,
            fontFamily = latoFontFamily,
            modifier = Modifier.clickable{
                //navController.navigate("myEvents")
            })
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Settings",
            fontSize = 18.sp,
            fontFamily = latoFontFamily,
            modifier = Modifier.clickable{
//            { navController.navigate("settings")
            })
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "Logout",
            fontSize = 18.sp,
            modifier = Modifier.clickable
            {
                logout(context)
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            })
    }
}

@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    HomePage(rememberNavController())
}
