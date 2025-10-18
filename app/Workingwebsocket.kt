@Composable
fun OnBordingScreen5(navController: NavController) {

    val context = LocalContext.current
    val sampleRate = 16000
    val maxQueueSize = 100
    val agentId = "5066"

    // --- Mic / WebSocket State ---
    var isRecording by remember { mutableStateOf(false) }
    var isCalander by remember { mutableStateOf(false) }
    var isContactList by remember { mutableStateOf(false) }
    var isSpeaking by remember { mutableStateOf(false) }
    var canSendMic by remember { mutableStateOf(true) }
    val audioQueue = remember { LinkedBlockingQueue<ByteArray>() }

    var audioTrack by remember { mutableStateOf<AudioTrack?>(null) }
    var audioRecord by remember { mutableStateOf<AudioRecord?>(null) }
    var webSocket by remember { mutableStateOf<WebSocket?>(null) }
    var information by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }

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
    BackHandler(){
        webSocket?.close(1000, "User closed connection")
        webSocket = null
        navController.navigate(NavRoute.OnBordingScreen4)
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        Column(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)) {

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
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color.White),
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
            if (isCalander)
            {
                val gson = Gson()
                val response = remember {
                    try {
                        gson.fromJson(information, CalendarResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                }
                val calendarList = response?.calendar_items ?: emptyList()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .weight(1f)
                )
                {
                    // Top shadow layer


                    // Card content
                    OutlinedCard(
                        modifier = Modifier.fillMaxSize(),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White,
                        ),
                        border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
                        )
                    ) {

                        DailyRoutineColumnArray(information)
//                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 13.sdp),
//                            horizontalAlignment = Alignment.CenterHorizontally)
//                        {
//                            com.io.luma.customcompose.height(10)
//                            Text("Daily Routing",
//                                style = TextStyle(
//                                    color = textColor,
//                                    fontSize = 22.ssp
//                                ),
//                                modifier = Modifier.fillMaxWidth(),
//                                fontFamily = manropebold,
//                                textAlign = TextAlign.Center
//                            )
//
//                            com.io.luma.customcompose.height(20)
//                             Text(information, style = TextStyle(
//                                 color = textColor,
//                                 fontSize = 16.ssp
//                             ),
//                                 modifier = Modifier.fillMaxWidth(),
//                                 fontFamily = manropebold,
//                                 textAlign = TextAlign.Center)
//                            com.io.luma.customcompose.height(20)
//
//                            CustomButton(modifier = Modifier.fillMaxWidth(),
//                                "Yes") {
//
//                                navController.navigate(NavRoute.OnBordingScreen8)
//                            }
//
//
//
//
//
//
//
//
//                        }
                    }
                }
            }
            else if(isContactList)
            {
                Box(  modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-10).dp)
                    .weight(1f)) {


                    Column(horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center) {

                        Text("Contact_List"+contact.toString(), style = TextStyle(
                            fontSize = 12.ssp
                        ))
                    }
                }
            }
            else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-10).dp)
                        .weight(1f)
                )
                {
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
                                        .align(Alignment.CenterEnd)
                                        .size(30.sdp) // align image to the right

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
                                    .background(
                                        if (isRecording) Color.Red else Color.Gray,
                                        shape = CircleShape
                                    )
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



                            Spacer(modifier = Modifier.height(20.sdp))

                            CustomButton(
                                modifier = Modifier.fillMaxWidth()
                                ,text = "Repeat Question") {

                                navController.navigate(NavRoute.OnBordingScreen6)

                            }
                        }
                    }
                }
            }
        }
    }

    // --- WebSocket + Playback Loop ---
    LaunchedEffect(agentId) {
        if (!micPermissionGranted.value) return@LaunchedEffect

        val client = OkHttpClient.Builder().readTimeout(0, java.util.concurrent.TimeUnit.MILLISECONDS)
            .retryOnConnectionFailure(true)
            .build()
        var token= TokenManager.getInstance(context)

        Log.d("Xccc","wss://api-mvp.lumalife.de/ws/agents/${TokenManager.getInstance().getId()}")
        val request = Request.Builder().url("wss://api-mvp.lumalife.de/ws/agents/${TokenManager.getInstance().getId()}").build()
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
                                    .setUsage(AudioAttributes.USAGE_MEDIA)
                                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
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

                    val tempBuffer = ByteArray(4087)
                    val silenceThreshold = 500 // Adjust to your mic sensitivity
                    var isVoiceDetected = false
                    var silenceCounter = 0
                    val silenceLimit = 15  // More tolerance before marking silence (e.g. ~1s)
                    var lastSpeechTime = System.currentTimeMillis()
                    val gracePeriodMs = 1200L // Keep "talking" state alive for 1.2 sec after last speech

                    while (isActive) {
                        val audioData = audioQueue.take()
                        var offset = 0
                        val chunkSize = 1024

                        // --- VAD: measure RMS energy ---
                        val energy = calculateEnergy(audioData)
                        val currentTime = System.currentTimeMillis()

                        if (energy > silenceThreshold) {
                            isVoiceDetected = true
                            silenceCounter = 0
                            lastSpeechTime = currentTime
                        } else {
                            silenceCounter++
                            // Switch to silence only if grace period passed
                            if (currentTime - lastSpeechTime > gracePeriodMs) {
                                isVoiceDetected = false
                            }
                        }

                        // --- send/play audio only if currently in speech or within grace period ---
                        if (isVoiceDetected) {
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
                        }

                        if (audioQueue.isEmpty()) canSendMic = true
                    }


                    //second

//                    val tempBuffer = ByteArray(4087)
//                    val silenceThreshold = 500 // Adjust based on your mic sensitivity
//                    var isVoiceDetected = false
//                    var silenceCounter = 0
//                    val silenceLimit = 10 // Number of silent chunks before pausing send
//
//                    while (isActive) {
//                        val audioData = audioQueue.take()
//                        var offset = 0
//                        val chunkSize = 1024
//
//                        // --- VAD step ---
//                        val energy = calculateEnergy(audioData)
//                        if (energy > silenceThreshold) {
//                            isVoiceDetected = true
//                            silenceCounter = 0
//                        } else {
//                            silenceCounter++
//                            if (silenceCounter > silenceLimit) {
//                                isVoiceDetected = false
//                            }
//                        }
//
//                        // --- send only if voice detected ---
//                        if (isVoiceDetected) {
//                            while (offset < audioData.size) {
//                                val len = minOf(chunkSize, audioData.size - offset)
//                                if (isSpeaking) {
//                                    for (i in 0 until len step 2) {
//                                        val sample = (audioData[offset + i + 1].toInt() shl 8) or
//                                                (audioData[offset + i].toInt() and 0xFF)
//                                        val ducked = (sample * 0.3).toInt()
//                                        tempBuffer[i] = (ducked and 0xFF).toByte()
//                                        tempBuffer[i + 1] = ((ducked shr 8) and 0xFF).toByte()
//                                    }
//                                    audioTrack?.write(tempBuffer, 0, len)
//                                } else {
//                                    audioTrack?.write(audioData, offset, len)
//                                }
//                                offset += len
//                            }
//                        }
//
//                        if (audioQueue.isEmpty()) canSendMic = true
//                    }



//                    val tempBuffer = ByteArray(4087)
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
                val jsonObject = JSONObject(text)
                val type = jsonObject.optString("type")

                if(type.equals("calendar_update"))
                {
                    val json= jsonObject.getJSONArray("calendar_items")
                    CoroutineScope(Dispatchers.Main).launch{
                        if(json.length()>0)
                        {
                            isCalander=true
                            information=json.toString()
                        }
                    }
                }
                else if(type.equals("calendar_show")) {


                    CoroutineScope(Dispatchers.Main).launch{
                        //  isCalander=true
                    }
                    // navController.navigate(NavRoute.OnBordingScreen7)
                }
                else if(type.equals("contact_update"))
                {
                    val json= jsonObject.getJSONArray("contact_items")

                    CoroutineScope(Dispatchers.Main).launch{
                        if(json.length()>0)
                        {
                            isContactList=true
                            contact=json.toString()
                        }
                    }

                }
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