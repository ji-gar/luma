package com.io.luma.customcompose

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.io.luma.R
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
    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    // 🔥 Start sound when dialog shows, stop when dialog disappears
    DisposableEffect(Unit) {
        // Start sound
        val afd = context.resources.openRawResourceFd(R.raw.alarm_alert)
        val player = MediaPlayer().apply {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            isLooping = true
            prepare()
            start()
        }

        mediaPlayer = player

        onDispose {
            // Stop sound automatically when dialog is removed
            mediaPlayer?.let {
                try {
                    it.stop()
                } catch (_: Exception) {
                }
                it.release()
            }
            mediaPlayer = null
        }
    }
    LumaPopupDialog(
        dismissOnClickOutside = false,
        onDismissRequest = onDismissRequest,
        content = {
            NotificationDialogContent(
                headerTitle = headerTitle,
                alertTitle = alertTitle,
                alertDescription = alertDescription,
                btnPositiveText = btnPositiveText,
                btnNegativeText = btnNegativeText,
                onDismissRequest = onDismissRequest,
                onPositiveClick = onPositiveClick,
            )
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
            .background(Color.White, RoundedCornerShape(12.sdp))
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
                color = Color.Black
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
            lineHeight = TextUnit(40f, TextUnitType.Sp),
            color = Color.Black
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