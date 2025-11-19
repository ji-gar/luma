package com.io.luma.customcompose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.io.luma.ui.theme.manropebold
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp

@Composable
fun NotificationDialog(
    headerTitle: String,
    alertTitle: String,
    alertDescription: String,
    btnPositiveText: String,
    btnNegativeText: String,
    onDismissRequest: () -> Unit,
    onPositiveClick: () -> Unit,
) {
    LumaPopupDialog(
        dismissOnClickOutside = false,
        onDismissRequest = onDismissRequest,
        content = {
            Column(
                modifier = Modifier.padding(16.sdp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = headerTitle,
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Text(
                    modifier = Modifier
                        .clip(RoundedCornerShape(30.sdp))
                        .background(Color.Red),
                    text = alertTitle,
                    color = Color.White
                )

                Text(
                    modifier = Modifier.weight(1f),
                    text = alertDescription,
                    textAlign = TextAlign.Center,
                    fontSize = 18.ssp,
                    color = MaterialTheme.colorScheme.onBackground
                )

                CustomButton(text = btnPositiveText, onClick = onPositiveClick)

                CustomOutlineButton(text = btnNegativeText, onClick = onDismissRequest)
            }
        }
    )
}

@Composable
private fun NotificationDialogContent(
    headerTitle: String,
    alertTitle: String,
    alertDescription: String,
    btnPositiveText: String,
    btnNegativeText: String,
    onDismissRequest: () -> Unit,
    onPositiveClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(Color.White)
            .padding(horizontal = 16.sdp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = headerTitle,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            IconButton(
                onClick = onDismissRequest,
                modifier = Modifier.align(Alignment.CenterEnd),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close"
                )
            }
        }

        Text(
            modifier = Modifier
                .clip(RoundedCornerShape(30.sdp))
                .background(Color.Red)
                .padding(8.sdp),
            text = alertTitle,
            fontSize = 16.ssp,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )
        height(24)

        Text(
            modifier = Modifier,
            text = alertDescription,
            textAlign = TextAlign.Center,
            fontSize = 24.ssp,
            fontFamily = manropebold,
            fontWeight = FontWeight(700),
            color = MaterialTheme.colorScheme.onBackground
        )

        height(32)

        CustomButton(
            text = btnPositiveText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.sdp),
            onClick = onPositiveClick
        )
        CustomOutlineButton(
            text = btnNegativeText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.sdp),
            onClick = onDismissRequest
        )
    }
}

@Preview
@Composable
private fun NotificationDialogPreview() {
    NotificationDialogContent(
        headerTitle = "Emergency Call",
        alertTitle = "Serverly disoriented",
        alertDescription = "Emergency situation with Amy Bishop!",
        btnPositiveText = "Talk to Amy",
        btnNegativeText = "No",
        onDismissRequest = { }
    ) { }
}