package com.io.luma.uiscreen.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.io.luma.customcompose.width
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.monospaceMedium
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp


data class bottomNav(
    var icon:Int,
    var text: String
)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashBoard(navController: NavController) {
    val navItem= listOf(
        bottomNav(R.drawable.iv_home,"Home"),
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

                       }
                   }

                  }

    }

}
@Composable
fun HomeScreen(navController: NavController, values: PaddingValues) {
    val listOfNotification = listOf(
        "Amy had trouble finding her way today and asked for my help.",
        "Amy had trouble finding her way today and asked for my help."
    )

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
               Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp)) {
                   Image(
                       painter = painterResource(R.drawable.lumalifewide),
                       contentDescription = "",
                       modifier = Modifier
                           .height(33.sdp)
                           .align(Alignment.Center),
                   )
                   Image(
                       painter = painterResource(R.drawable.iv_notification),
                       contentDescription = "",
                       modifier = Modifier.align(Alignment.CenterEnd)
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
                       Text(
                           "Hello, John!",
                           style = TextStyle(
                               color = Color(0xff0D0C0C),
                               fontSize = 22.ssp,
                               fontFamily = manropebold,
                               fontWeight = FontWeight.W700
                           )
                       )
                   }

                   // PWD Section
                   item {
                       com.io.luma.customcompose.height(23)
                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           verticalAlignment = Alignment.CenterVertically,
                           horizontalArrangement = Arrangement.SpaceBetween
                       ) {
                           Text(
                               "Your PWD to care",
                               style = TextStyle(
                                   color = Color(0xff0D0C0C),
                                   fontSize = 15.ssp,
                                   fontFamily = manropebold,
                                   fontWeight = FontWeight.W700
                               )
                           )

                           Box(
                               modifier = Modifier
                                   .wrapContentSize()
                                   .border(
                                       width = 2.dp,
                                       color = Color(0xff0D0C0C),
                                       shape = RoundedCornerShape(13.sdp)
                                   )
                           ) {
                               Row(
                                   modifier = Modifier.padding(
                                       vertical = 13.sdp,
                                       horizontal = 13.sdp
                                   ),
                                   verticalAlignment = Alignment.CenterVertically
                               ) {
                                   Icon(
                                       painter = painterResource(R.drawable.iv_plus),
                                       contentDescription = "",
                                       tint = Color.Unspecified
                                   )
                                   width(3)
                                   Text(
                                       "Add PWD",
                                       style = TextStyle(
                                           color = Color(0xff0D0C0C),
                                           fontSize = 13.ssp,
                                           fontFamily = manropebold,
                                           fontWeight = FontWeight.W700
                                       )
                                   )
                               }
                           }
                       }
                   }

                   // Card (Amy Bishop)
                   item {
                       com.io.luma.customcompose.height(13)
                       Card(
                           modifier = Modifier.fillMaxWidth(),
                           colors = CardDefaults.cardColors(containerColor = Color.White),
                           shape = RoundedCornerShape(13.sdp),
                           border = BorderStroke(
                               1.dp,
                               Color(0xFF53537633F).copy(alpha = 0.2f)
                           ),
                           elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                       ) {
                           Row(
                               modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(horizontal = 13.sdp, vertical = 13.sdp),
                               verticalAlignment = Alignment.CenterVertically,
                               horizontalArrangement = Arrangement.SpaceBetween
                           ) {
                               Row(verticalAlignment = Alignment.CenterVertically) {
                                   Text(
                                       "Amy Bishop",
                                       style = TextStyle(
                                           color = Color(0xff0D0C0C),
                                           fontSize = 16.ssp,
                                           fontFamily = manropebold,
                                           fontWeight = FontWeight.W700
                                       )
                                   )
                                   width(6)
                                   Icon(
                                       painter = painterResource(R.drawable.iv_error),
                                       contentDescription = "",
                                       tint = Color.Unspecified
                                   )
                               }

                               Row(verticalAlignment = Alignment.CenterVertically) {
                                   Text(
                                       "Check info",
                                       style = TextStyle(
                                           color = Color(0xff0D0C0C),
                                           fontSize = 11.ssp,
                                           fontFamily = manropebold,
                                           fontWeight = FontWeight.W700
                                       )
                                   )
                                   width(6)
                                   Icon(
                                       painter = painterResource(R.drawable.iv_rightarrow),
                                       contentDescription = "",
                                       tint = Color.Unspecified
                                   )
                               }
                           }
                       }
                   }

                   // Logo section
                   item {
                       com.io.luma.customcompose.height(13)
                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.Center
                       ) {
                           Image(
                               painter = painterResource(R.drawable.lumalogo),
                               contentDescription = "",
                               modifier = Modifier.size(116.sdp)
                           )
                       }
                   }

                   // Icon row
                   item {
                       com.io.luma.customcompose.height(13)
                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.Center
                       ) {
                           Icon(
                               painter = painterResource(R.drawable.iv_calender),
                               contentDescription = "",
                               tint = Color.Unspecified
                           )
                           width(20)
                           Icon(
                               painter = painterResource(R.drawable.iv_mic),
                               contentDescription = "",
                               tint = Color.Unspecified
                           )
                       }
                   }

                   // Button
                   item {
                       com.io.luma.customcompose.height(20)
                       CustomButton(
                           modifier = Modifier.fillMaxWidth(),
                           text = "See Detailed Overview"
                       ) {}
                   }

                   // Divider
                   item {
                       com.io.luma.customcompose.height(20)
                       HorizontalDivider(color = Color(0xffDAE1FD), thickness = 1.dp)
                   }

                   // Updates header
                   item {
                       com.io.luma.customcompose.height(20)
                       Row(
                           modifier = Modifier.fillMaxWidth(),
                           horizontalArrangement = Arrangement.SpaceBetween
                       ) {
                           Text(
                               "Updates",
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


//@Composable
//fun HomeScreen(navController: NavController) {
//    var listOfNotification= listOf("Amy had trouble finding her way today and asked for my help.","Amy had trouble finding her way today and asked for my help.")
//
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .background(Color.White)) {
//
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(goldenYellow.copy(alpha = 0.6f), Color.Transparent),
//                            center = Offset(0f, 0f), // top-left corner
//                            radius = 600f // adjust based on your layout
//                        )
//                    )
//            )
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(skyblue.copy(alpha = 0.4f), Color.Transparent),
//                            center = Offset(900f, 800f), // adjust for your design
//                            radius = 700f
//                        )
//                    )
//            )
//
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .windowInsetsPadding(WindowInsets.statusBars)
//                    .verticalScroll(rememberScrollState())
//                    .padding(horizontal = 13.sdp)
//            ) {
//                com.io.luma.customcompose.height(30)
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                )
//                {
//
//                    Image(
//                        painter = painterResource(R.drawable.lumalifewide),
//                        contentDescription = "",
//                        modifier = Modifier
//                            .height(33.sdp)
//                            .align(Alignment.Center),
//
//                        )
//
//                    Image(
//                        painter = painterResource(R.drawable.iv_notification),
//                        contentDescription = "",
//                        modifier = Modifier.align(Alignment.CenterEnd)
//
//
//                    )
//
//                }
//                Column(modifier = Modifier
//                    .fillMaxSize()
//                    )
//                {
//                    com.io.luma.customcompose.height(13)
//                    Text(
//                        "Hello, John!",
//                        style = TextStyle(
//                            color = Color(0xff0D0C0C),
//                            fontSize = 22.ssp,
//                            fontFamily = manropebold,
//                            fontWeight = FontWeight.W700
//                        )
//                    )
//                    com.io.luma.customcompose.height(23)
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    )
//                    {
//                        Text(
//                            "Your PWD to care",
//                            style = TextStyle(
//                                color = Color(0xff0D0C0C),
//                                fontSize = 15.ssp,
//                                fontFamily = manropebold,
//                                fontWeight = FontWeight.W700
//                            )
//                        )
//
//                        Box(
//                            modifier = Modifier
//                                .wrapContentSize()
//                                .border(
//                                    width = 2.dp, color = Color(0xff0D0C0C),
//                                    shape = RoundedCornerShape(13.sdp)
//                                )
//                        ) {
//                            Row(
//                                modifier = Modifier.padding(
//                                    vertical = 13.sdp,
//                                    horizontal = 13.sdp
//                                ),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(
//                                    painter = painterResource(R.drawable.iv_plus),
//                                    contentDescription = "",
//                                    tint = Color.Unspecified
//                                )
//                                width(3)
//                                Text(
//                                    "Add PWD",
//                                    style = TextStyle(
//                                        color = Color(0xff0D0C0C),
//                                        fontSize = 13.ssp,
//                                        fontFamily = manropebold,
//                                        fontWeight = FontWeight.W700
//                                    )
//                                )
//                            }
//                        }
//                    }
//                    com.io.luma.customcompose.height(13)
//
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = CardDefaults.cardColors(
//                            containerColor = Color.White,
//                        ),
//                        shape = RoundedCornerShape(13.sdp),
//                        border = BorderStroke(
//                            1.dp, Color(0xFF53537633F).copy(alpha = 0.2f) // 20% opacity
//                        ),
//                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
//                    )
//                    {
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(
//                                    horizontal = 13.sdp,
//                                    vertical = 13.sdp
//                                ),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        )
//                        {
//
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            )
//                            {
//
//                                Text(
//                                    "Amy Bishop",
//                                    style = TextStyle(
//                                        color = Color(0xff0D0C0C),
//                                        fontSize = 16.ssp,
//                                        fontFamily = manropebold,
//                                        fontWeight = FontWeight.W700
//                                    )
//                                )
//                                width(6)
//
//                                Icon(
//                                    painter = painterResource(R.drawable.iv_error),
//                                    contentDescription = "",
//                                    tint = Color.Unspecified
//                                )
//                            }
//
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically
//                            )
//                            {
//
//                                Text(
//                                    "Check info",
//                                    style = TextStyle(
//                                        color = Color(0xff0D0C0C),
//                                        fontSize = 11.ssp,
//                                        fontFamily = manropebold,
//                                        fontWeight = FontWeight.W700
//                                    )
//                                )
//                                width(6)
//
//                                Icon(
//                                    painter = painterResource(R.drawable.iv_rightarrow),
//                                    contentDescription = "",
//                                    tint = Color.Unspecified
//                                )
//                            }
//                        }
//
//                    }
//                    com.io.luma.customcompose.height(13)
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    )
//                    {
//
//                        Image(
//                            painter = painterResource(R.drawable.lumalogo),
//                            contentDescription = "",
//                            modifier = Modifier.size(116.sdp)
//
//                        )
//
//                    }
//                    com.io.luma.customcompose.height(13)
//
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center
//                    )
//                    {
//
//                        Icon(
//                            painter = painterResource(R.drawable.iv_calender),
//                            contentDescription = "",
//                            tint = Color.Unspecified
//                        )
//                        width(20)
//                        Icon(
//                            painter = painterResource(R.drawable.iv_mic),
//                            contentDescription = "",
//                            tint = Color.Unspecified
//                        )
//
//                    }
//                    com.io.luma.customcompose.height(20)
//
//                    CustomButton(
//                        modifier = Modifier.fillMaxWidth(),
//                        text = "See Detailed Overview"
//                    ) {
//
//                    }
//                    com.io.luma.customcompose.height(20)
//
//                    HorizontalDivider(
//                        color = Color(0xffDAE1FD),
//                        thickness = 1.dp
//                    )
//                    com.io.luma.customcompose.height(20)
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    )
//                    {
//                        Text(
//                            "Updates",
//                            style = TextStyle(
//                                color = Color(0xff0D0C0C),
//                                fontSize = 15.ssp,
//                                fontFamily = manropebold,
//                                fontWeight = FontWeight.W700
//                            )
//                        )
//                        Text(
//                            "See all",
//                            style = TextStyle(
//                                color = Color(0xff2951E0),
//                                fontSize = 11.ssp,
//                                fontFamily = manropebold,
//                                fontWeight = FontWeight.W700,
//                                textDecoration = TextDecoration.Underline
//                            )
//                        )
//                    }
//                    com.io.luma.customcompose.height(20)
//                    listOfNotification.forEach {
//                        notificationItem(it){}
//                    }
//
//
//                }
//
//            }
//
//
//        }
//
//
//    }
//}

@Composable
fun notificationItem(text: String,onClick:()->Unit) {

    Column(modifier = Modifier
        .wrapContentSize(),

    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        )
        {
            Text(text,
                modifier = Modifier.weight(1f),
                style = TextStyle(
                    color = Color(0xff0D0C0C),
                    fontSize = 13.ssp,
                    fontFamily = monospaceMedium,
                    fontWeight = FontWeight.W700
                ))

            Icon(
                painter = painterResource(R.drawable.iv_rightarrow),
                contentDescription = "",
                tint = Color.Unspecified
            )


        }
        com.io.luma.customcompose.height(10)
        HorizontalDivider(
            color = Color(0xffDAE1FD),
            thickness = 1.dp
        )

    }

}