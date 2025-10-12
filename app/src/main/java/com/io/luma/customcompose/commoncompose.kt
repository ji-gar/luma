package com.io.luma.customcompose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ir.kaaveh.sdpcompose.sdp

@Composable
fun height(height:Int=4) {
    Spacer(modifier = Modifier.height(height.sdp))
}

@Composable
fun width(width:Int) {
    Spacer(modifier = Modifier.width(width.sdp))
}