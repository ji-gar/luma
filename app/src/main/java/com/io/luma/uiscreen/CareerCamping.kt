package com.io.luma.uiscreen

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.height
import com.io.luma.customcompose.width
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.monospaceMedium
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
import com.io.luma.uiscreen.dashboard.bottomNav
import com.io.luma.uiscreen.dashboard.notificationItem
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp




@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CareerCamping(navController: NavController) {
    val navItem= listOf(
        bottomNav(R.drawable.iv_home,"Home"),
        bottomNav(R.drawable.iv_add,""),
        bottomNav(R.drawable.iv_message,"Feedback"))

    var bottomSelectedIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()){


        Scaffold(modifier = Modifier.fillMaxSize(),
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 1.dp,
                ) {


                    navItem.forEachIndexed { index, item ->

                        NavigationBarItem(
                            selected = bottomSelectedIndex == index,
                            onClick = { bottomSelectedIndex = index },
                            label = { Text(item.text,
                                style = TextStyle(
                                    color = if (bottomSelectedIndex == index) Color(0xff4AAFB6) else Color(0xffA8AAB8),
                                    fontSize = 12.ssp,
                                    fontFamily = monospaceRegular,
                                    fontWeight = FontWeight.W400
                                )) },
                            icon = { androidx.compose.material3.Icon(painter = painterResource(id = item.icon), contentDescription = "",
                                tint = if (bottomSelectedIndex == index) Color(0xff4AAFB6) else Color(0xffA8AAB8)) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color.Transparent
                            )
                        )


                    }
                }
            }

        ) {
            when(bottomSelectedIndex){
                0->{
                    HomeScreen(navController,it)
                }
                1->{
                    FeedBackScreen(navController)
                }
            }

        }

    }

}
@Composable
fun HomeScreen(navController: NavController, values: PaddingValues) {
    val listOfNotification = listOf(
        "Amy had trouble finding her way today and asked for my help.",
        "Amy had trouble finding her way today and asked for my help.",
        "Amy had trouble finding her way today and asked for my help.",
        "Amy had trouble finding her way today and asked for my help."
    )
    val listOfDurations = listOf("10 m", "15 m", "30 m", "45 m", "60 m")


    Box(modifier = Modifier.fillMaxSize()) {

        // Background layers
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Top-left gradient
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(goldenYellow.copy(alpha = 0.6f), Color.Transparent),
                            center = Offset(0f, 0f),
                            radius = 600f
                        )
                    )
            )

            // Bottom-right gradient
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(skyblue.copy(alpha = 0.4f), Color.Transparent),
                            center = Offset(900f, 800f),
                            radius = 700f
                        )
                    )
            )

            // Main scrollable content
            Column(modifier = Modifier.fillMaxSize()) {
                com.io.luma.customcompose.height(30)
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 13.sdp)) {
                    Image(
                        painter = painterResource(R.drawable.iv_smallleftarrow),
                        contentDescription = "",
                        modifier = Modifier.align(Alignment.CenterStart)
                    )
                   Text("Amy Bishop",
                       modifier = Modifier.align(Alignment.Center),
                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 22.ssp,
                            fontFamily = manropebold,
                            fontWeight = FontWeight.W700
                        )
                    )

                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(paddingValues = values),
                    contentPadding = PaddingValues(horizontal = 13.sdp)
                )
                {



                    // Greeting
                    item {
                        com.io.luma.customcompose.height(13)
                        Row(modifier = Modifier.fillMaxWidth(),

                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(

                            ) {
                                Image(
                                    painter = painterResource(R.drawable.lumalogo),
                                    contentDescription = "",
                                    modifier = Modifier.size(116.sdp)
                                )

                            }
                            width(10)
                            Column(
                            )
                            {

                                Row(
                                ) {
                                    Text(
                                        "Luma Gender:",
                                        style = TextStyle(
                                            color = Color(0xff4C4C50),
                                            fontSize = 12.ssp,
                                            fontFamily = monospaceRegular,
                                            fontWeight = FontWeight.W400
                                        )
                                    )
                                    Text(
                                        "Female",
                                        style = TextStyle(
                                            color = Color(0xff0D0C0C),
                                            fontSize = 12.ssp,
                                            fontFamily = manropebold,
                                            fontWeight = FontWeight.W400
                                        )
                                    )
                                }
                                height(13)
                                Row(
                                ) {
                                    Text(
                                        "Localisation:",
                                        style = TextStyle(
                                            color = Color(0xff4C4C50),
                                            fontSize = 12.ssp,
                                            fontFamily = monospaceRegular,
                                            fontWeight = FontWeight.W400
                                        )
                                    )
                                    Text(
                                        "London",
                                        style = TextStyle(
                                            color = Color(0xff0D0C0C),
                                            fontSize = 12.ssp,
                                            fontFamily = manropebold,
                                            fontWeight = FontWeight.W400
                                        )
                                    )
                                }
                                height(13)
                                Row(
                                ) {
                                    Text(
                                        "User known as:",
                                        style = TextStyle(
                                            color = Color(0xff4C4C50),
                                            fontSize = 12.ssp,
                                            fontFamily = monospaceRegular,
                                            fontWeight = FontWeight.W400
                                        )
                                    )
                                    Text(
                                        "London",
                                        style = TextStyle(
                                            color = Color(0xff0D0C0C),
                                            fontSize = 12.ssp,
                                            fontFamily = manropebold,
                                            fontWeight = FontWeight.W400
                                        )
                                    )
                                }

                            }



                        }
                    }

                    // PWD Section

                    // Icon row

                    // Button


                    // Divider
                    item {
                        com.io.luma.customcompose.height(20)
                        Text(
                            "Preferences",
                            style = TextStyle(
                                color = Color(0xff0D0C0C),
                                fontSize = 15.ssp,
                                fontFamily = manropebold,
                                fontWeight = FontWeight.W700
                            )
                        )
                        com.io.luma.customcompose.height(20)

                        LazyRow(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(13.sdp),
                            contentPadding = PaddingValues(horizontal = 10.sdp)
                        ) {
                           listOfDurations.forEachIndexed { index, item ->
                               items(5) {
                                   CardInformation()
                               }
                           }
                        }
                    }

                    // Updates header
                    item {
                        com.io.luma.customcompose.height(20)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Treasured memories",
                                style = TextStyle(
                                    color = Color(0xff0D0C0C),
                                    fontSize = 15.ssp,
                                    fontFamily = manropebold,
                                    fontWeight = FontWeight.W700
                                )
                            )
                            Text(
                                "See all",
                                style = TextStyle(
                                    color = Color(0xff2951E0),
                                    fontSize = 11.ssp,
                                    fontFamily = manropebold,
                                    fontWeight = FontWeight.W700,
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        }
                    }

                    // Notifications list
                    items(listOfNotification) { notification ->
                        com.io.luma.customcompose.height(20)
                        notificationItem(notification) {}
                    }
                }
            }
        }
    }
}


