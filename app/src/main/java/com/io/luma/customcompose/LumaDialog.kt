package com.io.luma.customcompose


import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

@Composable
fun LumaPopupDialog(
    dismissOnClickOutside: Boolean = true,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    var isShow by remember { mutableStateOf(false) }
    var isOpen by remember { mutableStateOf(false) }
    val scaleX by animateFloatAsState(
        targetValue = if (isShow) 1f else 0f,
        animationSpec = tween(300, easing = Ease), label = "scaleX"
    )

    val scaleY by animateFloatAsState(
        targetValue = if (isShow) 1f else 0f,
        animationSpec = tween(300, easing = Ease), label = "scaleY"
    )

    val alpha by animateFloatAsState(
        targetValue = if (isShow) 1f else 0f,
        animationSpec = tween(300, easing = Ease),
        label = "alpha"
    )

    val contentAlpha by animateFloatAsState(
        targetValue = if (isOpen) 1f else 0f,
        animationSpec = tween(100, easing = Ease),
        label = "contentAlpha"
    )

    LaunchedEffect(Unit) {
        delay(100)
        isShow = true
    }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnClickOutside = dismissOnClickOutside,
            dismissOnBackPress = true,
        ),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .graphicsLayer {
                        this.scaleX = scaleX
                        this.scaleY = scaleY
                        this.alpha = alpha
                        if (isShow)
                            isOpen = true
                    }
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = MaterialTheme.shapes.small
                    ),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .graphicsLayer {
                            this.alpha = contentAlpha
                        }) {
                    content.invoke()
                }
            }
        }
    )
}
