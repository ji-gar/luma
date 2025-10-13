package com.io.luma.uiscreen.someoneelsesignup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.manropesemibold
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.ui.theme.verandaRegular
import com.io.luma.viewmodel.CarerRegisterViewModel
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp



data class CheckBoxUI(
    var title:String,
    var dec:String,
    var isChecked: Boolean
)

@Composable
fun SignupStep6(navController: NavController, carerViewModel: CarerRegisterViewModel) {
    var checkBoxList= remember {

        mutableStateListOf(CheckBoxUI(
            title = "Send with Phone Number",
            dec = "${carerViewModel.user.patientPhoneNumber}",
            isChecked = false
        ),
            CheckBoxUI(
                title = "Send with Email",
                dec = "${carerViewModel.user.email}",
                isChecked = false
            )
            )
    }
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
                )
            )
            {

                Text("Great job!",
                    style = TextStyle(
                        color = textColor,
                        fontSize = 20.ssp
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    fontFamily = manropebold,
                    textAlign = TextAlign.Center
                )

                com.io.luma.customcompose.height(20)

                Text(text = "We’ve almost finished! Let’s send a download link to a Person you will care about", style =
                    TextStyle(
                        fontFamily = manropesemibold,
                        fontSize = 13.ssp,
                        color = textColor
                    ))

                com.io.luma.customcompose.height(20)


                Column(verticalArrangement = Arrangement.spacedBy(20.sdp)) {

                    checkBoxList.forEachIndexed { index, uI ->
                        Row {
                           Checkbox(
                               checked = uI.isChecked,
                               onCheckedChange = {
                                   checkBoxList[index]= uI.copy(isChecked = it)
                               },
                               colors = CheckboxDefaults.colors(
                                   uncheckedColor = Color.Black,
                                   checkmarkColor = Color.White,
                                   checkedColor = Color.Black
                               )
                           )
                            com.io.luma.customcompose.width(4)
                            Column {
                                Text(uI.title, style = TextStyle(
                                    fontFamily = verandaRegular,
                                    fontSize = 13.ssp
                                ))

                                Text(uI.dec, style = TextStyle(
                                    fontFamily = verandaBold,
                                    fontSize = 18.ssp
                                ))

                            }



                        }



                    }
                }


                com.io.luma.customcompose.height(20)

                CustomButton(

                    modifier = Modifier.fillMaxWidth(),
                    text = "Send a Link") { }








            }



        }


    }

}