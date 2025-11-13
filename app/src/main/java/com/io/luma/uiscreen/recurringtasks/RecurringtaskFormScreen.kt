package com.io.luma.uiscreen.recurringtasks

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.height
import com.io.luma.customcompose.width
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.monospaceMedium
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
import com.io.luma.uiscreen.weeaklySchdual.TimeSlot
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import java.util.Calendar



@Composable
fun RecurringtaskFormScreen(navController: NavController) {
    var context=LocalContext.current
    var type by rememberSaveable { mutableStateOf("") }
    var listOfType=listOf<String>("Daily Routine","Weekly Routine","Monthly Routine")
    val listOfDays = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )
    val listOfDurations = listOf("10 m", "15 m", "30 m", "45 m", "1 h", "3 h")

    var selectItem by rememberSaveable { mutableStateOf(0) }
    var isTypeDropdownExpanded by rememberSaveable { mutableStateOf(false) }

    var typeDecrption by rememberSaveable { mutableStateOf("") }
    var isChecked by rememberSaveable { mutableStateOf(false) }






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
            .windowInsetsPadding(WindowInsets.statusBars))
        {

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

                Text("Add Event",

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
                    .clickable {
                        isTypeDropdownExpanded = true
                    }
            ) {
                OutlinedTextField(
                    value = type,
                    enabled = false,
                    onValueChange = {},
                    readOnly = true,
                    textStyle = TextStyle(
                        color = Color(0xff0D0C0C), // 👈 change your text color here
                        fontSize = 15.ssp,
                        fontFamily = monospaceRegular,
                        fontWeight = FontWeight.W400
                    ),
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
                        unfocusedBorderColor = Color(0xff000000),
                        disabledBorderColor = Color(0xff000000)
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

            height(20)
            Text("Description",
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
                    .clickable {
                        isTypeDropdownExpanded = true
                    }
            ) {
                OutlinedTextField(
                    value = typeDecrption,
                    onValueChange = {
                        typeDecrption=it
                    },
                    textStyle = TextStyle(
                        color = Color(0xff0D0C0C), // 👈 change your text color here
                        fontSize = 15.ssp,
                        fontFamily = monospaceRegular,
                        fontWeight = FontWeight.W400
                    ),

                    placeholder = {
                        Text(
                            "Type description here",
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
                        unfocusedBorderColor = Color(0xff000000),
                        disabledBorderColor = Color(0xff000000)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()

                )


            }

            height(20)

            Text("How often to remind?",
                style = TextStyle(
                    color = Color(0xff0D0C0C),
                    fontSize = 13.ssp,
                    fontFamily = manropesemibold,
                    fontWeight = FontWeight.W600
                )
            )
            height(6)
            FlowRow(
                maxItemsInEachRow = 3,
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.sdp)
            ) {
                listOfDurations.forEachIndexed { index, item ->
                    TimeSlot(
                        text = item,
                        isSelected = selectItem == index
                    ) {
                        selectItem = index
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth() // Toggle on row click
            )
            {
                Checkbox(
                    checked = isChecked,

                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xf0000000), // Optional custom color
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                    )
                )
                width(2)
                Text("Should Luma remind beforehand?",
                    style = TextStyle(
                        color = Color(0xff0D0C0C),
                        fontSize = 15.ssp,
                        fontFamily = manropesemibold,
                        fontWeight = FontWeight.W600
                    )
                )



            }

            height(13)







        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.BottomCenter)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = 25.sdp, start = 13.sdp, end = 13.sdp)
        ){
            CustomButton(modifier = Modifier.fillMaxWidth(),
                "Save") {
                // navController.navigate(NavRoute.WeekalyRouting)
            }
        }
    }

}