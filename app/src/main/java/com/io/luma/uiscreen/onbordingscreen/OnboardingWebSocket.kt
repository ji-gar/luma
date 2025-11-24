package com.io.luma.uiscreen.onbordingscreen


import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.customcompose.height
import com.io.luma.customcompose.width
import com.io.luma.model.CalendarItem
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.utils.TokenManager
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

/**
 * OnboardingScreen5 - WebRTC Voice Interaction
 * Uses pure WebRTC implementation for voice communication with AI
 */
@Composable
fun OnBoardingWebSocket(navController: NavController) {
    val context = LocalContext.current
    val userId = TokenManager.getInstance(context).getId() ?: ""
    val optionList = remember { mutableStateListOf<RadioGroup>() }
    var contact by remember { mutableStateOf("") }



    val (selectedIndex, onSelectedIndexChange) = remember { mutableStateOf(0) }
    // WebRTC ViewModel
    val viewModel = remember {
        com.io.luma.webrtc.WebRTCViewModel(
            context = context,
            userId = userId,
            environment = com.io.luma.webrtc.WebRTCConfiguration.ServerEnvironment.Production
        )
    }

    // UI State
    var isCalander by remember { mutableStateOf(false) }
    var isContactList by remember { mutableStateOf(false) }
    val calendarList = remember { mutableStateListOf<CalendarItem>() }

    // Animation for mute button
    val backgroundColor by animateColorAsState(
        targetValue = if (viewModel.isMuted) Color(0xFFED8936) else Color(0xFF48BB78),
        animationSpec = tween(durationMillis = 300)
    )

    // Microphone permission
    val micPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        micPermissionGranted.value = granted
        if (granted) {
            viewModel.connect()
        }
    }

    // Auto-connect when permission is granted
    LaunchedEffect(micPermissionGranted.value) {
        if (micPermissionGranted.value && !viewModel.isConnected) {
            viewModel.connect()
        } else if (!micPermissionGranted.value) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // Setup UI callbacks
    LaunchedEffect(Unit) {
        viewModel.setOnCalendarShow { events ->
            isCalander = true
            // Parse calendar events if needed
        }

        viewModel.setOnCalendarHide {
            isCalander = false
        }

        viewModel.setOnContactShow { contacts ->
            isContactList = true
            // Parse contacts if needed
        }

        viewModel.setOnContactHide {
            isContactList = false
        }
    }

    // Cleanup on back press
    BackHandler {
        Log.d("OnboardingScreen5", "Back button pressed - cleaning up WebRTC")
        viewModel.disconnect()
        navController.navigate(NavRoute.OnBordingScreen4)
    }

    // Cleanup on dispose
    DisposableEffect(Unit) {
        onDispose {
            Log.d("OnboardingScreen5", "Screen disposed - cleaning up WebRTC")
            viewModel.disconnect()
        }
    }

    // UI
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        // 3D Model Background
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Image(
                painter = painterResource(R.drawable.onbordingluma),
                contentDescription = ""
            )
        }

        when {
            isCalander -> {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter)
                ) {
                    OutlinedCard(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.elevatedCardElevation(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(13.sdp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            com.io.luma.customcompose.height(13)
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center)
                            {

                                Icon(painter = painterResource(R.drawable.iv_indicator),
                                    contentDescription = "",tint=Color.Unspecified)
                            }
                            com.io.luma.customcompose.height(13)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth().padding(end = 13.sdp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Your Nomal Day",
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 21.ssp,
                                        fontFamily = verandaBold,
                                        fontWeight = FontWeight.W700
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Icon(
                                    painter = painterResource(R.drawable.cancle),
                                    contentDescription = "",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd) // align image to the right

                                )
                            }
                            com.io.luma.customcompose.height(13)
                            HorizontalDivider(
                                color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                                thickness = 1.dp
                            )
                            com.io.luma.customcompose.height(13)

                            // 3️⃣ Show each item in list
                            calendarList.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                ) {
                                    Text(
                                        text = item.time ?: "--:--",
                                        fontWeight = FontWeight.W700,
                                        style = TextStyle(
                                            fontFamily = verandaBold,
                                            fontSize = 21.ssp,
                                            color = Color(0xff0D0C0C)
                                        )
                                    )
                                    width(10)
                                    Text(
                                        text = item.title ?: "",
                                        fontWeight = FontWeight.W700,

                                        style = TextStyle(
                                            fontFamily = verandaBold,
                                            fontSize = 21.ssp,
                                            color = Color(0xff0D0C0C)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlineButton(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                text = "See you Afternoon"
                            )
                        }
                    }
                }
            }

            isContactList -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter)
                ) {

                    OutlinedCard(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.elevatedCardElevation(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                                .padding(horizontal = 13.dp),
                        ) {
                            com.io.luma.customcompose.height(13)
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center)
                            {

                                Icon(painter = painterResource(R.drawable.iv_indicator),
                                    contentDescription = "",tint=Color.Unspecified)
                            }
                            com.io.luma.customcompose.height(13)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth().padding(end = 13.sdp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Choose Your\nCarer!",
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 21.ssp,
                                        fontFamily = verandaBold,
                                        fontWeight = FontWeight.W700
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Icon(
                                    painter = painterResource(R.drawable.cancle),
                                    contentDescription = "",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd) // align image to the right

                                )
                            }
                            com.io.luma.customcompose.height(13)
                            HorizontalDivider(
                                color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                                thickness = 1.dp
                            )
                            com.io.luma.customcompose.height(13)
                            optionList.forEachIndexed { index,text ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth().padding(bottom = 10.dp)
                                        .background(
                                            color = Color.White,
                                            shape = RoundedCornerShape(12.dp)
                                        ) .selectable(
                                            selected = (index == selectedIndex),
                                            onClick = { onSelectedIndexChange(index) }
                                        )
                                        .border(width =if (index==selectedIndex)  2.dp else  1.dp, Color.Black, RoundedCornerShape(12.dp))
                                    ,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (index == selectedIndex),
                                        onClick = { onSelectedIndexChange(index) }, // click works now
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color.Black,
                                            unselectedColor = Color.Black
                                        )
                                    )
                                    Column(modifier = Modifier.padding(top = 13.sdp,
                                        bottom = 13.sdp
                                    )
                                    ) {
                                        Text(
                                            text = text.tilte,
                                            style = TextStyle(
                                                fontFamily = verandaBold,
                                                color = Color(0xff0D0C0C),
                                                fontWeight = FontWeight.W700,
                                                fontSize = 18.ssp
                                            )
                                        )
                                        Text(
                                            text = text.dec,
                                            style = TextStyle(
                                                fontFamily = verandaBold,
                                                color = Color(0xff4C4C50),
                                                fontSize = 18.ssp
                                            )
                                        )
                                    }
                                }
                            }
                            com.io.luma.customcompose.height(13)

                            CustomButton(modifier = Modifier.fillMaxWidth(),
                                "Yes") {

                                //  navController.navigate(NavRoute.OnBordingScreen8)
                            }
                        }
                    }
                }
            }

            else ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    height(40)

                    // Title
                    Text(
                        text = "Voice Interaction",
                        style = TextStyle(
                            fontFamily = verandaBold,
                            fontSize = 24.ssp,
                            color = textColor,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    height(20)

                    // Connection Status
                    Text(
                        text = viewModel.connectionStatusText,
                        style = TextStyle(
                            color = when {
                                viewModel.isConnected -> Color(0xFF4CAF50)
                                viewModel.connectionStatusText == "Connecting..." -> Color(0xFFFF9800)
                                else -> Color.Gray
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        textAlign = TextAlign.Center
                    )

                    height(20)

                    // Conversation Display
                    if (viewModel.receivedText.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                        ) {
                            Text(
                                text = viewModel.receivedText,
                                style = TextStyle(
                                    color = Color(0xFF333333),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        height(20)
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Mute/Unmute Button (only show when connected)
                    if (viewModel.isConnected) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .shadow(8.dp, CircleShape)
                                .background(backgroundColor, shape = CircleShape)
                                .clickable {
                                    viewModel.toggleMute()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (viewModel.isMuted) "🔇" else "🎤",
                                fontSize = 32.sp,
                                color = Color.White
                            )
                        }

                        height(10)

                        Text(
                            text = if (viewModel.isMuted) "Tap to unmute" else "Tap to mute",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        // Show connecting indicator
                        CircularProgressIndicator(
                            modifier = Modifier.size(60.dp),
                            color = Color(0xFF667eea)
                        )

                        height(10)

                        Text(
                            text = "Connecting to voice service...",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    height(40)

                    // Back Button
                    CustomButton(
                        text = "Back",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 13.dp)
                    ) {
                        viewModel.disconnect()
                        navController.navigate(NavRoute.OnBordingScreen4)
                    }

                    height(20)
                }

        }

        // Main Content


//        // Calendar Overlay (if needed)
//        if (isCalander) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f))
//                    .clickable { isCalander = false },
//                contentAlignment = Alignment.Center
//            ) {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth(0.9f)
//                        .fillMaxHeight(0.7f),
//                    shape = RoundedCornerShape(16.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Calendar Events",
//                            style = TextStyle(
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        )
//                        // Add calendar list here
//                    }
//                }
//            }
//        }
//
//        // Contact List Overlay (if needed)
//        if (isContactList) {
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.5f))
//                    .clickable { isContactList = false },
//                contentAlignment = Alignment.Center
//            ) {
//                Card(
//                    modifier = Modifier
//                        .fillMaxWidth(0.9f)
//                        .fillMaxHeight(0.7f),
//                    shape = RoundedCornerShape(16.dp)
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp)
//                    ) {
//                        Text(
//                            text = "Contacts",
//                            style = TextStyle(
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        )
//                        // Add contact list here
//                    }
//                }
//            }
//        }
    }
}