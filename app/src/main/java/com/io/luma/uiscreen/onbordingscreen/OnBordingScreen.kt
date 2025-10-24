package com.io.luma.uiscreen.onbordingscreen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBordingScreen(navController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition(label = "move")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f, // Move upward
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )
    Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(color = Color.White))
    {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(color = Color.White),
            contentAlignment = Alignment.Center) {

            Image(painter = painterResource(R.drawable.onbordingluma),
                modifier = Modifier.offset(y = offsetY.dp),
                contentDescription = "")
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .align(alignment = Alignment.BottomCenter)
        )
        {
            // Top shadow layer


            // Card content
            OutlinedCard(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    com.io.luma.customcompose.height(10)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth().padding(end = 13.sdp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Welcome",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 24.ssp,
                                fontFamily = manropebold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Image(
                            painter = painterResource(R.drawable.cancle),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.CenterEnd).size(30.sdp) // align image to the right

                        )
                    }
                    com.io.luma.customcompose.height(13)
                    HorizontalDivider(
                        color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    com.io.luma.customcompose.height(20)

                    Text("Do you want to\njoin?",
                        style = TextStyle(
                            color = textColor,
                            fontSize = 27.ssp,
                            fontWeight = FontWeight.W700
                        ),
                        modifier = Modifier.fillMaxWidth(),

                        textAlign = TextAlign.Center
                    )
                    com.io.luma.customcompose.height(20)

                    CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                        "Yes") {


                        navController.navigate(NavRoute.OnBordingScreen2)
                    }

                    com.io.luma.customcompose.height(20)

                    CustomOutlineButton (modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                        "No") {


                        // navController.navigate(NavRoute.SignupOptionStep5)
                    }


                }
            }
        }



//        Column(modifier = Modifier.fillMaxSize().background(color = Color.Transparent)) {
//
//            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).background(color = Color.White),
//                contentAlignment = Alignment.Center) {
//
//                Column(modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painter = painterResource(R.drawable.lumaperson),
//                        contentDescription = "")
//
//                }
//            }
//
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .weight(0.5f)
//                    .align(alignment = Alignment.BottomCenter)
//            )
//            {
//                // Top shadow layer
//
//
//                // Card content
//                OutlinedCard(
//                    modifier = Modifier.fillMaxSize(),
//                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color.White,
//                    ),
//                    border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
//                    )
//                ) {
//                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 13.sdp),
//                        horizontalAlignment = Alignment.CenterHorizontally) {
//                        com.io.luma.customcompose.height(10)
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                "Welcome",
//                                style = TextStyle(
//                                    color = textColor,
//                                    fontSize = 24.ssp,
//                                    fontFamily = manropebold
//                                ),
//                                textAlign = TextAlign.Center
//                            )
//
//                            Image(
//                                painter = painterResource(R.drawable.cancle),
//                                contentDescription = "",
//                                modifier = Modifier
//                                    .align(Alignment.CenterEnd).size(30.sdp) // align image to the right
//
//                            )
//                        }
//
//                        com.io.luma.customcompose.height(20)
//
//                        Text("Do you want to\njoin?",
//                            style = TextStyle(
//                                color = textColor,
//                                fontSize = 27.ssp
//                            ),
//                            modifier = Modifier.fillMaxWidth(),
//                            fontFamily = manropebold,
//                            textAlign = TextAlign.Center
//                        )
//                        com.io.luma.customcompose.height(20)
//
//                        CustomButton(modifier = Modifier.fillMaxWidth(),
//                            "Yes") {
//
//
//                            navController.navigate(NavRoute.OnBordingScreen2)
//                        }
//
//                        com.io.luma.customcompose.height(20)
//
//                        CustomOutlineButton (modifier = Modifier.fillMaxWidth(),
//                            "No") {
//
//
//                           // navController.navigate(NavRoute.SignupOptionStep5)
//                        }
//
//
//                    }
//                }
//            }
//
//        }



    }

}