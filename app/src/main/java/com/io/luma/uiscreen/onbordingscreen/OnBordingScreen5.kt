package com.io.luma.uiscreen.onbordingscreen

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.NoiseSuppressor
import android.util.Base64
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.io.luma.R
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.customcompose.width
import com.io.luma.model.CalendarItem
import com.io.luma.model.CalendarResponse
import com.io.luma.model.ContactItem
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import com.io.luma.ui.theme.verandaBold
import com.io.luma.utils.TokenManager
import io.github.sceneview.Scene
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import okio.ByteString.Companion.toByteString
import org.json.JSONObject
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.sqrt


enum class VoiceState {
    IDLE,              // Ready to start
    USER_SPEAKING,     // User is speaking
    AI_SPEAKING,       // AI is responding
    INTERRUPTED        // User interrupted AI
}

@Composable
fun OnBordingScreen5(navController: NavController) {

    val context = LocalContext.current
    val sampleRate = 16000
    val agentId = "5066"

    var isRecording by remember { mutableStateOf(false) }
    var isCalander by remember { mutableStateOf(false) }
    var isContactList by remember { mutableStateOf(false) }
    var isServerSpeaking by remember { mutableStateOf(false) }

    val audioQueue = remember { LinkedBlockingQueue<ByteArray>() }
    var audioTrack by remember { mutableStateOf<AudioTrack?>(null) }
    var audioRecord by remember { mutableStateOf<AudioRecord?>(null) }
    var webSocket by remember { mutableStateOf<WebSocket?>(null) }

    var information by remember { mutableStateOf("") }
    var lastAiSpeechEndTime by remember { mutableStateOf(0L) }
    var contact by remember { mutableStateOf("") }
    var playbackJob by remember { mutableStateOf<Job?>(null) }
    var shouldStopPlayback by remember { mutableStateOf(false) }
    var voiceState by remember { mutableStateOf(VoiceState.IDLE) }
    var userSpeechDetected by remember { mutableStateOf(false) }

    val optionList = remember { mutableStateListOf<RadioGroup>() }
    val calendarList = remember { mutableStateListOf<CalendarItem>() }


    val (selectedIndex, onSelectedIndexChange) = remember { mutableStateOf(0) }


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
    BackHandler {
        // Clean up all resources before navigating back
        Log.d("Lifecycle", "Back button pressed - cleaning up")

        // Stop recording
        isRecording = false

        // Stop playback
        shouldStopPlayback = true
        playbackJob?.cancel()

        // Clean up audio
        audioRecord?.stop()
        audioRecord?.release()
        audioTrack?.stop()
        audioTrack?.flush()
        audioTrack?.release()
        audioQueue.clear()

        // Close WebSocket
        webSocket?.close(1000, "User closed connection")
        webSocket = null

        // Reset state
        voiceState = VoiceState.IDLE
        userSpeechDetected = false
        isServerSpeaking = false

        navController.navigate(NavRoute.OnBordingScreen4)
    }



    // ------------------ UI Start ------------------
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White),

            )
        {
            val engine = rememberEngine()
            val modelLoader = rememberModelLoader(engine)
            val cameraNode = rememberCameraNode(engine).apply {
                position = io.github.sceneview.math.Position(0f, 1f, 4f)
                lookAt(io.github.sceneview.math.Position(0f, 1f, 0f))
            }

            Image(painter = painterResource(R.drawable.onbordingluma),
                contentDescription = "")
//            Scene(
//                modifier = Modifier.fillMaxSize().background(Color.White),
//                engine = engine,
//                modelLoader = modelLoader,
//                cameraNode = cameraNode,
//                childNodes = listOf(
//                    ModelNode(
//                        modelInstance = modelLoader.createModelInstance("models/marcel.glb"),
//                        scaleToUnits = 1.25f,
//                    )
//                )
//            )
        }

        // Show info / contacts / default UI
        when {
            isCalander -> {





                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter)
                ) {
                    OutlinedCard(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.elevatedCardElevation(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(13.sdp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            com.io.luma.customcompose.height(13)
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center)
                            {

                                Icon(painter = painterResource(R.drawable.iv_indicator),
                                    contentDescription = "",tint=Color.Unspecified)
                            }
                            com.io.luma.customcompose.height(13)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth().padding(end = 13.sdp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Your Nomal Day",
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 21.ssp,
                                        fontFamily = verandaBold,
                                        fontWeight = FontWeight.W700
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Icon(
                                    painter = painterResource(R.drawable.cancle),
                                    contentDescription = "",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd) // align image to the right

                                )
                            }
                            com.io.luma.customcompose.height(13)
                            HorizontalDivider(
                                color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                                thickness = 1.dp
                            )
                            com.io.luma.customcompose.height(13)

                            // 3️⃣ Show each item in list
                            calendarList.forEach { item ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                ) {
                                    Text(
                                        text = item.start_time ?: "--:--",
                                        fontWeight = FontWeight.W700,
                                        style = TextStyle(
                                            fontFamily = verandaBold,
                                            fontSize = 21.ssp,
                                            color = Color(0xff0D0C0C)
                                        )
                                    )
                                    width(10)
                                    Text(
                                        text = item.title ?: "",
                                        fontWeight = FontWeight.W700,

                                        style = TextStyle(
                                            fontFamily = verandaBold,
                                            fontSize = 21.ssp,
                                            color = Color(0xff0D0C0C)
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            CustomOutlineButton(
                                onClick = {},
                                modifier = Modifier.fillMaxWidth(),
                                text = "See you Afternoon"
                            )
                        }
                    }
                }
            }

            isContactList -> {
                Log.d("Contact",contact)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter)
                ) {

                    OutlinedCard(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.elevatedCardElevation(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f))
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                                .padding(horizontal = 13.dp),
                        ) {
                            com.io.luma.customcompose.height(13)
                            Row(modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center)
                            {

                                Icon(painter = painterResource(R.drawable.iv_indicator),
                                    contentDescription = "",tint=Color.Unspecified)
                            }
                            com.io.luma.customcompose.height(13)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth().padding(end = 13.sdp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    "Choose Your\nCarer!",
                                    style = TextStyle(
                                        color = textColor,
                                        fontSize = 21.ssp,
                                        fontFamily = verandaBold,
                                        fontWeight = FontWeight.W700
                                    ),
                                    textAlign = TextAlign.Center
                                )

                                Icon(
                                    painter = painterResource(R.drawable.cancle),
                                    contentDescription = "",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd) // align image to the right

                                )
                            }
                            com.io.luma.customcompose.height(13)
                            HorizontalDivider(
                                color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                                thickness = 1.dp
                            )
                            com.io.luma.customcompose.height(13)
                            optionList.forEachIndexed { index,text ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth().padding(bottom = 10.dp)
                                        .background(
                                            color = Color.White,
                                            shape = RoundedCornerShape(12.dp)
                                        ) .selectable(
                                            selected = (index == selectedIndex),
                                            onClick = { onSelectedIndexChange(index) }
                                        )
                                        .border(width =if (index==selectedIndex)  2.dp else  1.dp, Color.Black, RoundedCornerShape(12.dp))
                                        ,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (index == selectedIndex),
                                        onClick = { onSelectedIndexChange(index) }, // click works now
                                        colors = RadioButtonDefaults.colors(
                                            selectedColor = Color.Black,
                                            unselectedColor = Color.Black
                                        )
                                    )
                                    Column(modifier = Modifier.padding(top = 13.sdp,
                                        bottom = 13.sdp
                                    )
                                    ) {
                                        Text(
                                            text = text.tilte,
                                            style = TextStyle(
                                                fontFamily = verandaBold,
                                                color = Color(0xff0D0C0C),
                                                fontWeight = FontWeight.W700,
                                                fontSize = 18.ssp
                                            )
                                        )
                                        Text(
                                            text = text.dec,
                                            style = TextStyle(
                                                fontFamily = verandaBold,
                                                color = Color(0xff4C4C50),
                                                fontSize = 18.ssp
                                            )
                                        )
                                    }
                                }
                            }
                            com.io.luma.customcompose.height(13)

                            CustomButton(modifier = Modifier.fillMaxWidth(),
                                "Yes") {

                                //  navController.navigate(NavRoute.OnBordingScreen8)
                            }
                        }
                    }
                }
            }

            else -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                        .align(Alignment.BottomCenter)

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
                                ,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth().padding(end = 13.dp),
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

                            com.io.luma.customcompose.height(13)
                            HorizontalDivider(
                                color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                                thickness = 1.dp
                            )

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

                            Box(
                                modifier = Modifier
                                    .size(45.dp)
                                    .shadow(4.dp, CircleShape)
                                    .background(backgroundColor, shape = CircleShape)
                                    .clickable {
                                        if (micPermissionGranted.value) {
                                            isRecording = !isRecording
                                            if (!isRecording) {
                                                // Reset state when stopping
                                                voiceState = VoiceState.IDLE
                                                userSpeechDetected = false
                                            }
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            )
                            {
                                Text(
                                    text = if (isRecording) "Stop" else "Start",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            )
                            {
                                val stateText = when (voiceState) {
                                    VoiceState.IDLE -> "Ready to listen"
                                    VoiceState.USER_SPEAKING -> "You are speaking..."
                                    VoiceState.AI_SPEAKING -> "AI is responding..."
                                    VoiceState.INTERRUPTED -> "Processing interruption..."
                                }
                                val stateColor = when (voiceState) {
                                    VoiceState.IDLE -> Color.Gray
                                    VoiceState.USER_SPEAKING -> Color(0xFF4CAF50)
                                    VoiceState.AI_SPEAKING -> Color(0xFF2196F3)
                                    VoiceState.INTERRUPTED -> Color(0xFFFF9800)
                                }

                                Text(
                                    text = stateText,
                                    style = TextStyle(
                                        color = stateColor,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            CustomButton(text = "Repeat Question",
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 13.dp)) { }
                        }
                    }
                }
            }
        }




