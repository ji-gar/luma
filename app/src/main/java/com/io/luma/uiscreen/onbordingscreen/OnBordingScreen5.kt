package com.io.luma.uiscreen.onbordingscreen

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.xr.scenecore.GltfModel
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import io.github.sceneview.Scene
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader

import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.util.concurrent.TimeUnit
import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioRecord

import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.NoiseSuppressor
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.io.luma.utils.TokenManager
import kotlinx.coroutines.CoroutineScope

import okio.ByteString
import okio.ByteString.Companion.toByteString
import java.util.concurrent.LinkedBlockingQueue
import kotlin.math.abs

@Composable
fun OnBordingScreen5(navController: NavController) {
    val context = LocalContext.current
    val sampleRate = 16000
    val maxQueueSize = 100
    val agentId = "5066"

    // --- Mic / WebSocket State ---
    var isRecording by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }
    var canSendMic by remember { mutableStateOf(true) }
    val audioQueue = remember { LinkedBlockingQueue<ByteArray>() }

    var audioTrack by remember { mutableStateOf<AudioTrack?>(null) }
    var audioRecord by remember { mutableStateOf<AudioRecord?>(null) }
    var webSocket by remember { mutableStateOf<WebSocket?>(null) }

    // --- Permission Handling ---
    val micPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        micPermissionGranted.value = granted
    }

    val backgroundColor by animateColorAsState(
        targetValue = if (isRecording) Color.Red else Color.Gray,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(Unit) {
        if (!micPermissionGranted.value) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                val engine = rememberEngine()
                val modelLoader = rememberModelLoader(engine)
                val cameraNode = rememberCameraNode(engine).apply {
                    position = io.github.sceneview.math.Position(0f, 1f, 4f) // back and slightly up
                    lookAt(io.github.sceneview.math.Position(0f, 1f, 0f))  // look at middle
                }


                Scene(
                    modifier = Modifier.fillMaxSize().background(color = Color.White),
                    engine = engine,
                    modelLoader = modelLoader,
                    cameraNode = cameraNode,
                    childNodes = listOf(
                        ModelNode(
                            modelInstance = modelLoader.createModelInstance("models/marcel.glb"),
                            scaleToUnits = 1.25f,
                        )
                    )
                )
            }

            // --- Card UI Box ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-10).dp)
                    .weight(1f)
            ) {
                OutlinedCard(
                    modifier = Modifier.fillMaxSize(),
                    elevation = CardDefaults.elevatedCardElevation(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 13.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                                                com.io.luma.customcompose.height(10)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Personal\nInformation",
                                style = TextStyle(
                                    color = textColor,
                                    fontSize = 22.ssp
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                fontFamily = manropebold,
                                textAlign = TextAlign.Center
                            )
                            Image(
                                painter = painterResource(R.drawable.cancle),
                                contentDescription = "",
                                modifier = Modifier
                                    .align(Alignment.CenterEnd).size(30.sdp) // align image to the right

                            )
                        }

                        com.io.luma.customcompose.height(20)

                        Text("Let’s fill\nyour Personal\ninformation",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 26.ssp
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            fontFamily = manropebold,
                            textAlign = TextAlign.Center
                        )
                        com.io.luma.customcompose.height(10)

                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .shadow(4.dp, CircleShape)
                                .background(if (isRecording) Color.Red else Color.Gray, shape = CircleShape)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        if (micPermissionGranted.value) {
                                            isRecording = !isRecording
                                        }
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (isRecording) "Stop" else "Start",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // --- Mic Button ---
//                        Box(
//                            modifier = Modifier
//                                .size(30.dp)
//                                .background(if (isRecording) Color.Red else Color.Gray)
//                                .clickable {
//                                    if (micPermissionGranted.value) {
//                                        isRecording = !isRecording
//                                    }
//                                },
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                if (isRecording) "Stop" else "Start",
//                                color = Color.White
//                            )
//                        }



                        Spacer(modifier = Modifier.height(20.dp))

                       CustomButton(
                           modifier = Modifier.fillMaxWidth()
                           ,text = "Next") {

                           navController.navigate(NavRoute.OnBordingScreen6)

                       }
                    }
                }
            }
        }
    }

    // --- WebSocket + Playback Loop ---
    LaunchedEffect(agentId) {
        if (!micPermissionGranted.value) return@LaunchedEffect

        val client = OkHttpClient.Builder().readTimeout(0, java.util.concurrent.TimeUnit.MILLISECONDS).build()
        var token= TokenManager.getInstance(context)

        Log.d("Xccc","wss://api-mvp.lumalife.de/ws/agents/${token.getId()}")
        val request = Request.Builder().url("wss://api-mvp.lumalife.de/ws/agents/${token.getId()}").build()
        val wsListener = object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                webSocket = ws
                // Playback Loop
                CoroutineScope(Dispatchers.IO).launch {
                    if (audioTrack == null) {
                        val minBuffer = AudioTrack.getMinBufferSize(
                            sampleRate,
                            AudioFormat.CHANNEL_OUT_MONO,
                            AudioFormat.ENCODING_PCM_16BIT
                        ) * 6
                        audioTrack = AudioTrack.Builder()
                            .setAudioAttributes(
                                AudioAttributes.Builder()
                                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                                    .build()
                            )
                            .setAudioFormat(
                                AudioFormat.Builder()
                                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                                    .setSampleRate(sampleRate)
                                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                                    .build()
                            )
                            .setBufferSizeInBytes(minBuffer)
                            .setTransferMode(AudioTrack.MODE_STREAM)
                            .build()
                        audioTrack?.play()
                    }

                    val tempBuffer = ByteArray(2048)
                    while (isActive) {
                        val audioData = audioQueue.take()
                        var offset = 0
                        val chunkSize = 1024
                        while (offset < audioData.size) {
                            val len = minOf(chunkSize, audioData.size - offset)
                            if (isSpeaking) {
                                for (i in 0 until len step 2) {
                                    val sample = (audioData[offset + i + 1].toInt() shl 8) or
                                            (audioData[offset + i].toInt() and 0xFF)
                                    val ducked = (sample * 0.3).toInt()
                                    tempBuffer[i] = (ducked and 0xFF).toByte()
                                    tempBuffer[i + 1] = ((ducked shr 8) and 0xFF).toByte()
                                }
                                audioTrack?.write(tempBuffer, 0, len)
                            } else {
                                audioTrack?.write(audioData, offset, len)
                            }
                            offset += len
                        }
                        if (audioQueue.isEmpty()) canSendMic = true
                    }
                }
            }

            override fun onMessage(ws: WebSocket, bytes: ByteString) {
                if (audioQueue.size < maxQueueSize) audioQueue.offer(bytes.toByteArray())
                else { audioQueue.poll(); audioQueue.offer(bytes.toByteArray()) }
                canSendMic = false
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("WS", text)
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WS", "Error: ${t.message}", t)
            }
        }

        webSocket = client.newWebSocket(request, wsListener)
    }

    // --- Audio Recording + Cleanup ---
    DisposableEffect(isRecording) {
        if (isRecording && micPermissionGranted.value) {
            val bufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(sampleRate / 2)

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.MIC,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            ).apply {
                if (AcousticEchoCanceler.isAvailable())
                    AcousticEchoCanceler.create(audioSessionId)?.enabled = true
                if (NoiseSuppressor.isAvailable())
                    NoiseSuppressor.create(audioSessionId)?.enabled = true
                startRecording()
            }

            val job = CoroutineScope(Dispatchers.IO).launch {
                val buffer = ByteArray(bufferSize)
                val silenceThreshold = 700
                var lastVoiceTime = System.currentTimeMillis()
                val silenceTimeout = 1200L

                while (isActive && isRecording) {
                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        var maxAmp = 0
                        var i = 0
                        while (i < read - 1) {
                            val sample = (buffer[i + 1].toInt() shl 8) or (buffer[i].toInt() and 0xFF)
                            maxAmp = maxOf(maxAmp, abs(sample))
                            i += 2
                        }

                        if (maxAmp > silenceThreshold) {
                            lastVoiceTime = System.currentTimeMillis()
                            isSpeaking = true
                        } else if (isSpeaking && System.currentTimeMillis() - lastVoiceTime > silenceTimeout) {
                            isSpeaking = false
                        }

                        if (isSpeaking && canSendMic) {
                            webSocket?.send(buffer.toByteString(0, read))
                        }
                    }
                }
            }

            onDispose {
                job.cancel()
                audioRecord?.stop()
                audioRecord?.release()
                audioRecord = null
                audioTrack?.stop()
                audioTrack?.release()
                audioTrack = null
                webSocket?.close(1000, "Disposed")
                isRecording = false
                isSpeaking = false
            }
        } else {
            onDispose { }
        }
    }
}


