package com.io.luma.uiscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.ui.theme.verandaRegular
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CarerSignupOption(navController: NavController) {

    var optionList=listOf<String>("For myself.","For someone else.")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(optionList[0]) }
    Box(modifier = Modifier.fillMaxSize().background(color = Color.White),
        contentAlignment = Alignment.Center) {

        Column(
            modifier = Modifier.fillMaxSize().padding(
                horizontal = 16.sdp
            ),
             verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {

            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                Image(painter = painterResource(R.drawable.lumalife_logo),
                    contentDescription = "",
                    modifier = Modifier.height(33.sdp))
            }

            com.io.luma.customcompose.height(30)


            Text("Are you the\nperson who will\nuse Luma\nor are\nyou getting it for someone\nelse?",
                style = TextStyle(
                    color = textColor,
                    fontFamily = verandaBold,
                    fontSize = 26.ssp,
                    fontWeight = FontWeight.W700
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            com.io.luma.customcompose.height(40)

            Column(
                Modifier.selectableGroup(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(26.dp)
            ) {
                optionList.forEach { text ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .border(width = 1.dp, Color.Black, RoundedCornerShape(12.dp))
                            .height(56.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text)

                                          if (text=="For myself.")
                                          {
                                              Log.d("Select===",selectedOption)
                                              navController.navigate(NavRoute.Myself)
                                          }
                                         else {
                                              Log.d("Select===",selectedOption)
                                             navController.navigate(NavRoute.SignupOptionStep1)
                                         }
                                          },
                                role = Role.RadioButton
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text)

                                navController.navigate(NavRoute.SignupOptionStep1)


                                      }, // click works now
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color.Black,
                                unselectedColor = Color.Black
                            )
                        )
                        Text(
                            text = text,
                            style = TextStyle(
                                fontFamily = verandaRegular,
                                color = Color.Black,
                                fontSize = 20.ssp
                            )
                        )
                    }
                }
            }




        }



    }
}