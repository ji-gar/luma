package com.io.luma.uiscreen.dashboard

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.width
import com.io.luma.model.CarerDashBoardResponseModel
import com.io.luma.navroute.NavRoute
import com.io.luma.network.Resource
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.monospaceMedium
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
import com.io.luma.viewmodel.CarerDashboardViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PatientScreen(navController: NavController) {
    val navItem= listOf(
        bottomNav(R.drawable.iv_home,"Home"),
        bottomNav(R.drawable.iv_add,""),
        bottomNav(R.drawable.iv_message,"Feedback"))

    var bottomSelectedIndex by remember { mutableStateOf(0) }

    Box(modifier = Modifier.fillMaxSize()){


        Scaffold(
            modifier = Modifier.fillMaxSize(),

            bottomBar = {
                Box {
                    NavigationBar(
                        containerColor = Color.White,
                        tonalElevation = 1.dp,
                    )
                    {


                        navItem.forEachIndexed { index, item ->

                            NavigationBarItem(
                                selected = bottomSelectedIndex == index,
                                onClick = { bottomSelectedIndex = index },
                                label = {
                                    Text(
                                        item.text,
                                        style = TextStyle(
                                            color =
                                                if (index == 1) {               // center icon → always fixed color
                                                    Color(0xff4AAFB6)
                                                } else if (bottomSelectedIndex == index) {
                                                    Color(0xff4AAFB6)           // selected
                                                } else {
                                                    Color(0xffA8AAB8)           // unselected
                                                },
                                            fontSize = 12.ssp,
                                            fontFamily = monospaceRegular,
                                            fontWeight = FontWeight.W400
                                        )
                                    )
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = "",
                                        tint = if (index == 1) {               // center icon → always fixed color
                                            Color.Unspecified
                                        } else if (bottomSelectedIndex == index) {
                                            Color(0xff4AAFB6)           // selected
                                        } else {
                                            Color(0xffA8AAB8)           // unselected
                                        }
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.Transparent
                                )
                            )


                        }
                    }


                }
            },


            ) {
            when(bottomSelectedIndex){
                0->{
                    PatientHomeScreen(navController,it,bottomSelectedIndex)
                }

            }

        }

    }

}
@Composable
fun PatientHomeScreen(
    navController: NavController,
    values: PaddingValues,
    index: Int = 0,
    viewModel: CarerDashboardViewModel = viewModel()
) {
    var selected by remember { mutableStateOf(0) }
    val date = listOf("11\nMon", "12\nTue", "13\nWed", "14\nThu", "16\nFri", "17\nSat", "18\nSun")

    // Observe the API response
    val carerDashboard by viewModel.createUser.collectAsState()
    var carerName by remember { mutableStateOf("Amy Bishop") } // Default name

    // Call API when screen loads
    LaunchedEffect(Unit) {
        viewModel.getCarerDashBoard()
    }

    // Update carer name when data is loaded
    LaunchedEffect(carerDashboard) {
        when (carerDashboard) {
            is Resource.Success -> {
                val data = (carerDashboard as Resource.Success<CarerDashBoardResponseModel>).data
                carerName = data.data?.fullName ?: "Amy Bishop"
            }
            is Resource.Error -> {
                // Handle error if needed
                carerName = "Amy Bishop" // Keep default on error
            }
            else -> { /* Loading or null state */ }
        }
    }

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
            Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
                com.io.luma.customcompose.height(30)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 13.sdp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(R.drawable.iv_carerricon),
                        contentDescription = "",
                        modifier = Modifier.clickable { navController.navigate(NavRoute.CarrersContactList) }
                    )

                    // Display carer name from API
                    Text(
                        carerName,
                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 22.ssp,
                            fontFamily = manropebold,
                            fontWeight = FontWeight.W700
                        )
                    )

                    Image(
                        painter = painterResource(R.drawable.iv_notification),
                        contentDescription = "",
                        modifier = Modifier.clickable { navController.navigate(NavRoute.NotificationList) }
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(paddingValues = values),
                    contentPadding = PaddingValues(horizontal = 13.sdp)
                ) {
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
                                tint = Color.Unspecified,
                                modifier = Modifier.clickable { navController.navigate(NavRoute.SchdualScreen) }
                            )
                            width(20)
                            Icon(
                                painter = painterResource(R.drawable.iv_mic),
                                contentDescription = "",
                                tint = Color.Unspecified
                            )
                            width(20)
                            Icon(
                                painter = painterResource(R.drawable.iv_heart),
                                contentDescription = "",
                                tint = Color.Unspecified,
                                modifier = Modifier.clickable { navController.navigate(NavRoute.CareerCampingScreen) }
                            )
                        }
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
                            Icon(
                                painter = painterResource(R.drawable.iv_leftarrow),
                                contentDescription = "",
                                tint = Color.Unspecified
                            )
                            Text(
                                "August 11-17, 2025",
                                style = TextStyle(
                                    color = Color(0xff0D0C0C),
                                    fontSize = 15.ssp,
                                    fontFamily = manropebold,
                                    fontWeight = FontWeight.W700
                                )
                            )
                            Icon(
                                painter = painterResource(R.drawable.iv_rightarrow),
                                contentDescription = "",
                                tint = Color.Unspecified
                            )
                        }
                    }

                    item {
                        com.io.luma.customcompose.height(14)
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(13.sdp),
                            contentPadding = PaddingValues(horizontal = 10.sdp)
                        ) {
                            itemsIndexed(date) { index, item ->
                                DateBox(item, index == selected) {
                                    selected = index
                                }
                            }
                        }
                    }

                    item {
                        com.io.luma.customcompose.height(20)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                Icon(
                                    painter = painterResource(R.drawable.iv_conversion),
                                    contentDescription = "",
                                    tint = Color.Unspecified
                                )
                                width(6)
                                Column {
                                    Text(
                                        "Conversations",
                                        style = TextStyle(
                                            color = Color(0xff0D0C0C),
                                            fontSize = 15.ssp,
                                            fontFamily = manropebold,
                                            fontWeight = FontWeight.W700
                                        )
                                    )
                                    Text(
                                        "Minutes",
                                        style = TextStyle(
                                            color = Color(0xff4C4C50),
                                            fontSize = 11.ssp,
                                        )
                                    )
                                }
                            }
                            Text(
                                "126",
                                style = TextStyle(
                                    color = Color(0xff0D0C0C),
                                    fontSize = 15.ssp,
                                    fontFamily = manropebold,
                                    fontWeight = FontWeight.W700
                                )
                            )
                        }
                    }

                    item {
                        com.io.luma.customcompose.height(20)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                Icon(
                                    painter = painterResource(R.drawable.iv_reminders),
                                    contentDescription = "",
                                    tint = Color.Unspecified
                                )
                                width(6)
                                Column {
                                    Text(
                                        "Reminders",
                                        style = TextStyle(
                                            color = Color(0xff0D0C0C),
                                            fontSize = 15.ssp,
                                            fontFamily = manropebold,
                                            fontWeight = FontWeight.W700
                                        )
                                    )
                                    Text(
                                        "Acknowledged",
                                        style = TextStyle(
                                            color = Color(0xff4C4C50),
                                            fontSize = 11.ssp,
                                        )
                                    )
                                }
                            }
                            Text(
                                "40",
                                style = TextStyle(
                                    color = Color(0xff0D0C0C),
                                    fontSize = 15.ssp,
                                    fontFamily = manropebold,
                                    fontWeight = FontWeight.W700
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    if (index == 1) {
        Box(modifier = Modifier.fillMaxSize().background(color = Color(0xff00000080))) {
            Box(modifier = Modifier.fillMaxWidth().background(color = Color.White).height(230.dp)) {
            }
        }
    }
}
//@Composable
//fun  PatientHomeScreen(navController: NavController, values: PaddingValues,index:Int=0) {
//
//    var selected by remember { mutableStateOf(0) }
//    val date = listOf("11\nMon", "12\nTue", "13\nWed", "14\nThu", "16\nFri", "17\nSat", "18\nSun")
//
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        // Background layers
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.White)
//        ) {
//            // Top-left gradient
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(goldenYellow.copy(alpha = 0.6f), Color.Transparent),
//                            center = Offset(0f, 0f),
//                            radius = 600f
//                        )
//                    )
//            )
//
//            // Bottom-right gradient
//            Box(
//                modifier = Modifier
//                    .matchParentSize()
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(skyblue.copy(alpha = 0.4f), Color.Transparent),
//                            center = Offset(900f, 800f),
//                            radius = 700f
//                        )
//                    )
//            )
//
//            // Main scrollable content
//            Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {
//                com.io.luma.customcompose.height(30)
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 13.sdp),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    Image(
//                        painter = painterResource(R.drawable.iv_carerricon),
//                        contentDescription = "",
//
//                    )
//                    Text(
//                        "Amy Bishop",
//                        style = TextStyle(
//                            color = Color(0xff0D0C0C),
//                            fontSize = 22.ssp,
//                            fontFamily = manropebold,
//                            fontWeight = FontWeight.W700
//                        )
//                    )
//
//                    Image(
//                        painter = painterResource(R.drawable.iv_notification),
//                        contentDescription = "",
//                    )
//                }
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .weight(1f)
//                        .windowInsetsPadding(WindowInsets.statusBars)
//                        .padding(paddingValues = values),
//                    contentPadding = PaddingValues(horizontal = 13.sdp)
//                )
//                {
//
//
//                    // Logo section
//                    item {
//                        com.io.luma.customcompose.height(13)
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Image(
//                                painter = painterResource(R.drawable.lumalogo),
//                                contentDescription = "",
//                                modifier = Modifier.size(116.sdp)
//                            )
//                        }
//                    }
//
//                    // Icon row
//                    item {
//                        com.io.luma.customcompose.height(13)
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.Center
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.iv_calender),
//                                contentDescription = "",
//                                tint = Color.Unspecified,
//                                modifier = Modifier.clickable { navController.navigate(NavRoute.SchdualScreen) }
//                            )
//                            width(20)
//                            Icon(
//                                painter = painterResource(R.drawable.iv_mic),
//                                contentDescription = "",
//                                tint = Color.Unspecified
//                            )
//                            width(20)
//                            Icon(
//                                painter = painterResource(R.drawable.iv_heart),
//                                contentDescription = "",
//                                tint = Color.Unspecified,
//                                modifier = Modifier.clickable { navController.navigate(NavRoute.CareerCampingScreen) }
//                            )
//                        }
//                    }
//
//
//                    // Divider
//                    item {
//                        com.io.luma.customcompose.height(20)
//                        HorizontalDivider(color = Color(0xffDAE1FD), thickness = 1.dp)
//                    }
//
//                    // Updates header
//                    item {
//                        com.io.luma.customcompose.height(20)
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Icon(
//                                painter = painterResource(R.drawable.iv_leftarrow),
//                                contentDescription = "",
//                                tint = Color.Unspecified
//                            )
//                            Text(
//                                "August 11-17, 2025",
//                                style = TextStyle(
//                                    color = Color(0xff0D0C0C),
//                                    fontSize = 15.ssp,
//                                    fontFamily = manropebold,
//                                    fontWeight = FontWeight.W700
//                                )
//                            )
//
//                            Icon(
//                                painter = painterResource(R.drawable.iv_rightarrow),
//                                contentDescription = "",
//                                tint = Color.Unspecified
//                            )
//
//                        }
//                    }
//                    item {
//                        com.io.luma.customcompose.height(14)
//                        LazyRow(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.spacedBy(13.sdp),
//                            contentPadding = PaddingValues(horizontal = 10.sdp)
//                        ) {
//                            itemsIndexed(date) { index, item ->
//                                DateBox(item, index == selected) {
//                                    selected = index
//                                }
//                            }
//                        }
//
//                    }
//
//                    item {
//                        com.io.luma.customcompose.height(20)
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        )
//                        {
//                           Row() {
//                               Icon(
//                                   painter = painterResource(R.drawable.iv_conversion),
//                                   contentDescription = "",
//                                   tint = Color.Unspecified
//                               )
//                               width(6)
//                               Column {
//                                   Text(
//                                       "Conversations",
//                                       style = TextStyle(
//                                           color = Color(0xff0D0C0C),
//                                           fontSize = 15.ssp,
//                                           fontFamily = manropebold,
//                                           fontWeight = FontWeight.W700
//                                       )
//                                   )
//                                   Text(
//                                       "Minutes",
//                                       style = TextStyle(
//                                           color = Color(0xff4C4C50),
//                                           fontSize = 11.ssp,
//                                       )
//                                   )
//
//                               }
//                           }
//
//                            Text(
//                                "126",
//                                style = TextStyle(
//                                    color = Color(0xff0D0C0C),
//                                    fontSize = 15.ssp,
//                                    fontFamily = manropebold,
//                                    fontWeight = FontWeight.W700
//                                )
//                            )
//                        }
//
//                        // Notifications list
//
//                    }
//
//                    item {
//                        com.io.luma.customcompose.height(20)
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        )
//                        {
//                            Row() {
//                                Icon(
//                                    painter = painterResource(R.drawable.iv_reminders),
//                                    contentDescription = "",
//                                    tint = Color.Unspecified
//                                )
//                                width(6)
//                                Column {
//                                    Text(
//                                        "Reminders",
//                                        style = TextStyle(
//                                            color = Color(0xff0D0C0C),
//                                            fontSize = 15.ssp,
//                                            fontFamily = manropebold,
//                                            fontWeight = FontWeight.W700
//                                        )
//                                    )
//                                    Text(
//                                        "Acknowledged",
//                                        style = TextStyle(
//                                            color = Color(0xff4C4C50),
//                                            fontSize = 11.ssp,
//                                        )
//                                    )
//
//                                }
//                            }
//
//                            Text(
//                                "40",
//                                style = TextStyle(
//                                    color = Color(0xff0D0C0C),
//                                    fontSize = 15.ssp,
//                                    fontFamily = manropebold,
//                                    fontWeight = FontWeight.W700
//                                )
//                            )
//                        }
//
//                        // Notifications list
//
//                    }
//                }
//            }
//
//
//
//        }
//    }
//
//    if (index==1)
//    {
//        Box(modifier = Modifier.fillMaxSize().background(color = Color(0xff00000080))) {
//
//
//
//            Box(modifier = Modifier.fillMaxWidth().background(color = Color.White).height(230.dp)) {
//
//
//            }
//        }
//    }
//
//
//}
@Composable
fun DateBox(date: String, index: Boolean, onClick: () -> Unit) {

    Box(
        modifier = Modifier
            .wrapContentSize()
            .width(55.sdp)
            .background(
                color = if (index) Color.Black else Color.Transparent,
                shape = RoundedCornerShape(10.sdp)
            )
            .border(
                width = 1.dp,
                color = if (index) Color.Black else Color(0xff93969B),
                shape = RoundedCornerShape(10.sdp)
            )
            .clickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = date,

            style = TextStyle(
                color = if (index) Color.White else Color(0xff93969B),
                fontSize = 13.ssp,
                fontFamily = monospaceMedium,
                fontWeight = FontWeight.W700
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.sdp, vertical = 17.sdp)
        )

    }

}

