package com.io.luma.uiscreen.myselfflow.stepscreen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.height
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.uiscreen.someoneelsesignup.rowHeader
import com.io.luma.viewmodel.RegisterViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun MySelfStep1(navController: NavController, registermyself: RegisterViewModel) {
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var context= LocalContext.current


//    var firstName=remember { mutableStateOf("") }
//    var lasttName=remember { mutableStateOf("") }
//    var email=remember { mutableStateOf("") }
//    var phone=remember { mutableStateOf("") }
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White))
    {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center) {

           Row(modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.Center) {
               Image(painter = painterResource(R.drawable.lumatext),
                   contentDescription = "",
                   )
           }

            height(20)

            Column(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.sdp)
                .verticalScroll(rememberScrollState()).imePadding(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start)
            {

                Text("Set up your profile",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = verandaBold,
                    fontWeight = FontWeight.W700,
                    textAlign = TextAlign.Center
                )
                com.io.luma.customcompose.height(20)
                rowHeader("Please Enter Name")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(

                    modifier = Modifier.fillMaxWidth(),
                    value =firstName,
                    onValueChange = {
                        firstName=it
                    },
                    placeholder = {
                        Text("Enter your First Name",
                            style = TextStyle(
                                color = Color(0xff4C4C50),
                                fontSize = 13.ssp,
                                fontFamily = monospaceRegular,
                                fontWeight = FontWeight.W400
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
                    value =lastName,
                    onValueChange = {
                        lastName=it
                    },
                    placeholder = {
                        Text("Enter Your Last Name",
                            style = TextStyle(
                                color = Color(0xff4C4C50),
                                fontSize = 13.ssp,
                                fontFamily = monospaceRegular,
                                fontWeight = FontWeight.W400
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
                    value =email,
                    onValueChange = {
                        email=it
                    },
                    placeholder = {
                        Text("Enter Your Email",
                            style = TextStyle(
                                color = Color(0xff4C4C50),
                                fontSize = 13.ssp,
                                fontFamily = monospaceRegular,
                                fontWeight = FontWeight.W400
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
                rowHeader("User Phone number")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =phone,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    onValueChange = {
                        phone=it
                    },

                    placeholder = {
                        Text("Your Phone Number",
                            style = TextStyle(
                                color = Color(0xff4C4C50),
                                fontSize = 13.ssp,
                                fontFamily = monospaceRegular,
                                fontWeight = FontWeight.W400
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
            }
            height(20)
            CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp),
                "Next") {

                registermyself.updateName(name = firstName+""+lastName)
                registermyself.updateEmail(email=email)
                registermyself.updatePhone(phone = phone)
                when {
                    firstName.isBlank() -> {
                        Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show()
                    }
                    lastName.isBlank() -> {
                        Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show()
                    }
                    email.isBlank() -> {
                        Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                    }
                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                        Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                    }
                    phone.isBlank() -> {
                        Toast.makeText(context, "Please enter phone number", Toast.LENGTH_SHORT).show()
                    }
                    phone.length < 10 -> {
                        Toast.makeText(context, "Phone number must be 10 digits", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        navController.navigate(NavRoute.MyselfStep2)

                       // Toast.makeText(context, "Validation successful", Toast.LENGTH_SHORT).show()
                    }
                }



            }


        }
    }



}