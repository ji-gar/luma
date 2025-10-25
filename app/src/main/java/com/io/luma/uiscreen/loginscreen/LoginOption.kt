package com.io.luma.uiscreen.loginscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import com.io.luma.uiscreen.someoneelsesignup.rowHeader
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun LoginOption(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(color = Color.White))
    {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(color = Color.White),
            contentAlignment = Alignment.Center) {

            Image(painter = painterResource(R.drawable.helloluma),
                contentDescription = "")
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)

                .align(alignment = Alignment.BottomCenter)
        )
        {
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
                    verticalArrangement = Arrangement.Center,) {
                    com.io.luma.customcompose.height(10)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth().padding(end = 13.sdp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Welcome To\nLuma",
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
                  CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                        "Login") {

                      navController.navigate(NavRoute.LoginScreen)
                  }

                    com.io.luma.customcompose.height(20)
                    CustomOutlineButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                        "Signup") {
                        navController.navigate(NavRoute.SignupOption)
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