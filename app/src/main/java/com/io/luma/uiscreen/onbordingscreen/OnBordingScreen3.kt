package com.io.luma.uiscreen.onbordingscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.customcompose.height
import com.io.luma.customcompose.width
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun OnBordingScreen3(navController: NavController) {


    Column(modifier = Modifier.fillMaxSize().background(color = Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {




        Text("Welcome",
            style = TextStyle(
                color = textColor,
                fontSize = 24.ssp
            ),
            modifier = Modifier.fillMaxWidth(),
            fontFamily = manropebold,
            textAlign = TextAlign.Center
        )
        height(20)

        Text("Choose Who you want to talk with",
            style = TextStyle(
                color = textColor,
                fontSize = 22.ssp
            ),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp),
            fontFamily = manropebold,
            textAlign = TextAlign.Center
        )

        height(20)

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp),
            horizontalArrangement = Arrangement.Center) {

            CustomOutlineButton(modifier = Modifier.weight(1f),"Male") { }
            width(20)
            CustomOutlineButton(modifier = Modifier.weight(1f),"Female") {

                navController.navigate(NavRoute.OnBordingScreen4)
            }
        }





    }

}