//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .windowInsetsPadding(WindowInsets.statusBars)
//        ) {
//            // 3D Scene / Model
//
//        }
    }
    // ------------------ UI End ------------------

    // ------------------ WebSocket & Audio Handling ------------------
//    LaunchedEffect(agentId) {
//        if (!micPermissionGranted.value) return@LaunchedEffect
//
//        val client = OkHttpClient.Builder()
//            .readTimeout(0, TimeUnit.MILLISECONDS)
//            .retryOnConnectionFailure(true)
//            .build()
//
//        val token = TokenManager.getInstance(context)
//        val request = Request.Builder()
//            .url("wss://api-mvp.lumalife.de/ws/agents/${token.getId()}")
//            .build()
//
//        val wsListener = object : WebSocketListener() {
//
//            override fun onOpen(ws: WebSocket, response: Response) {
//                webSocket = ws
//
//                // Initialize AudioTrack once
//                CoroutineScope(Dispatchers.IO).launch {
//                    if (audioTrack == null) {
//                        val minBuffer = AudioTrack.getMinBufferSize(
//                            sampleRate,
//                            AudioFormat.CHANNEL_OUT_MONO,
//                            AudioFormat.ENCODING_PCM_16BIT
//                        ) * 6
//
//                        audioTrack = AudioTrack.Builder()
//                            .setAudioAttributes(
//                                AudioAttributes.Builder()
//                                    .setUsage(AudioAttributes.USAGE_MEDIA)
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
//
//                        audioTrack?.play()
//                    }
//
//                    // Playback loop for queued audio
//                    while (isActive) {
//                        val audioData = audioQueue.take()
//                        isServerSpeaking = true
//                        audioTrack?.write(audioData, 0, audioData.size)
//                        isServerSpeaking = false
//                    }
//                }
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                val jsonObject = JSONObject(text)
//                Log.d("WS", text)
//                when (jsonObject.optString("type")) {
//                    "calendar_update" -> {
//                        val json = jsonObject.getJSONArray("calendar_items")
//                        CoroutineScope(Dispatchers.Main).launch {
//                            if (json.length() > 0) {
//                                isCalander = true
//                                information = json.toString()
//                                val parsed = Gson().fromJson(information, Array<CalendarItem>::class.java).toList()
//                                calendarList.clear()
//                                calendarList.addAll(parsed)
//
//                                //calendarList.value = Gson().fromJson(information, Array<CalendarItem>::class.java).toList()
//
//
//                            }
//                        }
//                    }
//
//                    "contact_update" -> {
//                        val json = jsonObject.getJSONArray("contact_items")
//                        CoroutineScope(Dispatchers.Main).launch {
//                            if (json.length() > 0) {
//                                isContactList = true
//                                contact = json.toString()
//                                var temp=Gson().fromJson(contact, Array<ContactItem>::class.java).toList()
//                                temp.forEachIndexed { index, item ->
//                                    optionList.add(RadioGroup(item.name, item.phone!!))
//                                }
//                                Log.d("Contatc",temp.toString())
//
//                            }
//                        }
//                    }
//                }
//            }
//
//            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                val audioData = bytes.toByteArray()
//                if (audioData.isNotEmpty()) {
//                    audioQueue.put(audioData)
//                }
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
//    // ------------------ Audio Recording ------------------
//    DisposableEffect(isRecording) {
//        if (isRecording && micPermissionGranted.value) {
//            val bufferSize = AudioRecord.getMinBufferSize(
//                sampleRate,
//                AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT
//            ).coerceAtLeast(sampleRate / 10)
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
//                while (isActive && isRecording) {
//                    if (isServerSpeaking) {
//                        delay(50)
//                        continue
//                    }
//                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
//                    if (read > 0) {
//                        webSocket?.send(buffer.toByteString(0, read))
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
//            }
//        } else {
//            onDispose { }
//        }
//    }


    LaunchedEffect(agentId) {
        if (!micPermissionGranted.value) return@LaunchedEffect

        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
//        ${token.getId()}

        val token = TokenManager.getInstance(context)
        val request = Request.Builder()
            .url("wss://api-mvp.lumalife.de/ws/user/${token.getId()}")
            .build()

        val wsListener = object : WebSocketListener() {

            override fun onOpen(ws: WebSocket, response: Response) {
                webSocket = ws
                Log.d("WebSocket", "Connection opened successfully")

                // ============================================================================
                // AUDIO PLAYBACK SYSTEM - With Interruption Support
                // ============================================================================
                playbackJob = CoroutineScope(Dispatchers.IO).launch {
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
                        Log.d("AudioSetup", "AudioTrack started with VOICE_COMMUNICATION")
                    }

                    // ============================================================================
                    // SMART PLAYBACK LOOP - Handles Interruptions Properly
                    // ============================================================================
                    while (isActive && !shouldStopPlayback) {
                        try {
                            // ============================================================================
                            // INTERRUPTION HANDLER - Stop AI immediately when user speaks
                            // ============================================================================
                            // Check if user interrupted (state changed to INTERRUPTED in VAD loop)
                            if (voiceState == VoiceState.INTERRUPTED && isServerSpeaking) {
                                // CRITICAL: User interrupted - stop AI playback immediately!
                                Log.w("Interruption", "🔴 INTERRUPTION DETECTED - Stopping AI playback NOW!")

                                // 1. Stop playback immediately
                                audioTrack?.pause()
                                audioTrack?.flush()
                                Log.d("Interruption", "✅ Audio playback stopped")

                                // 2. Clear the entire audio queue (discard remaining AI speech)
                                val queueSize = audioQueue.size
                                audioQueue.clear()
                                Log.d("Interruption", "✅ Cleared $queueSize audio chunks from queue")

                                // 3. Send interruption signal to backend
                                val interruptMsg = JSONObject().apply {
                                    put("type", "interruption")
                                    put("timestamp", System.currentTimeMillis())
                                }
                                webSocket?.send(interruptMsg.toString())
                                Log.d("Interruption", "✅ Sent interruption signal to backend")

                                // 4. Update state
                                isServerSpeaking = false
                                Log.d("Interruption", "✅ Switched to user input mode - ready to receive user speech")

                                // 5. Resume playback (ready for next AI response after user finishes)
                                audioTrack?.play()

                                // Wait a bit before checking queue again
                                delay(50)
                                continue
                            }

                            // Normal playback - poll with timeout instead of blocking take()
                            val audioData = audioQueue.poll(50, TimeUnit.MILLISECONDS)

                            if (audioData != null && audioData.isNotEmpty()) {
                                // Mark that server is speaking
                                if (!isServerSpeaking) {
                                    isServerSpeaking = true
                                    voiceState = VoiceState.AI_SPEAKING
                                    Log.d("Playback", "AI started speaking")
                                }

                                // Amplify audio for louder playback (2.5x boost)
                                val amplifiedAudio = amplifyAudio(audioData, 2.5f)

                                // Write amplified audio data
                                audioTrack?.write(amplifiedAudio, 0, amplifiedAudio.size)

                                // Check if queue is empty (AI finished speaking)
                                if (audioQueue.isEmpty()) {
                                    delay(200) // Grace period to ensure all audio played
                                    if (audioQueue.isEmpty()) {
                                        isServerSpeaking = false
                                        lastAiSpeechEndTime = System.currentTimeMillis() // Track when AI finished
                                        voiceState = VoiceState.IDLE
                                        Log.d("Playback", "AI finished speaking - waiting 500ms before listening")
                                    }
                                }
                            } else {
                                // No audio available - check if we should mark as finished
                                if (isServerSpeaking && audioQueue.isEmpty()) {
                                    delay(300) // Wait to confirm no more audio coming
                                    if (audioQueue.isEmpty()) {
                                        isServerSpeaking = false
                                        lastAiSpeechEndTime = System.currentTimeMillis() // Track when AI finished
                                        voiceState = VoiceState.IDLE
                                        Log.d("Playback", "AI finished speaking (timeout) - waiting 500ms before listening")
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("Playback", "Error in playback loop", e)
                            delay(100)
                        }
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket",text.toString())

                try {
                    val jsonObject = JSONObject(text)
                    Log.d("WebSocket", "Received message: ${jsonObject.optString("type")}")

                    when (jsonObject.optString("type")) {
                        "calendar_update" -> {
                            val json = jsonObject.getJSONArray("calendar_items")
                            CoroutineScope(Dispatchers.Main).launch {
                                if (json.length() > 0) {
                                    isCalander = true
                                    information = json.toString()
                                    val parsed = Gson().fromJson(information, Array<CalendarItem>::class.java).toList()
                                    calendarList.clear()
                                    calendarList.addAll(parsed)

                                    //calendarList.value = Gson().fromJson(information, Array<CalendarItem>::class.java).toList()


                                }
                            }
                        }

                        "contact_update" -> {
                            val json = jsonObject.getJSONArray("contact_items")
                            CoroutineScope(Dispatchers.Main).launch {
                                if (json.length() > 0) {
                                    isContactList = true
                                    contact = json.toString()
                                    var temp=Gson().fromJson(contact, Array<ContactItem>::class.java).toList()
                                    temp.forEachIndexed { index, item ->
                                        optionList.add(RadioGroup(item.name, item.phone!!))
                                    }
                                    Log.d("Contatc",temp.toString())

                                }
                            }
                        }

                        "interruption_ack" -> {
                            // Backend acknowledged interruption
                            Log.d("Interruption", "Backend acknowledged interruption")
                            CoroutineScope(Dispatchers.Main).launch {
                                voiceState = VoiceState.USER_SPEAKING
                            }
                        }

                        "response_complete" -> {
                            // Backend finished sending response
                            Log.d("WebSocket", "Backend finished response")
                            CoroutineScope(Dispatchers.Main).launch {
                                // Mark that no more audio is coming
                                delay(500)
                                if (audioQueue.isEmpty()) {
                                    isServerSpeaking = false
                                    voiceState = VoiceState.IDLE
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error processing text message", e)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                try {
                    val audioData = bytes.toByteArray()
                    if (audioData.isNotEmpty()) {
                        // Only add to queue if not interrupted
                        if (voiceState != VoiceState.INTERRUPTED && !userSpeechDetected) {
                            audioQueue.offer(audioData)
                            Log.d("WebSocket", "Audio chunk received: ${audioData.size} bytes, queue size: ${audioQueue.size}")
                        } else {
                            Log.d("Interruption", "Discarding audio chunk - user is speaking")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("WebSocket", "Error processing audio message", e)
                }
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket", "Connection error: ${t.message}", t)
                CoroutineScope(Dispatchers.Main).launch {
                    voiceState = VoiceState.IDLE
                    isServerSpeaking = false
                }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Connection closed: $code - $reason")
                CoroutineScope(Dispatchers.Main).launch {
                    shouldStopPlayback = true
                    playbackJob?.cancel()
                }
            }
        }

        webSocket = client.newWebSocket(request, wsListener)
    }

    // ============================================================================
    // AUDIO RECORDING - With Voice Activity Detection (VAD) for Interruption
    // ============================================================================
    DisposableEffect(isRecording) {
        if (isRecording && micPermissionGranted.value) {
            val bufferSize = AudioRecord.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT
            ).coerceAtLeast(sampleRate / 10)

            audioRecord = AudioRecord(
                MediaRecorder.AudioSource.VOICE_COMMUNICATION, // Better for voice calls (has built-in echo cancellation)
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            ).apply {
                // Enable acoustic echo cancellation if available
                if (AcousticEchoCanceler.isAvailable()) {
                    val echoCanceler = AcousticEchoCanceler.create(audioSessionId)
                    echoCanceler?.enabled = true
                    Log.d("AudioSetup", "Acoustic Echo Canceler enabled: ${echoCanceler?.enabled}")
                } else {
                    Log.w("AudioSetup", "Acoustic Echo Canceler NOT available on this device")
                }

                // Enable noise suppression if available
                if (NoiseSuppressor.isAvailable()) {
                    val noiseSuppressor = NoiseSuppressor.create(audioSessionId)
                    noiseSuppressor?.enabled = true
                    Log.d("AudioSetup", "Noise Suppressor enabled: ${noiseSuppressor?.enabled}")
                } else {
                    Log.w("AudioSetup", "Noise Suppressor NOT available on this device")
                }

                startRecording()
                Log.d("AudioSetup", "AudioRecord started with VOICE_COMMUNICATION source")
            }

            val job = CoroutineScope(Dispatchers.IO).launch {
                val buffer = ByteArray(bufferSize)
                val energyThreshold = 500 // Threshold for voice detection
                var consecutiveVoiceFrames = 0
                var consecutiveSilenceFrames = 0

                while (isActive && isRecording) {
                    // ============================================================================
                    // ALWAYS READ FROM MIC - To enable real-time interruption
                    // ============================================================================
                    // We MUST read from mic even when AI is speaking to detect interruptions
                    // Echo prevention is handled by energy threshold checks below

                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
                    if (read > 0) {
                        // ============================================================================
                        // VOICE ACTIVITY DETECTION (VAD) - Detect User Speech
                        // ============================================================================
                        val energy = calculateEnergy(buffer)
                        val isVoiceDetected = energy > energyThreshold

                        if (isVoiceDetected) {
                            consecutiveVoiceFrames++
                            consecutiveSilenceFrames = 0

                            // ============================================================================
                            // REAL-TIME INTERRUPTION DETECTION
                            // ============================================================================
                            // Handle different states with appropriate thresholds

                            if (voiceState == VoiceState.AI_SPEAKING) {
                                // *** INTERRUPTION MODE ***
                                // User is speaking while AI is speaking - this is an interruption!
                                // Use HIGHER threshold to avoid echo, but detect quickly (2 frames)
                                val interruptionThreshold = energyThreshold * 1.5 // 50% higher to avoid echo

                                if (consecutiveVoiceFrames >= 2 && energy > interruptionThreshold) {
                                    if (!userSpeechDetected) {
                                        userSpeechDetected = true
                                        voiceState = VoiceState.INTERRUPTED
                                        Log.w("Interruption", "🔴 USER INTERRUPTED AI! Energy: $energy (threshold: $interruptionThreshold)")
                                        Log.w("Interruption", "Stopping AI playback and switching to user input")
                                    }
                                } else if (energy <= interruptionThreshold) {
                                    // Log potential echo being ignored
                                    if (consecutiveVoiceFrames % 20 == 0) {
                                        Log.d("VAD", "Ignoring echo during AI speech - energy: $energy (need > $interruptionThreshold)")
                                    }
                                }
                            } else if (voiceState == VoiceState.IDLE) {
                                // *** NORMAL SPEECH DETECTION ***
                                // User starting to speak when AI is not speaking
                                if (consecutiveVoiceFrames >= 3) {
                                    if (!userSpeechDetected) {
                                        // Check if enough time has passed since AI finished (prevents echo tail)
                                        val timeSinceAiFinished = System.currentTimeMillis() - lastAiSpeechEndTime
                                        val echoPreventionDelay = 500L

                                        if (timeSinceAiFinished > echoPreventionDelay || lastAiSpeechEndTime == 0L) {
                                            userSpeechDetected = true
                                            voiceState = VoiceState.USER_SPEAKING
                                            Log.d("VAD", "✅ User speech detected - energy: $energy")
                                        } else {
                                            Log.d("VAD", "Ignoring echo tail - only ${timeSinceAiFinished}ms since AI finished")
                                        }
                                    }
                                }
                            }
                            // If already in USER_SPEAKING or INTERRUPTED state, keep detecting voice
                        } else {
                            consecutiveSilenceFrames++
                            consecutiveVoiceFrames = 0

                            // User stopped speaking - need more silence frames to confirm
                            if (consecutiveSilenceFrames >= 20 && userSpeechDetected) { // Increased from 15 to 20 for better tolerance
                                userSpeechDetected = false
                                Log.d("VAD", "User speech ended")

                                if (voiceState == VoiceState.USER_SPEAKING || voiceState == VoiceState.INTERRUPTED) {
                                    // Send end-of-speech signal
                                    val eosMsg = JSONObject().apply {
                                        put("type", "end_of_speech")
                                        put("timestamp", System.currentTimeMillis())
                                    }
                                    webSocket?.send(eosMsg.toString())
                                    Log.d("VAD", "Sent end_of_speech signal")
                                    voiceState = VoiceState.IDLE
                                }
                            }
                        }

                        // ============================================================================
                        // SEND AUDIO TO SERVER - Send when user is speaking
                        // ============================================================================
                        // Send audio when user is speaking (all frames, not just voice frames)
                        // This ensures continuous audio stream including pauses between words
                        val shouldSendAudio = when (voiceState) {
                            VoiceState.USER_SPEAKING -> true
                            VoiceState.INTERRUPTED -> true // Send during interruption
                            VoiceState.IDLE -> false // Don't send when idle
                            VoiceState.AI_SPEAKING -> false // Don't send when AI is speaking
                        }

                        // Send ALL audio frames when user is speaking (including silence between words)
                        if (shouldSendAudio) {
                            try {
                                webSocket?.send(buffer.toByteString(0, read))

                                // Enhanced logging for interruption tracking
                                if (voiceState == VoiceState.INTERRUPTED) {
                                    // Log first few frames of interruption
                                    if (consecutiveVoiceFrames <= 5) {
                                        Log.d("AudioSend", "📤 Sending INTERRUPTION audio: ${read} bytes, energy: $energy")
                                    }
                                } else if (consecutiveVoiceFrames % 10 == 0) {
                                    // Log every 10th frame for normal speech
                                    Log.d("AudioSend", "📤 Sending audio: ${read} bytes, state: $voiceState, energy: $energy")
                                }
                            } catch (e: Exception) {
                                Log.e("AudioSend", "Error sending audio", e)
                            }
                        } else {
                            // Log when we're NOT sending (for debugging)
                            if (isVoiceDetected && consecutiveVoiceFrames == 1) {
                                Log.d("AudioSend", "⏸️ NOT sending - state: $voiceState, energy: $energy")
                            }
                        }
                    }
                }
            }

            onDispose {
                Log.d("Lifecycle", "Disposing audio recording")
                job.cancel()
                audioRecord?.stop()
                audioRecord?.release()
                audioRecord = null

                // Clean up playback
                shouldStopPlayback = true
                playbackJob?.cancel()
                audioTrack?.stop()
                audioTrack?.flush()
                audioTrack?.release()
                audioTrack = null

                // Close WebSocket
                webSocket?.close(1000, "User stopped recording")
                webSocket = null

                // Reset state
                isRecording = false
                isServerSpeaking = false
                voiceState = VoiceState.IDLE
                userSpeechDetected = false
                audioQueue.clear()
            }
        } else {
            onDispose { }
        }
    }
}


fun amplifyAudio(audioData: ByteArray, gain: Float): ByteArray {
    val amplified = ByteArray(audioData.size)

    for (i in audioData.indices step 2) {
        // Read 16-bit PCM sample (little-endian)
        val sample = ((audioData[i + 1].toInt() shl 8) or (audioData[i].toInt() and 0xFF)).toShort()

        // Apply gain
        var amplifiedSample = (sample * gain).toInt()

        // Prevent clipping (keep within 16-bit range: -32768 to 32767)
        amplifiedSample = amplifiedSample.coerceIn(-32768, 32767)

        // Write back as 16-bit PCM (little-endian)
        amplified[i] = (amplifiedSample and 0xFF).toByte()
        amplified[i + 1] = ((amplifiedSample shr 8) and 0xFF).toByte()
    }

    return amplified
}


//@Composable
//fun OnBordingScreen5(navController: NavController) {
//    val context = LocalContext.current
//    val sampleRate = 16000
//    val agentId = "5066"
//
//    var isRecording by remember { mutableStateOf(false) }
//    var isCalander by remember { mutableStateOf(false) }
//    var isContactList by remember { mutableStateOf(false) }
//
//    val audioQueue = remember { LinkedBlockingQueue<ByteArray>() }
//    var audioTrack by remember { mutableStateOf<AudioTrack?>(null) }
//    var audioRecord by remember { mutableStateOf<AudioRecord?>(null) }
//    var webSocket by remember { mutableStateOf<WebSocket?>(null) }
//
//    var information by remember { mutableStateOf("") }
//    var contact by remember { mutableStateOf("") }
//
//    val micPermissionGranted = remember {
//        mutableStateOf(
//            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
//                    PackageManager.PERMISSION_GRANTED
//        )
//    }
//
//    val permissionLauncher = rememberLauncherForActivityResult(
//        ActivityResultContracts.RequestPermission()
//    ) { granted -> micPermissionGranted.value = granted }
//
//    LaunchedEffect(Unit) {
//        if (!micPermissionGranted.value) {
//            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//        }
//    }
//
//    BackHandler {
//        webSocket?.close(1000, "User closed connection")
//        webSocket = null
//        navController.navigate(NavRoute.OnBordingScreen4)
//    }
//
//    // --- WebSocket + AudioTrack initialization ---
//    LaunchedEffect(agentId) {
//        if (!micPermissionGranted.value) return@LaunchedEffect
//
//        val client = OkHttpClient.Builder()
//            .readTimeout(0, TimeUnit.MILLISECONDS)
//            .retryOnConnectionFailure(true)
//            .build()
//
//        val token = TokenManager.getInstance(context)
//        val request = Request.Builder()
//            .url("wss://api-mvp.lumalife.de/ws/agents/${token.getId()}")
//            .build()
//
//        val wsListener = object : WebSocketListener() {
//            override fun onOpen(ws: WebSocket, response: Response) {
//                webSocket = ws
//
//                // --- Audio playback coroutine ---
//                CoroutineScope(Dispatchers.IO).launch {
//                    if (audioTrack == null) {
//                        val minBuffer = AudioTrack.getMinBufferSize(
//                            sampleRate,
//                            AudioFormat.CHANNEL_OUT_MONO,
//                            AudioFormat.ENCODING_PCM_16BIT
//                        ) * 6
//
//                        audioTrack = AudioTrack.Builder()
//                            .setAudioAttributes(
//                                AudioAttributes.Builder()
//                                    .setUsage(AudioAttributes.USAGE_MEDIA)
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
//
//                        audioTrack?.play()
//                    }
//
//                    while (isActive) {
//                        val audioData = audioQueue.poll() // non-blocking
//                        if (audioData != null) {
//                            audioTrack?.write(audioData, 0, audioData.size)
//                        } else {
//                            delay(10) // avoid busy loop
//                        }
//                    }
//                }
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                val jsonObject = JSONObject(text)
//                when (jsonObject.optString("type")) {
//                    "calendar_update" -> {
//                        val json = jsonObject.getJSONArray("calendar_items")
//                        CoroutineScope(Dispatchers.Main).launch {
//                            if (json.length() > 0) {
//                                isCalander = true
//                                information = json.toString()
//                            }
//                        }
//                    }
//
//                    "contact_update" -> {
//                        val json = jsonObject.getJSONArray("contact_items")
//                        CoroutineScope(Dispatchers.Main).launch {
//                            if (json.length() > 0) {
//                                isContactList = true
//                                contact = json.toString()
//                            }
//                        }
//                    }
//                }
//            }
//
//            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                val audioData = bytes.toByteArray()
//                if (audioData.isNotEmpty()) {
//                    audioQueue.put(audioData)
//                }
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
//    // --- AudioRecord + sending to server ---
//    DisposableEffect(isRecording) {
//        if (isRecording && micPermissionGranted.value) {
//            val bufferSize = AudioRecord.getMinBufferSize(
//                sampleRate,
//                AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT
//            ).coerceAtLeast(sampleRate / 10)
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
//
//                while (isActive && isRecording) {
//                    val read = audioRecord?.read(buffer, 0, buffer.size) ?: 0
//                    if (read > 0) {
//                        // send mic audio immediately
//                        webSocket?.send(buffer.toByteString(0, read))
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
//            }
//        } else {
//            onDispose { }
//        }
//    }
//}




fun calculateEnergy(audioData: ByteArray): Int {
    var sum = 0.0
    for (i in audioData.indices step 2) {
        val sample = (audioData[i + 1].toInt() shl 8) or (audioData[i].toInt() and 0xFF)
        sum += sample * sample
    }
    val rms = sqrt(sum / (audioData.size / 2))
    return rms.toInt()
}





@Composable
fun calander() {



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