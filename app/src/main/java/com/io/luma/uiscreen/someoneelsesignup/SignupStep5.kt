package com.io.luma.uiscreen.someoneelsesignup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaRegular
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SignupStep5(navController: NavController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(
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
        Column(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)) {


            com.io.luma.customcompose.height(30)
            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.sdp)) {

                Icon(imageVector = Icons.Filled.KeyboardArrowLeft,
                    modifier = Modifier.clickable{
                        navController.popBackStack()
                    },
                    contentDescription = "Back")


                Image(painter = painterResource(R.drawable.luma_life),
                    contentDescription = "",
                    modifier = Modifier.height(40.sdp),
                    contentScale = ContentScale.Crop
                )

                Image(painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "",
                    modifier = Modifier
                        .height(32.sdp)
                        .clip(CircleShape))
            }

            com.io.luma.customcompose.height(30)


            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp).verticalScroll(
                    rememberScrollState()
                )
            )
            {

                Text("Set up the profile for the person you care for",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Center
                )

                com.io.luma.customcompose.height(20)


                CustomOutlineButton (modifier = Modifier.fillMaxWidth(),
                    bgColor = Color(0xffDAE1FD),
                    text = "Your PWD is Amy Bishop") {

                }
                com.io.luma.customcompose.height(20)

                Text("+49 151 234 56 78",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Start
                )

                Text("amybishop@gmail.com",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 16.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Start
                )

                com.io.luma.customcompose.height(20)

               Spacer(modifier = Modifier.fillMaxHeight().weight(1f))






            }
            Spacer(modifier = Modifier.fillMaxHeight().weight(1f))
            Column {
                CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp),
                    text = "Save") {
                         navController.navigate(NavRoute.SignupOptionStep6)

                }
                com.io.luma.customcompose.height(20)

                Row(modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center) {

                    Text("By creating an account or signing you agree to our", style = TextStyle(
                        color = Color(0xff3F3F3F),
                        fontWeight = FontWeight.W500,
                        fontFamily = manropebold,
                        fontSize = 14.sp
                    ))


                }
                Text("Terms and Conditions",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = TextStyle(
                        color = Color(0xff2951E0),
                        fontWeight = FontWeight.W500,
                        fontFamily = verandaRegular,
                        fontSize = 14.sp,
                    ))
                com.io.luma.customcompose.height(30)
                com.io.luma.customcompose.height(30)
            }


        }


    }

}