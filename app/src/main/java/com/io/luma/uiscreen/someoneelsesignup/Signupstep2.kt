package com.io.luma.uiscreen.someoneelsesignup

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.model.CarerSignupReuestModel
import com.io.luma.model.SignupResponseModel
import com.io.luma.navroute.NavRoute
import com.io.luma.network.Resource
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.ui.theme.verandaRegular
import com.io.luma.utils.TokenManager
import com.io.luma.viewmodel.CarerRegisterViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import org.w3c.dom.Text

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SignupStep2(navController: NavController,carerSignupViewModel: CarerRegisterViewModel= viewModel()) {
    var firstName = rememberSaveable { mutableStateOf("") }
    var lasttName = rememberSaveable { mutableStateOf("") }
    var email = rememberSaveable { mutableStateOf("") }
    var password = rememberSaveable { mutableStateOf("") }
    var confirmPassword = rememberSaveable { mutableStateOf("") }


    var context= LocalContext.current

    val userFlow by carerSignupViewModel.createUser.collectAsState() // ✅ collectAsState() with 'by'
    val snackbarHostState = remember { SnackbarHostState() }
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
        Scaffold(
            modifier = Modifier.background(
                brush = Brush.linearGradient(
                    listOf(
                        goldenYellow,
                        Color.White,
                        Color.White,
                        Color.White,
                        skyblue
                    )
                )
            ),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        )

        { it

            Column(modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars))
            {

                com.io.luma.customcompose.height(30)
                Row(horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.sdp))
                {

                    Icon(imageVector = Icons.Filled.KeyboardArrowLeft,
                        modifier = Modifier.clickable{
                            navController.popBackStack()
                        },
                        contentDescription = "Back")


                    Image(painter = painterResource(R.drawable.lumalifewide),
                        contentDescription = "",
                        modifier = Modifier.height(33.sdp),

                        )

                    Image(painter = painterResource(R.drawable.ic_launcher_background),
                        contentDescription = "",
                        modifier = Modifier
                            .height(16.sdp)
                            .clip(CircleShape))
                }
                com.io.luma.customcompose.height(30)


                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 20.sdp).verticalScroll(
                        rememberScrollState()
                    ).imePadding().windowInsetsPadding(WindowInsets.navigationBars)
                )
                {

                    Text("Finishing signing up",
                        style = TextStyle(
                            color = textColor,
                            fontSize = 20.ssp
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = manropebold,
                        fontWeight = FontWeight.W700,
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
                                    fontSize = 15.ssp,
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
                        value =lasttName.value,
                        onValueChange = {
                            lasttName.value=it
                        },
                        placeholder = {
                            Text("Enter Your Last Name",
                                style = TextStyle(
                                    color = Color(0xff56575D),
                                    fontSize = 15.ssp,
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
                        value =email.value,
                        onValueChange = {
                            email.value=it
                        },
                        placeholder = {
                            Text("Enter Your Email",
                                style = TextStyle(
                                    color = Color(0xff56575D),
                                    fontSize = 15.ssp,
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
                                    fontSize = 15.ssp,
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
                                    fontSize = 15.ssp,
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

                    com.io.luma.customcompose.height(20)

                    CustomButton(modifier = Modifier.fillMaxWidth(),
                        "Agree and Continue") {


                        if (firstName.value.isEmpty())
                        {
                            Toast.makeText(context, "Please enter first name", Toast.LENGTH_SHORT).show()
                        }
                      else  if (lasttName.value.isEmpty())
                        {
                            Toast.makeText(context, "Please enter last name", Toast.LENGTH_SHORT).show()
                        }
                      else  if (email.value.isEmpty())
                        {
                            Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                        }
                      else  if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches())
                        {
                            Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                        }
                      else  if (password.value.isEmpty())
                        {
                            Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                        }
                      else  if (confirmPassword.value.isEmpty())
                        {
                            Toast.makeText(context, "Please enter confirm password", Toast.LENGTH_SHORT).show()
                        }

                      else  if (password.value!=confirmPassword.value)
                        {
                            Toast.makeText(context, "Password and Confirm Password do not match", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            carerSignupViewModel.updateFirstName(firstName.value)
                            carerSignupViewModel.updateLastName(lasttName.value)
                            carerSignupViewModel.updateEmail(email.value)
                            carerSignupViewModel.updatePassword(password.value,confirmPassword.value)
                            navController.navigate(NavRoute.SignupOptionStep3)
                        }

                    }

                    com.io.luma.customcompose.height(20)

                    Row(modifier = Modifier.fillMaxWidth().windowInsetsPadding(WindowInsets.navigationBars),
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

        when (userFlow) {
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

                val response = (userFlow as Resource.Success<SignupResponseModel>).data
                var token= TokenManager.getInstance(context)
                token.saveTokens(response.accessToken.toString(),response.refreshToken.toString())
                token.saveId(response.user?.userId.toString())
                LaunchedEffect(Unit) {

                }
            }

            is Resource.Error<*> -> {
                val message = (userFlow as Resource.Error<*>).message
                LaunchedEffect(message) {
                    snackbarHostState.showSnackbar(message)
                }
            }

            else -> {}
        }


    }


}


@Composable
fun rowHeader(text: String) {

    Row() {

        Text(text,
            style = TextStyle(
                color = Color(0xff0D0C0C),
                fontFamily = manropesemibold,
                fontWeight = FontWeight.W600,
                fontSize = 13.ssp
            ))

        Text(" *",
            style = TextStyle(
                color = Color(0xffDD0000),
                fontFamily = manropesemibold,
                fontWeight = FontWeight.W600,
                fontSize = 13.ssp
            ))

    }

}