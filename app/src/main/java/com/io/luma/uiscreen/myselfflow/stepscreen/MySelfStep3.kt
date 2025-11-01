package com.io.luma.uiscreen.myselfflow.stepscreen

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.customcompose.height
import com.io.luma.model.SignupResponseModel
import com.io.luma.navroute.NavRoute
import com.io.luma.network.Resource
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import com.io.luma.utils.TokenManager
import com.io.luma.viewmodel.RegisterViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

@Composable
fun MySelfStep3(navController: NavController, registermyself: RegisterViewModel) {
    var uri = remember { mutableStateOf<Uri?>(null) }
    val image = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri.value = it }
    )
    var context= LocalContext.current

    val userFlow by registermyself.createUser.collectAsState() // ✅ collectAsState() with 'by'
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {

        // ✅ Scaffold should be background layer
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize().background(color = Color.White)
                    .padding(padding).background(Color.White)
            )
            {

                height(20)

                Row(horizontalArrangement = Arrangement.Start,
                    modifier = Modifier.padding(horizontal = 20.sdp).width(95.sdp))
                {
                    Box(modifier = Modifier.border(width = 2.dp, color = Color.Black,
                        shape = RoundedCornerShape(13.sdp)).clip(RoundedCornerShape(16.sdp)).clickable {
                        navController.popBackStack()
                    }.padding(horizontal = 20.sdp).wrapContentSize()) {

                        Row(modifier = Modifier.wrapContentSize().padding(vertical = 15.sdp),
                            verticalAlignment = Alignment.CenterVertically) {

                            Icon(painter = painterResource(R.drawable.backarrow),
                                contentDescription = "")

                            com.io.luma.customcompose.width(3)

                            Text("Back",
                                style = TextStyle(
                                    color = textColor,
                                    fontSize = 16.ssp
                                ))

                        }
                    }
                }

                height(20)

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.sdp)
                        .verticalScroll(rememberScrollState())
                        .imePadding(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        "Step 3",
                        style = TextStyle(
                            color = textColor,
                            fontSize = 20.ssp
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = manropebold,
                        textAlign = TextAlign.Center
                    )
                }

                height(20)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .drawBehind {
                                val strokeWidth = 3.dp.toPx()
                                val dashLength = 10.dp.toPx()
                                val gapLength = 10.dp.toPx()
                                val pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(dashLength, gapLength), 0f
                                )

                                drawRoundRect(
                                    color = Color.Gray,
                                    size = Size(size.width, size.height),
                                    style = Stroke(width = strokeWidth, pathEffect = pathEffect)
                                )
                            }
                            .height(150.sdp)
                            .width(150.sdp)
                            .clickable {
                                image.launch("image/*")
                            }
                    ) {
                        AsyncImage(
                            model = uri.value,
                            contentScale = ContentScale.Crop,
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    height(24)

                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.sdp),
                        "Take Photo"
                    ) {
                        image.launch("image/*")
                    }

                    height(24)

                    CustomOutlineButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.sdp),
                        "Upload Photo"
                    ) {
                        registermyself.updateRole(role = "patient")
                        registermyself.updateCountryCode("+91")
                        Log.d("GSON", registermyself.user.toString())
                        registermyself.addDetils(registermyself.user)
                    }
                }
            }
        }

        // ✅ Overlay loading/error/success over everything
        when (userFlow) {
            is Resource.Loading<*> -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            is Resource.Success<*> -> {

                val response = (userFlow as Resource.Success<SignupResponseModel>).data
               var token= TokenManager.getInstance(context)
                token.saveTokens(response.accessToken.toString(),response.refreshToken.toString())
                token.saveId(response.user?.userId.toString())
                LaunchedEffect(Unit) {
                    registermyself.resetInviteState()
                    navController.navigate(NavRoute.LoginScreen)
                }
            }

            is Resource.Error<*> -> {
                val message = (userFlow as Resource.Error<*>).message
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(message)
                    registermyself.resetInviteState()
                }
            }

            else -> {}
        }
    }
}

