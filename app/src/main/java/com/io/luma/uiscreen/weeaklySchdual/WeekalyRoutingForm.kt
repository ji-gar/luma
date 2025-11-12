package com.io.luma.uiscreen.weeaklySchdual

import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.height
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.monospaceMedium
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun WeekalyRoutingForm(navController: NavController) {
    var type by rememberSaveable { mutableStateOf("") }
    var listOfType=listOf<String>("Daily Routine","Weekly Routine","Monthly Routine")
    var isTypeDropdownExpanded by rememberSaveable { mutableStateOf(false) }
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


        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 13.sdp)
            .windowInsetsPadding(WindowInsets.statusBars)){

            height(30)
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            )
            {

                Icon(

                    painter = painterResource(R.drawable.iv_smallleftarrow),
                    contentDescription = "",
                    tint = Color.Unspecified
                )

                Text("Weekly Routine",

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
            height(20)
            Text("Schedule type",
                style = TextStyle(
                    color = Color(0xff0D0C0C),
                    fontSize = 13.ssp,
                    fontFamily = manropesemibold,
                    fontWeight = FontWeight.W600
                )
            )
            height(6)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(6.sdp))
            ) {
                OutlinedTextField(
                    value = type,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                isTypeDropdownExpanded = true
                            }
                        )
                    },
                    placeholder = {
                        Text(
                            "Daily Routine",
                            style = TextStyle(
                                color = Color(0xff4C4C50),
                                fontSize = 13.ssp,
                                fontFamily = monospaceRegular,
                                fontWeight = FontWeight.W400
                            )
                        )
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff000000),
                        unfocusedBorderColor = Color(0xff000000)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            Log.d("Type===============", "Clicked Field ✅")
                            isTypeDropdownExpanded = true
                        }
                )

                DropdownMenu(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White),
                    expanded = isTypeDropdownExpanded,
                    onDismissRequest = { isTypeDropdownExpanded = false }
                ) {
                    listOfType.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = item,
                                    style = TextStyle(
                                        color = Color(0xff0D0C0C),
                                        fontSize = 13.ssp,
                                        fontFamily = monospaceRegular,
                                        fontWeight = FontWeight.W400
                                    )
                                )
                            },
                            onClick = {
                                type = item
                                isTypeDropdownExpanded = false
                            }
                        )
                    }
                }
            }




        }
    }
    
}