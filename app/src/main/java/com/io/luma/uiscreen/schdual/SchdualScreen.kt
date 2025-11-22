package com.io.luma.uiscreen.schdual

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.height
import com.io.luma.customcompose.width
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.monospaceMedium
import com.io.luma.ui.theme.skyblue
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun SchdualScreen(navController: NavController) {

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
        )){
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Your click action */ },
                    containerColor = Color.Black, // optional
                    contentColor = Color.White
                ) {
                    Icon(
                        painter = painterResource(R.drawable.iv_mics),
                        contentDescription = "Add",
                        tint = Color.Unspecified
                    )
                }


            },
            floatingActionButtonPosition = FabPosition.End,


        ){

            Column( modifier = Modifier.fillMaxSize().padding(horizontal = 13.sdp).windowInsetsPadding(WindowInsets.statusBars))
            {
                height(30)
                Box(modifier = Modifier.fillMaxWidth(),
                  contentAlignment = Alignment.Center
                )
                {

                    Icon(
                        modifier = Modifier.align(Alignment.CenterStart),
                        painter = painterResource(R.drawable.iv_smallleftarrow),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )

                    Text("Schedule",
                        modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 20.ssp,
                            fontFamily = monospaceMedium,
                            fontWeight = FontWeight.W700
                        )
                    )
                }
                height(30)

                CardInformation(icon = R.drawable.iv_dailyrouting,
                    text = "Reminders") { }
                height(13)
                CardInformation(icon = R.drawable.iv_weaklyrouting,
                    text = "Weekly Schedule") { }
                height(13)
                CardInformation(icon = R.drawable.iv_specialevent,
                    text = "Special Events") { }
                height(13)
                CardInformation(icon = R.drawable.iv_remindercalander,
                    text = "Reminders") { }



            }

        }


    }

}

@Composable
fun CardInformation(icon:Int,text:String,onClick:()->Unit) {

    OutlinedCard(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.sdp)).clickable {
        onClick.invoke()
    },
        shape = RoundedCornerShape(10.sdp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
        )
        ) {

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 10.sdp,
            vertical = 10.sdp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    painter = painterResource(icon),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
                width(7)
                Text(text,
                    style = TextStyle(
                        color = Color(0xff0D0C0C),
                        fontSize = 13.ssp,
                        fontFamily = monospaceMedium,

                        )
                )
            }
            Icon(
                painter = painterResource(R.drawable.iv_smallrightarrow),
                contentDescription = "",
                tint = Color.Unspecified
            )

        }

}
}
