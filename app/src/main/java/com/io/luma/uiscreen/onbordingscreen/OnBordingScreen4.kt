package com.io.luma.uiscreen.onbordingscreen

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
fun OnBordingScreen4(navController: NavController) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(color = Color.White))
    {


        Column(modifier = Modifier.fillMaxSize().background(color = Color.Transparent)) {

            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).background(color = Color.White),
                contentAlignment = Alignment.Center) {

                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(R.drawable.onbordingluma),
                        contentDescription = "")

                }
            }


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .weight(1f)
            ) {
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
                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 13.sdp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        com.io.luma.customcompose.height(10)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Hello! I am Luma!",
                                style = TextStyle(
                                    color = textColor,
                                    fontSize = 22.ssp
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                fontFamily = manropebold,
                                textAlign = TextAlign.Center
                            )

                            Image(
                                painter = painterResource(R.drawable.cancle),
                                contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd).size(30.sdp) // align image to the right

                            )
                        }
                        Text("Chose who you want\nto talk with",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 22.ssp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = manropebold,
                            textAlign = TextAlign.Center
                        )
                        com.io.luma.customcompose.height(20)

                        Text("Luma (Female)",
                            style = TextStyle(
                                color = Color(0xff4C4C50),
                                fontSize = 26.ssp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = manropebold,
                            textAlign = TextAlign.Center
                        )
                        com.io.luma.customcompose.height(20)

                        CustomButton(modifier = Modifier.fillMaxWidth(),
                            "I Agree") {

                            navController.navigate(NavRoute.OnBordingScreen5)
                        }

                        com.io.luma.customcompose.height(20)

                        CustomOutlineButton (modifier = Modifier.fillMaxWidth(),
                            "Change Assistant") {


                            // navController.navigate(NavRoute.SignupOptionStep5)
                        }


                    }
                }
            }

        }



    }

}