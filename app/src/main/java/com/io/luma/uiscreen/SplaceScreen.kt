package com.io.luma.uiscreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.navroute.NavRoute
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.delay

@Composable
fun SplaceScreen(navController: NavController) {
    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }

    LaunchedEffect(true) {
        delay(4000)
        navController.navigate(NavRoute.MobileScreen)
    }


    Box(modifier = Modifier.fillMaxSize().background(color = Color.White), contentAlignment = Alignment.Center){

        Image(painter = painterResource(R.drawable.helloluma),
            contentDescription = "Hello Luma",)


        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).windowInsetsPadding(
            WindowInsets.navigationBars))
        {
            Row(modifier= Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center) {


                AnimatedVisibility(visibleState = state) {
                    Text(text = "L U M A",
                        fontSize = 24.ssp,
                        style = TextStyle(
                            color = Color.Black,
                            fontWeight = FontWeight.W700
                        ))
                }
            }
        }


    }

}