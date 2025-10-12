package com.io.luma.uiscreen.someoneelsesignup

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignupStep1(navController: NavController) {
    val options = listOf("They don’t have any memory issues at present.", "People close to me notice they’re more forgetful and they sometimes lose their way in infamiliar places.",
        "People close to me notice they’re more forgetful and they sometimes lose their way in infamiliar places.", "They need daily help with personal care or cannot live alone.")

    // State for each checkbox
    val checkedStates = remember { mutableStateListOf(*Array(options.size) { false }) }
    Box(modifier = Modifier.fillMaxSize().background(
        brush = Brush.linearGradient(
            listOf(
                goldenYellow,
                Color.White,
                Color.White,
                Color.White,
                skyblue
            )
        )
    ))
    {
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {

            com.io.luma.customcompose.height(30)
            Row(horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(R.drawable.luma_life),
                    modifier = Modifier.height(40.sdp),
                    contentScale = ContentScale.Crop,
                    contentDescription = "")
            }
            com.io.luma.customcompose.height(30)


            Column(
             modifier = Modifier.fillMaxWidth().padding(horizontal = 21.sdp)

            )
            {

                Text("Which statement best describes them?",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Center
                )

                com.io.luma.customcompose.height(30)
                options.forEachIndexed { index, option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .toggleable(
                                value = checkedStates[index],
                                onValueChange = { checkedStates[index] = it }
                            )
                    ) {
                        Checkbox(
                            checked = checkedStates[index],
                            onCheckedChange = { checkedStates[index] = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color.Black,
                                uncheckedColor = Color.Black
                            )

                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = option, style =
                            TextStyle(
                                fontFamily = manropesemibold,
                                fontSize = 13.ssp,
                                color = textColor
                            ))
                    }
                }
                com.io.luma.customcompose.height(30)
            }

            com.io.luma.customcompose.height(30)

            CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 21.sdp),
                text = "Continue") {

                navController.navigate(NavRoute.SignupOptionStep2)
            }
            }


        }





    }
