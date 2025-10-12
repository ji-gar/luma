package com.io.luma.customcompose

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.io.luma.ui.theme.skyblue
import com.io.luma.ui.theme.verandaBold
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CustomButton(modifier: Modifier = Modifier,
                text: String,bgColor: Color= skyblue,
                 textColor: Color=Color(0xff0D0C0C),
                 fontSize: Int=16,
                 isIcon: Boolean=false,
                 onClick :()->Unit

) {
    Button(
        modifier = modifier,
        shape = RoundedCornerShape(41.sdp),
        colors = ButtonDefaults.buttonColors(
            containerColor = bgColor,
            contentColor = textColor
        ),
        contentPadding = PaddingValues(
            vertical = 11.sdp
        ),
        onClick = {

        onClick.invoke()
    }) {


        Row {
            Text(text = text, style = TextStyle(
                fontSize = fontSize.ssp,
                fontFamily = verandaBold
            ))
            width(3)
            if (isIcon)
            {
                width(3)
                androidx.compose.material3.Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = ""
                )
            }
        }
    }
}


@Composable
fun CustomOutlineButton(modifier: Modifier = Modifier,
                 text: String,bgColor: Color= Color.Transparent,
                 textColor: Color=Color(0xff0D0C0C),
                 fontSize: Int=16,
                  isIcon: Boolean=false,
                 onClick :()->Unit

) {
    OutlinedButton(
        modifier = modifier,
        border = BorderStroke(width=1.dp,Color.Black),
        shape = RoundedCornerShape(41.sdp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = textColor,
            containerColor = bgColor
        ),
        contentPadding = PaddingValues(
            vertical = 10.sdp,
            horizontal = 5.sdp
        ),
        onClick = {

            onClick.invoke()
        }) {


        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = text, style = TextStyle(
                fontSize = fontSize.ssp,
                fontFamily = verandaBold
            ))
            if (isIcon)
            {
                width(3)
                androidx.compose.material3.Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = ""
                )
            }
        }
    }
}