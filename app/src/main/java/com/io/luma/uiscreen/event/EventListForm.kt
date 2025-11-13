package com.io.luma.uiscreen.event

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
fun EventListForm(navController: NavController) {
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
    val listOfDurations = listOf("10 m", "15 m", "30 m")
    var selectItem by rememberSaveable { mutableStateOf(0) }
    var day by rememberSaveable { mutableStateOf(listOfDays.get(0)) }
    var time by rememberSaveable { mutableStateOf("") }
    var isTypeDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var isDayDropdownExpanded by rememberSaveable { mutableStateOf(false) }
    var typeDecrption by rememberSaveable { mutableStateOf("") }
    var isChecked by rememberSaveable { mutableStateOf(false) }
    var shouldLumaCall by rememberSaveable { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    var date by rememberSaveable { mutableStateOf("") }



    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val days = calendar.get(Calendar.DAY_OF_MONTH)


    val timePickerDialog = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            // Format time like 09:05 AM
            val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            time = formattedTime
        },
        hour,
        minute,
        true // 24-hour format
    )

    val datePickerDialog = DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            // Format selected date as DD/MM/YYYY
            date =
                String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
        },
        year,
        month,
        days
    )

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
            Text("Day",
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
            )
            {
                OutlinedTextField(
                    value = date,
                    onValueChange = {
                        typeDecrption=it
                    },
                    enabled = false,
                    textStyle = TextStyle(
                        color = Color(0xff0D0C0C), // 👈 change your text color here
                        fontSize = 15.ssp,
                        fontFamily = monospaceRegular,
                        fontWeight = FontWeight.W400
                    ),

                    placeholder = {
                        Text(
                            "${13/11/2025}",
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
                            datePickerDialog.show()
                        }

                )
            }

            height(20)
            Text("Time",
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
                    value = time,
                    onValueChange = {
                        typeDecrption=it
                    },
                    textStyle = TextStyle(
                        color = Color(0xff0D0C0C), // 👈 change your text color here
                        fontSize = 15.ssp,
                        fontFamily = monospaceRegular,
                        fontWeight = FontWeight.W400
                    ),
                    enabled = false,

                    placeholder = {
                        Text(
                            "8:00 AM",
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
                            timePickerDialog.show()
                        }

                )


            }
            height(13)
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
            if (isChecked)
            {
                Column() {
                    Text("How long before?",
                        style = TextStyle(
                            color = Color(0xff0D0C0C),
                            fontSize = 13.ssp,
                            fontFamily = manropesemibold,
                            fontWeight = FontWeight.W600
                        )
                    )
                    height(6)
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        listOfDurations.forEachIndexed { index, item ->
                            TimeSlot(item,isSelected = selectItem==index) {
                                selectItem=index
                            }
                        }
                    }
                }
            }
            height(13)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth() // Toggle on row click
            )
            {
                Checkbox(
                    checked = shouldLumaCall,

                    onCheckedChange = { shouldLumaCall = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xf0000000), // Optional custom color
                        uncheckedColor = Color.Gray,
                        checkmarkColor = Color.White,
                    )
                )
                width(2)
                Text("Should Luma make a call?",
                    style = TextStyle(
                        color = Color(0xff0D0C0C),
                        fontSize = 15.ssp,
                        fontFamily = manropesemibold,
                        fontWeight = FontWeight.W600
                    )
                )



            }
            height(13)
            CustomButton(modifier = Modifier.fillMaxWidth(),
                "Save") {
                // navController.navigate(NavRoute.WeekalyRouting)
            }






        }
    }

}