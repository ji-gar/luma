package com.io.luma.uiscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.utils.TokenManager
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SignupCarer(navController: NavController) {

    LaunchedEffect(true) {

        if(TokenManager.getInstance().getAccessToken()!=null)
        {
            navController.navigate(NavRoute.OnBordingScreen)

        }
    }

    Box(modifier = Modifier.fillMaxSize().background(color = Color.White),
        contentAlignment = Alignment.Center) {

        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().padding(
                horizontal = 16.sdp
            ),

            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Image(painter = painterResource(R.drawable.helloluma),
                contentDescription = "Logo",
                modifier = Modifier.size(245.sdp))
            com.io.luma.customcompose.height(16)

            Text("Hello! I am Luma!",
                style = TextStyle(
                    color = textColor,
                    fontFamily = verandaBold,
                    fontSize = 22.ssp,
                    fontWeight = FontWeight.W700
                ))

            com.io.luma.customcompose.height(16)

            Text("I’m your helper, here to make things easier and guide you through each day.!",
                style = TextStyle(
                    color = Color(0xff4C4C50),
                    fontFamily = verandaBold,
                    fontSize = 22.ssp,
                    fontWeight = FontWeight.W700
                ),
                textAlign = TextAlign.Center)

            com.io.luma.customcompose.height(16)

            CustomButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Join Luma", isIcon = true) {

                 navController.navigate(NavRoute.MobileScreen)
            }



        }



    }

}