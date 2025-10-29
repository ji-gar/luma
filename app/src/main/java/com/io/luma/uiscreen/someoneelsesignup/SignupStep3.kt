package com.io.luma.uiscreen.someoneelsesignup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.textColor
import com.io.luma.viewmodel.CarerRegisterViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SignupStep3(navController: NavController, carerViewModel: CarerRegisterViewModel) {
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
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.sdp))
            {

                Icon(imageVector = Icons.Filled.KeyboardArrowLeft,
                    modifier = Modifier.clickable{
                        navController.popBackStack()
                    },
                    contentDescription = "Back")


                Image(painter = painterResource(R.drawable.lumalifewide),
                    contentDescription = "",
                    modifier = Modifier.height(33.sdp),

                    )

                Image(painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .height(16.sdp)
                        .clip(CircleShape))
            }
            com.io.luma.customcompose.height(30)


            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.sdp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text("Hello, ${carerViewModel.user.fullName}. Your account\nhas been created",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Center
                )
                com.io.luma.customcompose.height(13)

                Text("Get started by adding the person\nyou care for.",
                    style = TextStyle(
                        color = Color(0xff4C4C50),
                        fontSize = 13.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Center
                )

                com.io.luma.customcompose.height(13)
                CustomButton(modifier = Modifier.fillMaxWidth(),
                    text = "Add PWD") {

                    navController.navigate(NavRoute.SignupOptionStep4)
                }

            }

        }


    }


}