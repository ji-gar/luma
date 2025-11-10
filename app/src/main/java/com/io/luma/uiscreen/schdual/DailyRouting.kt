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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.height
import com.io.luma.customcompose.width
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.monospaceMedium
import com.io.luma.ui.theme.skyblue
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyRouting(navController: NavController)
{
   var showModel= remember { mutableStateOf(true) }
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

            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 13.sdp).windowInsetsPadding(WindowInsets.statusBars)) {
                height(30)
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {

                    Icon(

                        painter = painterResource(R.drawable.iv_smallleftarrow),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )

                    Text("Schedule",

                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 20.ssp,
                            fontFamily = monospaceMedium,
                            fontWeight = FontWeight.W700
                        )
                    )
                    Icon(

                        painter = painterResource(R.drawable.iv_addplus),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                )
                {


                    height(13)

                    Text(
                        "Morning",

                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 20.ssp,
                            fontFamily = monospaceMedium,
                            fontWeight = FontWeight.W700
                        )
                    )
                    height(13)

                    EventInformation(
                        "7:30",
                        text = "Reminders"
                    ) { }
                    height(13)
                    EventInformation(
                        "8:30",
                        text = "Weekly Schedule"
                    ) { }
                    height(13)
                    EventInformation(
                        "8:25",
                        text = "Special Events"
                    ) { }
                    height(13)



                    Text(
                        "Afternoon",

                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 20.ssp,
                            fontFamily = monospaceMedium,
                            fontWeight = FontWeight.W700
                        )
                    )
                    height(13)

                    EventInformation(
                        "7:30",
                        text = "Reminders"
                    ) { }
                    height(13)
                    EventInformation(
                        "8:30",
                        text = "Weekly Schedule"
                    ) { }
                    height(13)
                    EventInformation(
                        "8:25",
                        text = "Special Events"
                    ) { }
                    height(13)


                    Text(
                        "Evening",

                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 20.ssp,
                            fontFamily = monospaceMedium,
                            fontWeight = FontWeight.W700
                        )
                    )
                    height(13)

                    EventInformation(
                        "7:30",
                        text = "Reminders"
                    ) { }
                    height(13)
                    EventInformation(
                        "8:30",
                        text = "Weekly Schedule"
                    ) { }
                    height(13)
                    EventInformation(
                        "8:25",
                        text = "Special Events"
                    ) { }


                    if (showModel.value)
                    {
                        ModalBottomSheet(
                            onDismissRequest = {
                                showModel.value=false
                            },
                            containerColor = Color.White,
                            shape = RoundedCornerShape(topStart = 13.sdp, topEnd = 13.sdp)
                        ) {

                            Column(modifier = Modifier.fillMaxSize()) {

                                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,

                                    )
                                {
                                    Text("Edit Routine",
                                        style = TextStyle(
                                            color = Color(0xff0D0C0C),
                                            fontSize = 15.ssp,
                                            fontFamily = manropebold,
                                            fontWeight = FontWeight.W700
                                        )
                                    )
                                    Icon(
                                        modifier = Modifier.clickable {
                                            showModel.value=false
                                        },
                                        painter = painterResource(R.drawable.iv_cross),
                                        contentDescription = "",
                                        tint = Color.Unspecified
                                    )

                                }
                                height(13)
                                HorizontalDivider(
                                    color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                                    thickness = 1.dp
                                )
                                height(20)
                                Column(modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                )
                                {

                                    Text("Do you want to edit",
                                        style = TextStyle(
                                            color = Color(0xff4C4C50),
                                            fontSize = 13.ssp,
                                            fontFamily = monospaceMedium,
                                            fontWeight = FontWeight.W700
                                        ))
                                    height(10)
                                    Text("Wake up",
                                        style = TextStyle(
                                            color = Color(0xff0D0C0C),
                                            fontSize = 26.ssp,
                                            fontFamily = manropebold,
                                            fontWeight = FontWeight.W500
                                        ))
                                    height(10)
                                    Text("daily routine for Amy Bishop?",
                                        style = TextStyle(
                                            color = Color(0xff4C4C50),
                                            fontSize = 13.ssp,
                                            fontFamily = monospaceMedium,
                                            fontWeight = FontWeight.W700
                                        ))
                                }
                                height(20)
                                CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                                    "Edit") {
                                    showModel.value=false
                                }
                                height(20)
                                OutlinedButton(
                                    onClick = { /* Your click action */ },
                                    shape = RoundedCornerShape(50.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDD0000)),
                                     modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(horizontal = 13.sdp,
                                            vertical = 10.sdp),
                                        text = "Delete ",
                                        color = Color(0xFFDD0000)
                                    )
                                }


                            }



                        }

                    }




                }


            }

        }


    }

}


@Composable
fun EventInformation(title: String, text:String, onClick:()->Unit) {

    OutlinedCard(modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.sdp))
        .clickable {
            onClick.invoke()
        },
        shape = RoundedCornerShape(10.sdp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xffF0F5FB),
        ),
        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
        )
    ) {

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 10.sdp,
                vertical = 10.sdp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Text(title,
                style = TextStyle(
                    color = Color(0xff0D0C0C),
                    fontSize = 13.ssp,
                    fontFamily = monospaceMedium,

                    )
            )

            Text(text,
                style = TextStyle(
                    color = Color(0xff0D0C0C),
                    fontSize = 13.ssp,
                    fontFamily = manropesemibold,

                    )
            )
            Icon(
                painter = painterResource(R.drawable.iv_editor),
                contentDescription = "",
                tint = Color.Unspecified
            )

        }

    }
}