//@Composable
//fun OnBordingScreen5(navController: NavController) {
//
//    val context = LocalContext.current
//    val sampleRate = 16000
//    val maxQueueSize = 100
//    val agentId = "876"
//
//    // --- State ---
//    var isRecording by remember { mutableStateOf(false) }
//    var isSpeaking by remember { mutableStateOf(false) }
//    var canSendMic by remember { mutableStateOf(true) }
//    val audioQueue = remember { LinkedBlockingQueue<ByteArray>() }
//
//    // --- Holders ---
//    var audioTrack by remember { mutableStateOf<AudioTrack?>(null) }
//    var audioRecord by remember { mutableStateOf<AudioRecord?>(null) }
//    var webSocket by remember { mutableStateOf<WebSocket?>(null) }
//
//    // --- Permission Handling ---
//    val micPermissionGranted = remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
//                    PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { granted ->
//        micPermissionGranted.value = granted
//    }
//
//    LaunchedEffect(Unit) {
//        if (!micPermissionGranted.value) {
//            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//        }
//    }
//
//    // --- UI ---
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(if (isRecording) Color.Red else Color.Gray)
//            .clickable {
//                if (micPermissionGranted.value) isRecording = !isRecording
//            },
//        contentAlignment = Alignment.Center
//    ) {
//        Text(if (isRecording) "Stop" else "Start", color = Color.White)
//    }
//
//    // --- WebSocket ---
//    LaunchedEffect(agentId) {
//        if (!micPermissionGranted.value) return@LaunchedEffect
//
//        val client = OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build()
//        val request = Request.Builder()
//            .url("wss://api-mvp.lumalife.de/ws/agents/$agentId").build()
//
//        val wsListener = object : WebSocketListener() {
//            override fun onOpen(ws: WebSocket, response: Response) {
//                webSocket = ws
//
//                // --- Playback loop ---
//                CoroutineScope(Dispatchers.IO).launch {
//                    if (audioTrack == null) {
//                        val minBuffer = AudioTrack.getMinBufferSize(
//                            sampleRate,
//                            AudioFormat.CHANNEL_OUT_MONO,
//                            AudioFormat.ENCODING_PCM_16BIT
//                        ) * 6
//                        audioTrack = AudioTrack.Builder()
//                            .setAudioAttributes(
//                                AudioAttributes.Builder()
//                                    .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
//                                    .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
//                                    .build()
//                            )
//                            .setAudioFormat(
//                                AudioFormat.Builder()
//                                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
//                                    .setSampleRate(sampleRate)
//                                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
//                                    .build()
//                            )
//                            .setBufferSizeInBytes(minBuffer)
//                            .setTransferMode(AudioTrack.MODE_STREAM)
//                            .build()
//                        audioTrack?.play()
//                    }
//
//                    val tempBuffer = ByteArray(2048)
//                    while (isActive) {
//                        val audioData = audioQueue.take()
//                        var offset = 0
//                        val chunkSize = 1024
//                        while (offset < audioData.size) {
//                            val len = minOf(chunkSize, audioData.size - offset)
//                            if (isSpeaking) {
//                                for (i in 0 until len step 2) {
//                                    val sample = (audioData[offset + i + 1].toInt() shl 8) or
//                                            (audioData[offset + i].toInt() and 0xFF)
//                                    val ducked = (sample * 0.3).toInt()
//                                    tempBuffer[i] = (ducked and 0xFF).toByte()
//                                    tempBuffer[i + 1] = ((ducked shr 8) and 0xFF).toByte()
//                                }
//                                audioTrack?.write(tempBuffer, 0, len)
//                            } else {
//                                audioTrack?.write(audioData, offset, len)
//                            }
//                            offset += len
//                        }
//                        if (audioQueue.isEmpty()) canSendMic = true
//                    }
//                }
//            }
//
//            override fun onMessage(ws: WebSocket, bytes: ByteString) {
//                if (audioQueue.size < maxQueueSize) audioQueue.offer(bytes.toByteArray())
//                else { audioQueue.poll(); audioQueue.offer(bytes.toByteArray()) }
//                canSendMic = false
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                super.onMessage(webSocket, text)
//
//                Log.e("WS", text)
//            }
//
//            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
//                Log.e("WS", "Error: ${t.message}", t)
//            }
//        }
//
//        webSocket = client.newWebSocket(request, wsListener)
//    }
//
//    // --- Audio Recording + Cleanup ---
//    DisposableEffect(isRecording) {
//        if (isRecording && micPermissionGranted.value) {
//            val bufferSize = AudioRecord.getMinBufferSize(
//                sampleRate,
//                AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT
//            ).coerceAtLeast(sampleRate / 2)
//
//            audioRecord = AudioRecord(
//                MediaRecorder.AudioSource.MIC,
//                sampleRate,
//                AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT,
//                bufferSize
//            ).apply {
//                if (AcousticEchoCanceler.isAvailable())
//                    AcousticEchoCanceler.create(audioSessionId)?.enabled = true
//                if (NoiseSuppressor.isAvailable())
//                    NoiseSuppressor.create(audioSessionId)?.enabled = true
//                startRecording()
//            }
//
//            val job = CoroutineScope(Dispatchers.IO).launch {
//                val buffer = ByteArray(bufferSize)
//                val silenceThreshold = 700
//                var lastVoiceTime = System.currentTimeMillis()
//                val silenceTimeout = 1200L
//
//                while (isActive && isRecording) {
//                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
//                    if (read > 0) {
//                        var maxAmp = 0
//                        var i = 0
//                        while (i < read - 1) {
//                            val sample = (buffer[i + 1].toInt() shl 8) or (buffer[i].toInt() and 0xFF)
//                            maxAmp = maxOf(maxAmp, abs(sample))
//                            i += 2
//                        }
//
//                        if (maxAmp > silenceThreshold) {
//                            lastVoiceTime = System.currentTimeMillis()
//                            isSpeaking = true
//                        } else if (isSpeaking && System.currentTimeMillis() - lastVoiceTime > silenceTimeout) {
//                            isSpeaking = false
//                        }
//
//                        if (isSpeaking && canSendMic) {
//                            webSocket?.send(buffer.toByteString(0, read))
//                        }
//                    }
//                }
//            }
//
//            onDispose {
//                job.cancel()
//                audioRecord?.stop()
//                audioRecord?.release()
//                audioRecord = null
//                audioTrack?.stop()
//                audioTrack?.release()
//                audioTrack = null
//                webSocket?.close(1000, "Disposed")
//                isRecording = false
//                isSpeaking = false
//            }
//        } else {
//            onDispose { }
//        }
//    }
//}



