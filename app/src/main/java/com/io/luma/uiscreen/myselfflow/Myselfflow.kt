package com.io.luma.uiscreen.myselfflow

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.verandaBold
import com.io.luma.ui.theme.verandaRegular
import ir.kaaveh.sdpcompose.ssp

@Composable
fun MySelfFlow(navController: NavController) {

    val questions = listOf(
        "Are you the person who will use Luma or are you getting it for someone else?",
        "Do you notice any problems with your memory?",
        "Do you often misplace everyday things, like your keys or glasses?",
        "Do you sometimes forget appointments or planned events?",
        "Do you need help with things like paying bills or managing your money?",
        "Do you need help choosing what clothes to wear each day?"
    )

    var currentIndex by remember { mutableStateOf(0) }
    var optionList=listOf<String>("Yes","No")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(optionList[0]) }


    Box(modifier = Modifier.fillMaxWidth().background(color = Color.White).windowInsetsPadding(
        WindowInsets.statusBars)) {


        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Luma Life",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(
                    fontSize = 22.ssp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
            )

           Column(modifier = Modifier.fillMaxSize(),
               verticalArrangement = Arrangement.Center
               ) {


               com.io.luma.customcompose.height(27)
               Text(
                   text = questions[currentIndex],
                   maxLines = 4,
                   style = TextStyle(fontSize = 26.ssp, textAlign = TextAlign.Center),
                   fontFamily = verandaBold,

                   modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
               )
               com.io.luma.customcompose.height(27)

               Column(
                   Modifier.selectableGroup().padding(horizontal = 16.dp),
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
                                     if (!(currentIndex==questions.size-1))
                                     {
                                         currentIndex++
                                     }
                                     else {
                                         navController.navigate(NavRoute.MyselfStep1)
                                     }
                                       //  navController.navigate(NavRoute.SignupOptionStep1)
                                   },
                                   role = Role.RadioButton
                               ),
                           verticalAlignment = Alignment.CenterVertically
                       ) {
                           RadioButton(
                               selected = (text == selectedOption),
                               onClick = { onOptionSelected(text)
                                   if (!(currentIndex==questions.size-1))
                                   {
                                       currentIndex++
                                   }
                                   else {
                                         navController.navigate(NavRoute.MyselfStep1)
                                   }



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
                               ),
                               modifier = Modifier.padding(start = 16.dp)
                           )
                       }
                   }
               }
           }



        }




    }




}