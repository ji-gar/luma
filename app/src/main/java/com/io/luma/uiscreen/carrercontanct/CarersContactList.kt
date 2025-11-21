package com.io.luma.uiscreen.carrercontanct

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.io.luma.R
import com.io.luma.ui.theme.manropebold
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun CarersContactList() {

    val goldenYellow = Color(0xFFFFE68A)
    val skyBlue = Color(0xFFD6F6FF)

    val gradientBrush = Brush.linearGradient(
        listOf(
            goldenYellow,
            Color.White,
            Color.White,
            Color.White,
            skyBlue
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
            .padding(16.sdp)
    ) {
        Column {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.sdp))

            CarerItem(
                name = "Karin",
                isMainContact = true
            )

            Divider(modifier = Modifier.padding(vertical = 8.sdp))

            CarerItem(name = "Julia")
            Divider(modifier = Modifier.padding(vertical = 8.sdp))

            CarerItem(name = "Benedikt")
        }
    }
}

@Composable
fun HeaderSection() {
    com.io.luma.customcompose.height(30)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.iv_smallleftarrow),
            contentDescription = "",

            )
        Text(
            "Amy Bishop",
            style = TextStyle(
                color = Color(0xff0D0C0C),
                fontSize = 20.ssp,
                fontFamily = manropebold,
                fontWeight = FontWeight.W700
            )
        )

        Image(
            painter = painterResource(R.drawable.iv_addplus),
            contentDescription = "",
        )
    }
}

@Composable
fun AddButton() {
    Box(
        modifier = Modifier
            .size(32.sdp)
            .border(
                width = 1.sdp,
                color = Color.Black,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            fontSize = 18.ssp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CarerItem(
    name: String,
    isMainContact: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    fontSize = 15.ssp,
                    fontFamily = manropebold,
                    fontWeight = FontWeight.W700
                )

                if (isMainContact) {
                    Spacer(modifier = Modifier.width(6.sdp))

                    Icon(
                        painter = painterResource(id = R.drawable.iv_maincontent),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.width(6.sdp))

                Icon(
                    painter = painterResource(id = R.drawable.iv_chat),
                    contentDescription = "",
                    tint = Color.Unspecified
                )
            }

            if (isMainContact) {
                Text(
                    text = "Main contact",
                    fontSize = 11.ssp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.sdp)
                )
            }
        }

        Icon(
            painter = painterResource(id = R.drawable.iv_editor),
            contentDescription = "Edit",
            tint = Color.Unspecified

        )
    }
}

