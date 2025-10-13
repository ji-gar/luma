package com.io.luma.uiscreen.onbordingscreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun OnBordingScreen8(navController: NavController) {

    Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(color = Color.White))
    {


        Column(modifier = Modifier.fillMaxSize().background(color = Color.Transparent)) {

            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).background(color = Color.White),
                contentAlignment = Alignment.Center) {

                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(painter = painterResource(R.drawable.img),
                        modifier = Modifier.height(150.dp),
                        contentScale = ContentScale.Crop,
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
                        Text("Your Normal Day",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 22.ssp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = manropebold,
                            textAlign = TextAlign.Center
                        )

                        com.io.luma.customcompose.height(10)


                        Text("Wake Up :- 11:00",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 16.ssp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = manropebold,
                            textAlign = TextAlign.Center
                        )










                    }
                }
            }

        }



    }

}