//@Composable
//fun OnBordingScreen5(navController: NavController) {
//
//
//
//    Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(color = Color.White))
//    {
//
//
//        Column(modifier = Modifier.fillMaxSize().background(color = Color.Transparent)) {
//
//            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).background(color = Color.White),
//                contentAlignment = Alignment.Center) {
//
//                Column(modifier = Modifier.fillMaxSize().background(color = Color.White),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally) {
//
//                    val engine = rememberEngine()
//                    val modelLoader = rememberModelLoader(engine)
//                    val cameraNode = rememberCameraNode(engine)
//                    cameraNode.position =
//                        _root_ide_package_.io.github.sceneview.math.Position(z = 3f)
//
//                    Scene(
//                        modifier = Modifier.fillMaxSize().background(color = Color.White),
//                        engine = engine,
//                        modelLoader = modelLoader,
//                        cameraNode = cameraNode,
//                        childNodes = listOf(
//                            ModelNode(
//                                modelInstance = modelLoader.createModelInstance("models/marcel.glb"), // ✅ no space
//                                scaleToUnits = 1.25f
//                            )
//                        )
//                    )
//
//
////                    Image(painter = painterResource(R.drawable.img),
////                        modifier = Modifier.height(150.dp),
////                        contentScale = ContentScale.Crop,
////                        contentDescription = "")
//
//                }
//            }
//
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .weight(1f)
//            ) {
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
//                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 30.sdp),
//                        horizontalAlignment = Alignment.CenterHorizontally) {
//                        com.io.luma.customcompose.height(10)
//                        Text("Personal\nInformation",
//                            style = TextStyle(
//                                color = textColor,
//                                fontSize = 22.ssp
//                            ),
//                            modifier = Modifier.fillMaxWidth(),
//                            fontFamily = manropebold,
//                            textAlign = TextAlign.Center
//                        )
//                        com.io.luma.customcompose.height(20)
//
//                        Text("Let’s fill\nyour Personal\ninformation",
//                            style = TextStyle(
//                                color = textColor,
//                                fontSize = 26.ssp
//                            ),
//                            modifier = Modifier.fillMaxWidth(),
//                            fontFamily = manropebold,
//                            textAlign = TextAlign.Center
//                        )
//                        com.io.luma.customcompose.height(20)
//
//                        CustomButton(modifier = Modifier.fillMaxWidth(),
//                            "Repeat Question") {
//
//                            navController.navigate(NavRoute.OnBordingScreen6)
//                        }
//
//
//
//
//                    }
//                }
//            }
//
//        }
//
//
//
//    }
//
//}