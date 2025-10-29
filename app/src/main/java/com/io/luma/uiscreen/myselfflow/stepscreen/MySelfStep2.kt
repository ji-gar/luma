package com.io.luma.uiscreen.myselfflow.stepscreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.height
import com.io.luma.customcompose.width
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
fun MySelfStep2(navController: NavController, registermyself: RegisterViewModel) {

    var language by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var context= LocalContext.current

    Box(modifier = Modifier.fillMaxSize().background(color = Color.White).windowInsetsPadding(
        WindowInsets.statusBars))
    {
        Column(modifier = Modifier.fillMaxSize()) {

            height(20)
         Row(horizontalArrangement = Arrangement.Start,
             modifier = Modifier.padding(horizontal = 20.sdp).width(95.sdp))
         {
             Box(modifier = Modifier.border(width = 2.dp, color = Color.Black,
                 shape = RoundedCornerShape(13.sdp)).clip(RoundedCornerShape(16.sdp)).clickable {
                 navController.popBackStack()
             }.padding(horizontal = 20.sdp).wrapContentSize()) {

                 Row(modifier = Modifier.wrapContentSize().padding(vertical = 15.sdp),
                     verticalAlignment = Alignment.CenterVertically) {

                     Icon(painter = painterResource(R.drawable.backarrow),
                         contentDescription = "")

                     width(3)

                     Text("Back",
                         style = TextStyle(
                             color = textColor,
                             fontSize = 16.ssp
                         ))

                 }
             }
         }


            height(20)

            Column(modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 24.sdp)
                .verticalScroll(rememberScrollState()).imePadding(),
                horizontalAlignment = Alignment.Start)
            {

                Text("Step 2",
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
                rowHeader("Create Password")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =password,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        password=it
                    },

                    placeholder = {
                        Text("Create your Password",
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
                rowHeader("Confirm Password")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = confirmPassword,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        confirmPassword=it
                    },

                    placeholder = {
                        Text("Confirm your Password",
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
                rowHeader("User’s Language")
                com.io.luma.customcompose.height(6)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = language,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    onValueChange = {
                        language=it
                    },
                    trailingIcon = {
                        Icon(
                            imageVector =  Icons.Filled.KeyboardArrowDown,
                            contentDescription = null
                        )
                    },

                    placeholder = {
                        Text("German",
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
            }
            height(20)
            CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp),
                "Set Profile") {
                if (password.isBlank()) {
                   var passwordError = "Please enter password"
                    Toast.makeText(context, passwordError, Toast.LENGTH_SHORT).show()

                } else if (confirmPassword.isBlank())
                {
                  var  confirmPasswordError = "Please enter confirm password"
                    Toast.makeText(context, confirmPasswordError, Toast.LENGTH_SHORT).show()

                } else if (password != confirmPassword) {
                    var confirmPasswordError = "Password and Confirm Password do not match"
                    Toast.makeText(context, confirmPasswordError, Toast.LENGTH_SHORT).show()

                }
                else {
                    registermyself.updatePassword(password,confirmPassword)
                    registermyself.updateLanguage(language = "en")
                    navController.navigate(NavRoute.MyselfStep3)
                }

//                  registermyself.updatePassword(password,confirmPassword)
//                  registermyself.updateLanguage(language = "en")
//                navController.navigate(NavRoute.MyselfStep3)
            }


        }
    }

}