@Composable
fun FeedBackScreen(navController: NavController) {

    // Background layers
    // Top-left gradient
    Box(
        modifier = Modifier
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
            )
    ){


        Image(painter = painterResource(R.drawable.helloluma),
            contentDescription = "",
            modifier = Modifier.fillMaxSize()
        )

    }

    // Bottom-right gradient

}


@Composable
fun CardInformation() {


    Card(modifier = Modifier.fillMaxWidth().width(208.sdp).clip(RoundedCornerShape(10.sdp)),
        shape = RoundedCornerShape(10.sdp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
        )
    ) {

        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp,
            vertical = 13.sdp
        )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Text(
                    "Likes",
                    style = TextStyle(
                        color = Color(0xff0D0C0C),
                        fontSize = 15.ssp,
                        fontFamily = monospaceMedium,
                        fontWeight = FontWeight.W700
                    )
                )
               Row(
               ) {

                   Text(
                       "See all",
                       style = TextStyle(
                           color = Color(0xff0D0C0C),
                           fontSize = 12.ssp,
                           fontFamily = monospaceMedium,
                           fontWeight = FontWeight.W400
                       )
                   )
                   Icon(
                       painter = painterResource(R.drawable.iv_smallrightarrow),
                       contentDescription = "",
                       tint = Color.Unspecified
                   )
               }

            }
            height(13)
            Icon(
                painter = painterResource(R.drawable.iv_thumpliked),
                contentDescription = "",
                tint = Color.Unspecified
            )
            height(13)
            BulletRow("•", "Gardening")
            height(5)
            BulletRow("•", "Gardening")
            height(5)
            BulletRow("•", "Gardening")

        }
    }
}

@Composable
fun BulletRow(bullet: String = "•", text: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text(
            text = bullet,
            modifier = Modifier.padding(end = 8.dp),
            fontSize = 14.ssp
        )
        Text(
            text = text,
            style = TextStyle(
                color = Color(0xff0D0C0C),
                fontSize = 13.ssp,
                fontFamily = manropesemibold,
                fontWeight = FontWeight.W600
            ),

        )
    }
}

