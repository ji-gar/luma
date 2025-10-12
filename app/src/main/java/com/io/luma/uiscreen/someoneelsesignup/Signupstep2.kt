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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.ui.theme.verandaRegular
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.w3c.dom.Text

@Composable
fun SignupStep2(navController: NavController) {
    var firstName=remember { mutableStateOf("") }
    var lasttName=remember { mutableStateOf("") }
    var email=remember { mutableStateOf("") }
    var password=remember { mutableStateOf("") }
    var confirmPassword=remember { mutableStateOf("") }
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
                ).imePadding()
            )
            {

                Text("Finishing signing up",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Center
                )
                com.io.luma.customcompose.height(20)
               rowHeader("Please Enter Name")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                   modifier = Modifier.fillMaxWidth(),
                    value =firstName.value,
                    onValueChange = {
                       firstName.value=it
                    },
                    placeholder = {
                        Text("Enter your First Name",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )
                com.io.luma.customcompose.height(13)
                rowHeader("Your Last Name")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =lasttName.value,
                    onValueChange = {
                        lasttName.value=it
                    },
                    placeholder = {
                        Text("Enter Your Last Name",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )
                com.io.luma.customcompose.height(13)
                rowHeader("Your Email")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =email.value,
                    onValueChange = {
                        email.value=it
                    },
                    placeholder = {
                        Text("Enter Your Email",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )
                com.io.luma.customcompose.height(13)
                rowHeader("Create Password")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =password.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        password.value=it
                    },

                    placeholder = {
                        Text("Create your Password",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )
                com.io.luma.customcompose.height(13)


                rowHeader("Confirm Password")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword.value,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        confirmPassword.value=it
                    },

                    placeholder = {
                        Text("Confirm your Password",
                            style = TextStyle(
                                color = Color(0xff56575D),
                                fontSize = 15.ssp
                            ))
                    },
                    shape = RoundedCornerShape(6.sdp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedBorderColor = Color(0xff93969B),
                        unfocusedBorderColor = Color(0xff93969B)
                    )
                )

                com.io.luma.customcompose.height(20)

                CustomButton(modifier = Modifier.fillMaxWidth(),
                    "Agree and Continue") {


                    navController.navigate(NavRoute.SignupOptionStep3)
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




            }


        }


    }


}


@Composable
fun rowHeader(text: String) {

    Row() {

        Text(text,
            style = TextStyle(
                color = Color(0xff3F3F3F),
                fontFamily = manropesemibold,
                fontSize = 13.ssp
            ))

        Text(" *",
            style = TextStyle(
                color = Color(0xffDD0000),
                fontFamily = manropesemibold,
                fontSize = 13.ssp
            ))

    }

}