package com.io.luma.uiscreen.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
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

        }

    }

}