//@Composable
//fun MySelfStep3(navController: NavController, registermyself: RegisterViewModel) {
//    var uri = remember { mutableStateOf<Uri?>(null) }
//    var image = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        onResult = {
//
//            uri.value = it
//
//        })
//    val userFlow = registermyself.createUser.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.White)
//            .windowInsetsPadding(
//                WindowInsets.statusBars
//            )
//    )
//    {
//        Scaffold(
//            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
//        ) {
//            Column(
//                modifier = Modifier.fillMaxSize(),
//            )
//            {
//
//                height(20)
//                Row(
//                    horizontalArrangement = Arrangement.Start,
//                    modifier = Modifier.padding(horizontal = 20.sdp)
//                ) {
//
//
//                    Box(
//                        modifier = Modifier
//                            .border(
//                                width = 2.dp, color = Color.Black,
//                                shape = RoundedCornerShape(13.sdp)
//                            )
//                            .clip(RoundedCornerShape(13.sdp))
//                            .clickable {
//                                navController.popBackStack()
//                            }
//                            .padding(horizontal = 20.sdp)
//                            .wrapContentSize()
//                    ) {
//
//                        Row(
//                            modifier = Modifier
//                                .wrapContentSize()
//                                .padding(horizontal = 1.sdp, vertical = 5.sdp),
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//
//                            Icon(
//                                imageVector = Icons.Default.ArrowBack,
//                                contentDescription = ""
//                            )
//
//                            Text(
//                                "Back",
//                                style = TextStyle(
//                                    color = textColor,
//                                    fontSize = 16.ssp
//                                )
//                            )
//
//                        }
//                    }
//                }
//
//
//                height(20)
//
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 24.sdp)
//                        .verticalScroll(rememberScrollState())
//                        .imePadding(),
//                    horizontalAlignment = Alignment.Start
//                )
//                {
//
//                    Text(
//                        "Step 3",
//                        style = TextStyle(
//                            color = textColor,
//                            fontSize = 20.ssp
//                        ),
//                        modifier = Modifier.fillMaxWidth(),
//                        fontFamily = manropebold,
//                        textAlign = TextAlign.Center
//                    )
//
//                }
//                height(20)
//
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//
//                    Box(modifier = Modifier
//                        .drawBehind {
//                            val strokeWidth = 3.dp.toPx()
//                            val dashLength = 10.dp.toPx()
//                            val gapLength = 10.dp.toPx()
//
//                            // Create a dotted or dashed effect
//                            val pathEffect = PathEffect.dashPathEffect(
//                                floatArrayOf(dashLength, gapLength), 0f
//                            )
//
//                            // Draw rectangle border
//                            drawRoundRect(
//                                color = Color.Gray,
//                                size = Size(size.width, size.height),
//                                style = Stroke(width = strokeWidth, pathEffect = pathEffect)
//                            )
//                        }
//                        .height(150.sdp)
//                        .width(150.sdp)
//                        .clickable {
//
//                            image.launch("image/*")
//
//                        }) {
//                        AsyncImage(
//                            model = uri.value,
//                            contentScale = ContentScale.Crop,
//                            contentDescription = "",
//                            modifier = Modifier.fillMaxSize()
//                        )
//                    }
//
//                    height(20)
//
//
//                    CustomButton(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 20.sdp),
//                        "Take Photo"
//                    ) {
//
//
//                        navController.navigate(NavRoute.MyselfStep2)
//                    }
//
//                    height(20)
//
//                    CustomOutlineButton(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 20.sdp),
//                        "Upload Photo"
//                    ) {
//                        registermyself.updateRole(role = "patient")
//                        Log.d("GSON", registermyself.user.toString())
//                        registermyself.addDetils(registermyself.user)
//                        // navController.navigate(NavRoute.OnBordingScreen)
//                    }
//
//
//
//
//                }
//            }
//        }
//
//        when (userFlow) {
//            is Resource.Loading<*> -> {
//                Column(modifier = Modifier.fillMaxSize(),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center) {
//                    CircularProgressIndicator()
//                }  // ✅ now shows while loading
//            }
//
//            is Resource.Success<*> -> {
//                Text("Login Success!")
//            }
//
//            is Resource.Error<*> -> {
//                Text("Error occurred!")
//            }
//
//            else -> {}
//        }
//    }
//}