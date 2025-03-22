package com.example.eventmanagement.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.eventmanagement.R
import com.example.eventmanagement.ui.theme.latoFontFamily
import com.example.eventmanagement.viewmodel.EventViewModel

fun extractDate(dateTime: String): String {
    return if (dateTime.length >= 8) {
        val year = dateTime.substring(0, 4)
        val month = dateTime.substring(4, 6)
        val day = dateTime.substring(6, 8)
        "$day-$month-$year"
    } else {
        "Invalid Date"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ViewModelConstructorInComposable")
@Composable
fun AttendPage(navController: NavController) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    val context = LocalContext.current
    val viewModel: EventViewModel = EventViewModel()
    val events by viewModel.events
    val loading by viewModel.loading
    val error by viewModel.error
    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    var num by remember { mutableStateOf(0) }

    viewModel.fetchEvents()
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
                            bottom = 0.02 * screenWidth
                        )
                        .windowInsetsPadding(WindowInsets.systemBars.only(WindowInsetsSides.Bottom)),
                ) {
                    Text(
                        text = "Attend Events",
                        fontFamily = latoFontFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.W500
                    )
                    Spacer(modifier = Modifier.height(0.05 * screenWidth))
                    when{
                        loading -> {
                            CircularProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                        error != null -> {
                            Log.e("AttendPage", "Error: $error")
                            Text(text = "Error: $error", color = Color.Red, modifier = Modifier.fillMaxWidth())
                        }
                        events.isNotEmpty() -> {
                            Log.e("AttendPage", "Error: $error")
                            LazyColumn {
                                repeat(events.size) {
                                    item {
                                        Column(
                                            modifier = Modifier
                                                .clickable {
                                                    num = it
                                                    showSheet = true
                                                }
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .shadow(
                                                        elevation = 5.dp, // Elevation value
                                                        shape = RoundedCornerShape(8.dp), // Shape of the box
                                                        clip = true // If true, clips the shadow to the shape of the Box
                                                    )
                                                    .background(Color.White),
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .padding(8.dp)
                                                        .fillMaxWidth(),
                                                    verticalArrangement = Arrangement.Center,
                                                ) {
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Image(
                                                            painter = painterResource(id = R.drawable.img),
                                                            contentDescription = "Home",
                                                            Modifier.size(48.dp)
                                                        )
                                                        Spacer(modifier = Modifier.width(10.dp))
                                                        Column(
                                                            modifier = Modifier.weight(1f),
                                                            verticalArrangement = Arrangement.Center
                                                        ) {
                                                            Text(
                                                                text = events[it].name,
                                                                fontSize = 22.sp
                                                            )
                                                            Text(
                                                                text = events[it].location + " | " + extractDate(events[it].startDate),
                                                                fontSize = 14.sp
                                                            )
                                                        }
                                                    }
                                                    Column(
                                                        modifier = Modifier.fillMaxWidth(),
                                                    ) {
                                                        var expanded by remember {
                                                            mutableStateOf(
                                                                false
                                                            )
                                                        }
                                                        val minimizedMaxLines = 2
                                                        Text(
                                                            modifier = Modifier.padding(4.dp),//.clickable{expanded = !expanded}
                                                            text = events[it].desc.trimIndent(),
                                                            maxLines = if (expanded) Int.MAX_VALUE else minimizedMaxLines,
                                                            overflow = TextOverflow.Ellipsis,
                                                        )
                                                        Text(
                                                            text = if (expanded) "Read Less" else "Read More",
                                                            color = Color.Blue,
                                                            modifier = Modifier
                                                                .padding(start = 4.dp)
                                                                .clickable { expanded = !expanded }
                                                        )
                                                    }
                                                    Column {
                                                        AsyncImage(
                                                            model = events[it].image,
                                                            contentDescription = "image",
                                                            modifier = Modifier
                                                                .padding(5.dp)
                                                                .clip(shape = RoundedCornerShape(8.dp))
                                                        )
                                                    }
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(0.025 * screenHeight))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (showSheet) {
                    ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = sheetState,
                        scrimColor = Color.Transparent,
                        containerColor = Color.White,
                        dragHandle = null
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(0.3f * screenHeight)  // Adjusted for edge-to-edge
                            ) {
                                AsyncImage(
                                    model = events[num].image,
                                    contentDescription = "image",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(0.38 * screenHeight),
                                    contentScale = ContentScale.Crop
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Black.copy(alpha = 0.0f),
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.Transparent,
                                                    Color.White.copy(alpha = 1f)
                                                )
                                            )
                                        )
                                )
                                Box(
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .width(50.dp)
                                        .height(5.dp)
                                        .background(Color.Black, shape = RoundedCornerShape(50))
                                        .align(Alignment.TopCenter)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 0.012 * screenHeight,
                                        start = 0.035 * screenWidth,
                                        end = 0.035 * screenWidth
                                    )
                            ) {
                                Text(
                                    events[num].name,
                                    fontSize = 24.sp,
                                    color = Color.Black,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    events[num].desc,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Justify,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Dates: " + extractDate(events[num].startDate) + " - " + extractDate(events[num].endDate),
                                    color = Color(0xFF000000),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.W500,
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Text(
                                    text = "Venue: " + events[num].location,
                                    color = Color(0xFF000000),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W500,
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row{
                                    Text(
                                        text = "Location: ",
                                        color = Color(0xFF000000),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                    Text(
                                        text = " " + events[num].location,
                                        color = Color(0xFF6298F0),
                                        fontSize = 18.sp,
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.W500,
                                        textDecoration = TextDecoration.Underline,
                                        modifier = Modifier.clickable {
                                            val intent = Intent(Intent.ACTION_VIEW,
                                                events[num].locationLink.toUri())
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row{
                                    Text(
                                        text = "Instagram Link: ",
                                        color = Color(0xFF000000),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                    Text(
                                        text = " Instagram",
                                        color = Color(0xFF6298F0),
                                        fontSize = 18.sp,
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.W500,
                                        textDecoration = TextDecoration.Underline,
                                        modifier = Modifier.clickable {
                                            val intent = Intent(Intent.ACTION_VIEW,
                                                events[num].instagram.toUri())
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row{
                                    Text(
                                        text = "Add to Calendar: ",
                                        color = Color(0xFF000000),
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                    Text(
                                        text = "Google Calendar",
                                        color = Color(0xFF6298F0),
                                        fontSize = 18.sp,
                                        fontFamily = latoFontFamily,
                                        fontWeight = FontWeight.W500,
                                        textDecoration = TextDecoration.Underline,
                                        modifier = Modifier.clickable {
                                            val intent = Intent(Intent.ACTION_VIEW,
                                                events[num].calendar.toUri())
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Event Type: " + events[num].type,
                                    color = Color(0xFF4CAF50),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                                FloatingActionButton(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(shape = RoundedCornerShape(50)),
                                    onClick = {
                                        Log.d("AttendPage", "Registering for event ${events[num].id}")
                                        navController.navigate(
                                            "register/${events[num].id}"
                                        )
                                    },
                                    containerColor = Color(0xFF6298F0),
                                ) {
                                    Text(
                                        modifier = Modifier.padding(top = 12.dp, bottom = 12.dp),
                                        color = Color.White,
                                        text = "I'm Interested",
                                        fontSize = 20.sp,
                                        fontFamily = latoFontFamily
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun AttendPagePreview() {
    AttendPage(rememberNavController())
}