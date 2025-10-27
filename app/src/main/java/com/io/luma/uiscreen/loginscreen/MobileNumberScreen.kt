package com.io.luma.uiscreen.loginscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.model.SignupResponseModel
import com.io.luma.model.VerifyNumberRequestModel
import com.io.luma.model.VerifyNumberResponseModel
import com.io.luma.navroute.NavRoute
import com.io.luma.network.Resource
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import com.io.luma.uiscreen.someoneelsesignup.rowHeader
import com.io.luma.utils.TokenManager
import com.io.luma.viewmodel.MobileNumberCheckViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun MobileNumberScreen(navController: NavController,verifyNumberViewModel: MobileNumberCheckViewModel= viewModel()) {
    var phone= remember{
        mutableStateOf("")
    }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val verifyNumberFlow by verifyNumberViewModel.verifyNumber.collectAsState()


    Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(color = Color.White))
    {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(color = Color.White),
            contentAlignment = Alignment.Center) {

            Image(painter = painterResource(R.drawable.helloluma),
                contentDescription = "")
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)

                .align(alignment = Alignment.BottomCenter)
        )
        {
            OutlinedCard(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
                )
            ) {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,) {
                    com.io.luma.customcompose.height(10)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth().padding(end = 13.sdp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Enter your\nphone number",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 24.ssp,
                                fontFamily = manropebold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Image(
                            painter = painterResource(R.drawable.cancle),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.CenterEnd).size(30.sdp) // align image to the right

                        )
                    }
                    com.io.luma.customcompose.height(13)
                    HorizontalDivider(
                        color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    com.io.luma.customcompose.height(20)
                    Box(modifier = Modifier.padding(horizontal = 13.sdp)){
                        rowHeader("Phone Number")
                    }
                    com.io.luma.customcompose.height(3)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                        value =phone.value,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        onValueChange = {
                            phone.value=it
                        },
                        singleLine = true,
                        keyboardActions = KeyboardActions(onDone = {
                             keyboardController?.hide()

                        }),

                        placeholder = {
                            Text("Your Phone Number",
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
                    CustomButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
                        "Next") {

                        if(phone.value.isNullOrEmpty())
                        {
                            Toast.makeText(context, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show()

                        }
                        else if(phone.value.length<10)
                        {
                            Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
                        }
                        else if(phone.value.length>10)
                        {
                            Toast.makeText(context, "Please enter valid phone number", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            Log.d("Number",phone.value)
                            verifyNumberViewModel.verifyNumber(VerifyNumberRequestModel("+91",phone.value.toString()))
                           // navController.navigate(NavRoute.LoginScreen)
                        }
                    }




                }
            }
        }

        when (verifyNumberFlow) {
            is Resource.Loading<*> -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            is Resource.Success<*> -> {

                val response = (verifyNumberFlow as Resource.Success<VerifyNumberResponseModel>).data
                if (response.data?.nextAction.equals("register"))
                {
                    navController.navigate(NavRoute.SignupOption)
                }
//                var token= TokenManager.getInstance(context)
//                token.saveTokens(response.accessToken.toString(),response.refreshToken.toString())
//                token.saveId(response.user?.userId.toString())
//                LaunchedEffect(Unit) {
//                    navController.navigate(NavRoute.SignupOptionStep6)
//                }
            }

            is Resource.Error<*> -> {
                val message = (verifyNumberFlow as Resource.Error<*>).message
                LaunchedEffect(message) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }

            else -> {}
        }



//        Column(modifier = Modifier.fillMaxSize().background(color = Color.Transparent)) {
//
//            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).background(color = Color.White),
//                contentAlignment = Alignment.Center) {
//
//                Column(modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painter = painterResource(R.drawable.lumaperson),
//                        contentDescription = "")
//
//                }
//            }
//
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .weight(0.5f)
//                    .align(alignment = Alignment.BottomCenter)
//            )
//            {
//                // Top shadow layer
//
//
//                // Card content
//                OutlinedCard(
//                    modifier = Modifier.fillMaxSize(),
//                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color.White,
//                    ),
//                    border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
//                    )
//                ) {
//                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 13.sdp),
//                        horizontalAlignment = Alignment.CenterHorizontally) {
//                        com.io.luma.customcompose.height(10)
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                "Welcome",
//                                style = TextStyle(
//                                    color = textColor,
//                                    fontSize = 24.ssp,
//                                    fontFamily = manropebold
//                                ),
//                                textAlign = TextAlign.Center
//                            )
//
//                            Image(
//                                painter = painterResource(R.drawable.cancle),
//                                contentDescription = "",
//                                modifier = Modifier
//                                    .align(Alignment.CenterEnd).size(30.sdp) // align image to the right
//
//                            )
//                        }
//
//                        com.io.luma.customcompose.height(20)
//
//                        Text("Do you want to\njoin?",
//                            style = TextStyle(
//                                color = textColor,
//                                fontSize = 27.ssp
//                            ),
//                            modifier = Modifier.fillMaxWidth(),
//                            fontFamily = manropebold,
//                            textAlign = TextAlign.Center
//                        )
//                        com.io.luma.customcompose.height(20)
//
//                        CustomButton(modifier = Modifier.fillMaxWidth(),
//                            "Yes") {
//
//
//                            navController.navigate(NavRoute.OnBordingScreen2)
//                        }
//
//                        com.io.luma.customcompose.height(20)
//
//                        CustomOutlineButton (modifier = Modifier.fillMaxWidth(),
//                            "No") {
//
//
//                           // navController.navigate(NavRoute.SignupOptionStep5)
//                        }
//
//
//                    }
//                }
//            }
//
//        }



    }

}