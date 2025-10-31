package com.io.luma.uiscreen.loginscreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.io.luma.customcompose.height
import com.io.luma.model.Country
import com.io.luma.model.SignupResponseModel
import com.io.luma.model.VerifyNumberRequestModel
import com.io.luma.model.VerifyNumberResponseModel
import com.io.luma.navroute.NavRoute
import com.io.luma.network.Resource
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.monospaceRegular
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
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

     val countries = listOf(
        Country("Germany", "+49", "🇩🇪"),
        Country("Switzerland", "+41", "🇨🇭"),
        Country("Austria", "+43", "🇦🇹"),
        Country("United Kingdom", "+44", "🇬🇧"),
        Country("United States", "+1", "🇺🇸")
    )


    Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(color = Color.White))
    {
        Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().background(color = Color.White),
            ) {

            Image(painter = painterResource(R.drawable.helloluma),
                contentDescription = "")
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.40f)

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
                    horizontalAlignment = Alignment.Start) {
                    height(10)
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center)
                    {

                        Icon(painter = painterResource(R.drawable.iv_indicator),
                            contentDescription = "",tint=Color.Unspecified)
                    }
                    height(13)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth().padding(end = 13.sdp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Enter your\nPhone number",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 21.ssp,
                                fontFamily = verandaBold,
                                fontWeight = FontWeight.W700
                            ),
                            textAlign = TextAlign.Center
                        )

                        Icon(
                            painter = painterResource(R.drawable.cancle),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .align(Alignment.CenterEnd) // align image to the right

                        )
                    }
                    height(13)
                    HorizontalDivider(
                        color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    com.io.luma.customcompose.height(20)
                    Box(modifier = Modifier.padding(horizontal = 13.sdp)){
                        rowHeader("Phone Number")
                    }
                    com.io.luma.customcompose.height(6)
                    Row(modifier = Modifier.fillMaxWidth())
                    {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 13.sdp),
                            value = phone.value,
                            onValueChange = { phone.value = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            leadingIcon = {
                                CountryOutlinedDropdown(
                                    modifier = Modifier.wrapContentHeight()
                                )
                            },
                            placeholder = {
                                Text(
                                    "Enter your phone number",
                                    style = TextStyle(
                                        color = Color(0xff56575D),
                                        fontSize = 15.ssp,
                                        fontFamily = monospaceRegular
                                    )
                                )
                            },
                            shape = RoundedCornerShape(6.sdp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedBorderColor = Color(0xff93969B),
                                unfocusedBorderColor = Color(0xff93969B)
                            )
                        )




//                        OutlinedTextField(
//                            modifier = Modifier.fillMaxWidth().padding(horizontal = 13.sdp),
//                            value =phone.value,
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                            onValueChange = {
//                                phone.value=it
//                            },
//                            leadingIcon = {
//                                CountryOutlinedDropdown(modifier = Modifier.wrapContentSize()) {
//
//                                }
//                            },
//                            singleLine = true,
//                            keyboardActions = KeyboardActions(onDone = {
//                                keyboardController?.hide()
//
//                            }),
//
//                            placeholder = {
//                                Text("Enter your phone number",
//                                    style = TextStyle(
//                                        color = Color(0xff56575D),
//                                        fontSize = 15.ssp,
//                                        fontFamily = monospaceRegular
//                                    ))
//                            },
//                            shape = RoundedCornerShape(6.sdp),
//                            colors = OutlinedTextFieldDefaults.colors(
//                                focusedContainerColor = Color.Transparent,
//                                unfocusedContainerColor = Color.Transparent,
//                                focusedBorderColor = Color(0xff93969B),
//                                unfocusedBorderColor = Color(0xff93969B)
//                            )
//                        )
                    }
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
                else if (response.data?.nextAction.equals("login"))
                {
                    navController.navigate(NavRoute.LoginScreen)
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
private val countries = listOf(
    Country("Germany", "+49", "🇩🇪"),
    Country("Switzerland", "+41", "🇨🇭"),
    Country("Austria", "+43", "🇦🇺"),
    Country("United Kingdom", "+44", "🇬🇧"),
    Country("United States", "+1", "🇺🇸")
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryOutlinedDropdown(
    modifier: Modifier = Modifier,
    items: List<Country> = countries,
    selectedCountry: MutableState<Country?> = remember { mutableStateOf(items.first()) },
    onCountrySelected: (Country) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    // ✅ Compact layout without using OutlinedTextField inside
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { expanded = true }
            .padding(horizontal = 6.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedCountry.value?.flagEmoji ?: "🇨🇭",
                fontSize = 18.ssp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = selectedCountry.value?.code ?: "+41",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 15.ssp,
                    fontFamily = monospaceRegular
                )
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Dropdown Arrow",
                tint = Color(0xff56575D),
                modifier = Modifier.size(18.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            containerColor = Color.White,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { country ->
                DropdownMenuItem(
                    text = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row {
                                Text(
                                    text = country.flagEmoji,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = country.name,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Text(
                                text = country.code,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xff56575D)
                            )
                        }
                    },
                    onClick = {
                        selectedCountry.value = country
                        expanded = false
                        onCountrySelected(country)
                    }
                )
            }
        }
    }
}
