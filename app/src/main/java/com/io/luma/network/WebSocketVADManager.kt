package com.io.luma.network


// ============================================================================
// IMPORTS - These bring in necessary Android and Kotlin libraries
// ============================================================================

import android.media.AudioRecord                    // For recording audio from microphone
import android.media.MediaRecorder                  // For audio recording configuration
import android.util.Base64                          // For encoding audio data to base64 string
import android.util.Log                             // For logging debug messages
import kotlinx.coroutines.*                         // For asynchronous operations (async/await)
import okhttp3.OkHttpClient                         // HTTP client for WebSocket connections
import okhttp3.Request                              // For creating WebSocket requests
import okhttp3.WebSocket                            // WebSocket interface
import okhttp3.WebSocketListener                    // Listener for WebSocket events
import org.json.JSONObject                          // For creating and parsing JSON messages
import java.util.concurrent.TimeUnit                // For timeout configurations
import kotlin.math.sqrt                             // For calculating square root (RMS calculation)

/**
 * WebSocket Manager with VAD (Voice Activity Detection) for Android
 *
 * This class handles:
 * 1. WebSocket connection to the server
 * 2. Real-time audio recording from microphone
 * 3. Voice Activity Detection (VAD) to identify when user is speaking
 * 4. Sending audio data to server only when voice is detected
 * 5. Managing connection state and error handling
 */
class WebSocketVADManager(
    // Constructor parameters - passed when creating the manager
    private val wsUrl: String,              // WebSocket server URL (e.g., "wss://example.com/ws")
    private val userToken: String,          // Authentication token for the user
    private val agentRole: String,          // Role of the agent (e.g., "OBA", "PPCA")
    private val language: String            // Language for the conversation (e.g., "en", "es")
) : WebSocketListener()
{                   // Inherit from WebSocketListener to handle WebSocket events

    // ========================================================================
    // PRIVATE VARIABLES - Internal state management
    // ========================================================================

    private var webSocket: WebSocket? = null       // Holds the WebSocket connection (nullable)
    private var audioRecord: AudioRecord? = null   // Holds the audio recorder instance (nullable)
    private var isRecording = false                // Flag: true when actively recording audio
    private var isConnected = false                // Flag: true when WebSocket is connected

    // Coroutine scope for managing async operations
    // Dispatchers.Default = runs on background thread
    // Job() = allows cancellation of all coroutines when needed
    private val scope = CoroutineScope(Dispatchers.Default + Job())

    // ========================================================================
    // VAD (VOICE ACTIVITY DETECTION) CONFIGURATION
    // ========================================================================
    // These settings control how the VAD algorithm detects voice

    private val vadConfig = VADConfig(
        voiceTrigger = 0.05f,                // Energy threshold to START detecting voice (0.05 = 5%)
        voiceStop = 0.03f,                   // Energy threshold to STOP detecting voice (0.03 = 3%)
        smoothingTimeConstant = 0.95f,       // Smoothing factor (0.95 = 95% previous + 5% new)
        minVoiceFrames = 8,                  // Minimum consecutive frames needed to confirm voice
        energyThreshold = 0.04f,             // Minimum energy to filter out background noise
        bufferSize = 1024                    // Size of audio buffer for processing
    )

    // VAD context stores the current state of voice detection
    private val vadContext = VADContext()

    // ========================================================================
    // CALLBACK FUNCTIONS - Called when events occur
    // ========================================================================
    // These are optional functions that can be set by the caller to respond to events

    var onConnectionStateChange: ((Boolean) -> Unit)? = null  // Called when connection status changes
    var onVoiceStart: (() -> Unit)? = null                    // Called when voice is detected
    var onVoiceEnd: (() -> Unit)? = null                      // Called when voice stops
    var onError: ((String) -> Unit)? = null                   // Called when an error occurs

    /**
     * Connect to WebSocket server
     *
     * This function:
     * 1. Creates an HTTP client with timeout settings
     * 2. Builds the WebSocket URL with authentication parameters
     * 3. Initiates the connection
     * 4. Handles any connection errors
     */
    fun connect() {
        try {
            // Create an OkHttpClient with custom timeout settings
            val client = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)      // Wait max 5 seconds to connect
                .readTimeout(0, TimeUnit.MILLISECONDS)    // No timeout for reading (WebSocket is long-lived)
                .build()

            // Build the complete WebSocket URL with query parameters
            // Format: wss://example.com/ws?token=ABC123&agent_role=OBA&language=en
            val url = "$wsUrl?token=${userToken}&agent_role=${agentRole}&language=${language}"

            // Create a request object for the WebSocket connection
            val request = Request.Builder().url(url).build()

            // Create the WebSocket connection
            // 'this' means this class (WebSocketVADManager) will handle the WebSocket events
            webSocket = client.newWebSocket(request, this)

            // Log the connection attempt for debugging
            Log.d(TAG, "WebSocket connection initiated to: $url")
        } catch (e: Exception) {
            // If any error occurs during connection setup, handle it gracefully
            Log.e(TAG, "Error connecting to WebSocket", e)
            // Call the error callback to notify the caller
            onError?.invoke("Connection failed: ${e.message}")
        }
    }

    /**
     * Disconnect from WebSocket
     *
     * This function:
     * 1. Stops recording audio
     * 2. Closes the WebSocket connection
     * 3. Cancels all pending coroutines
     */
    fun disconnect() {
        // Stop recording audio first
        stopRecording()

        // Close the WebSocket connection gracefully
        // 1000 = normal closure code
        // "Client disconnecting" = reason message
        webSocket?.close(1000, "Client disconnecting")

        // Cancel all coroutines in the scope to clean up resources
        scope.cancel()
    }

    /**
     * Start recording audio with VAD
     *
     * This function:
     * 1. Checks if already recording (prevents duplicate recording)
     * 2. Calculates the minimum buffer size needed
     * 3. Creates an AudioRecord instance
     * 4. Starts recording and begins VAD processing
     * 5. Runs in a background coroutine to not block the main thread
     */
    fun startRecording() {
        // If already recording, exit early to prevent duplicate recording
        if (isRecording) return

        // Launch a coroutine on the Default dispatcher (background thread)
        scope.launch @androidx.annotation.RequiresPermission(android.Manifest.permission.RECORD_AUDIO) {
            try {
                // Set flag to indicate recording is active
                isRecording = true

                // Calculate the minimum buffer size required by Android
                // This depends on sample rate, channel configuration, and audio format
                val bufferSize = AudioRecord.getMinBufferSize(
                    SAMPLE_RATE,                    // 24000 Hz
                    CHANNEL_CONFIG,                 // Mono (1 channel)
                    AUDIO_FORMAT                    // PCM 16-bit
                )

                // Create an AudioRecord instance to capture microphone audio
                audioRecord = AudioRecord(
                    MediaRecorder.AudioSource.MIC,  // Source: microphone
                    SAMPLE_RATE,                    // 24000 Hz sample rate
                    CHANNEL_CONFIG,                 // Mono audio
                    AUDIO_FORMAT,                   // 16-bit PCM format
                    bufferSize * 2                  // Buffer size (doubled for safety)
                )

                // Start the audio recording
                audioRecord?.startRecording()
                Log.d(TAG, "Recording started")

                // Begin processing audio with VAD detection
                recordAudioWithVAD(bufferSize)
            } catch (e: Exception) {
                // If any error occurs, log it and notify the caller
                Log.e(TAG, "Error starting recording", e)
                onError?.invoke("Recording failed: ${e.message}")
                isRecording = false
            }
        }
    }

    /**
     * Stop recording audio
     *
     * This function:
     * 1. Sets recording flag to false
     * 2. Stops the AudioRecord
     * 3. Releases resources
     * 4. Cleans up the reference
     */
    fun stopRecording() {
        // Set flag to stop the recording loop
        isRecording = false

        // Stop the audio recording
        audioRecord?.stop()

        // Release the AudioRecord resources (important for memory management)
        audioRecord?.release()

        // Set reference to null to allow garbage collection
        audioRecord = null

        // Log the stop action for debugging
        Log.d(TAG, "Recording stopped")
    }

    /**
     * Record audio with VAD detection
     *
     * This is a suspend function (runs in a coroutine) that:
     * 1. Continuously reads audio from the microphone
     * 2. Analyzes each audio chunk for voice activity
     * 3. Sends audio to server only when voice is detected
     * 4. Notifies when voice starts and stops
     *
     * @param bufferSize Size of the audio buffer to read at a time
     */
    private suspend fun recordAudioWithVAD(bufferSize: Int) {
        // Create a buffer to hold audio samples (16-bit integers)
        val audioBuffer = ShortArray(bufferSize)

        // Loop while recording is active AND WebSocket is connected
        while (isRecording && isConnected) {
            // Read audio data from the microphone into the buffer
            // Returns the number of samples read, or 0 if no data
            val readSize = audioRecord?.read(audioBuffer, 0, bufferSize) ?: 0

            // Only process if we actually read some audio data
            if (readSize > 0) {
                // Convert 16-bit integer samples to float (-1.0 to 1.0 range)
                // This is needed for VAD analysis
                // Division by 32768 converts from 16-bit range to float range
                val floatBuffer = FloatArray(readSize) { i ->
                    audioBuffer[i] / 32768f  // Convert: -32768 to 32767 becomes -1.0 to 1.0
                }

                // Analyze the audio chunk to detect if it contains voice
                val hasVoice = detectVoiceActivity(floatBuffer)

                // If voice is detected
                if (hasVoice) {
                    // If this is the first voice chunk (transition from silence to voice)
                    if (!vadContext.isStreaming) {
                        // Mark that we're now streaming voice
                        vadContext.isStreaming = true
                        // Notify the caller that voice has started
                        onVoiceStart?.invoke()
                    }

                    // Send the audio data to the server
                    sendAudioData(audioBuffer.sliceArray(0 until readSize))
                }
                // If no voice is detected but we were streaming
                else if (vadContext.isStreaming) {
                    // Mark that we're no longer streaming
                    vadContext.isStreaming = false
                    // Notify the caller that voice has stopped
                    onVoiceEnd?.invoke()
                }
            }

            // Small delay to prevent busy waiting (consuming CPU)
            // This allows other coroutines to run
            delay(10)
        }
    }

    /**
     * Detect voice activity using energy and zero-crossing rate
     *
     * This function uses TWO features to detect voice:
     * 1. RMS Energy: Measures the loudness of the audio
     * 2. Zero-Crossing Rate: Measures how often the audio signal changes sign
     *
     * Voice typically has:
     * - Higher energy (louder)
     * - Moderate zero-crossing rate (not too many sign changes)
     *
     * Background noise typically has:
     * - Lower energy or very high energy
     * - Very high zero-crossing rate (lots of random changes)
     *
     * @param inputData Audio samples as float array (-1.0 to 1.0)
     * @return true if voice is detected, false otherwise
     */
    private fun detectVoiceActivity(inputData: FloatArray): Boolean {
        // ====================================================================
        // STEP 1: Calculate RMS (Root Mean Square) Energy
        // ====================================================================
        // RMS measures the loudness/energy of the audio signal
        // Formula: RMS = sqrt(sum(sample^2) / number_of_samples)

        var rms = 0f                    // Initialize RMS accumulator
        var zeroCrossings = 0           // Initialize zero-crossing counter

        // Sum up the squares of all samples
        for (i in inputData.indices) {
            rms += inputData[i] * inputData[i]  // Square each sample and add to sum
        }
        // Take the square root and divide by number of samples
        rms = sqrt(rms / inputData.size)

        // ====================================================================
        // STEP 2: Calculate Zero-Crossing Rate (ZCR)
        // ====================================================================
        // ZCR counts how many times the audio signal crosses zero
        // Voice has moderate ZCR, noise has very high ZCR

        // Get the sign of the first sample (positive or negative)
        var sign = if (inputData[0] > 0) 1 else -1

        // Check each sample to see if it crosses zero
        for (i in 1 until inputData.size) {
            // Get the sign of the current sample
            val nextSign = if (inputData[i] > 0) 1 else -1

            // If sign changed, we have a zero crossing
            if (sign != nextSign) {
                zeroCrossings++
            }
            sign = nextSign
        }

        // Calculate ZCR as a ratio (0.0 to 1.0)
        val zcr = zeroCrossings.toFloat() / (inputData.size - 1)

        // ====================================================================
        // STEP 3: Apply Smoothing to Reduce Noise
        // ====================================================================
        // Smoothing makes the detection more stable by blending with previous values
        // This prevents rapid on/off switching due to noise

        if (vadContext.history.isNotEmpty()) {
            // Get the last recorded values
            val lastEntry = vadContext.history.last()

            // Blend current RMS with previous RMS
            // Formula: smoothed = (constant * previous) + ((1 - constant) * current)
            // With constant = 0.95: 95% previous + 5% current
            rms = vadConfig.smoothingTimeConstant * lastEntry.rms +
                    (1 - vadConfig.smoothingTimeConstant) * rms

            // Blend current ZCR with previous ZCR
            vadContext.zeroCrossingRate = vadConfig.smoothingTimeConstant * lastEntry.zcr +
                    (1 - vadConfig.smoothingTimeConstant) * zcr
        }

        // ====================================================================
        // STEP 4: Keep History for Smoothing
        // ====================================================================
        // Store the current values for next iteration's smoothing

        vadContext.history.add(VADHistoryEntry(rms, zcr))

        // Keep only the last 10 entries to save memory
        if (vadContext.history.size > 10) {
            vadContext.history.removeAt(0)  // Remove oldest entry
        }

        // ====================================================================
        // STEP 5: Multi-Feature Voice Detection
        // ====================================================================
        // Use BOTH energy and zero-crossing rate to detect voice

        // Check if RMS energy is above the voice trigger threshold
        val isVoiceEnergy = rms > vadConfig.voiceTrigger

        // Check if ZCR is in the typical voice range (0.1 to 0.5)
        // Voice typically has ZCR between 0.1 and 0.5
        // Noise has very high ZCR (> 0.5) or very low ZCR (< 0.1)
        val isVoiceZCR = vadContext.zeroCrossingRate > 0.1f && vadContext.zeroCrossingRate < 0.5f

        // Voice is detected only if BOTH conditions are true
        val isVoice = isVoiceEnergy && isVoiceZCR

        // ====================================================================
        // STEP 6: State Tracking with Hysteresis
        // ====================================================================
        // Hysteresis prevents rapid switching between voice/silence
        // We require multiple consecutive frames to confirm voice

        if (isVoice) {
            // Increment the counter of consecutive voice frames
            vadContext.consecutiveVoiceFrames++

            // Reset silence counter
            vadContext.silenceFrames = 0

            // Only return true if we have enough consecutive voice frames
            // This prevents false positives from brief noise spikes
            return vadContext.consecutiveVoiceFrames >= vadConfig.minVoiceFrames
        } else {
            // Reset consecutive voice frame counter
            vadContext.consecutiveVoiceFrames = 0

            // If RMS is below the stop threshold, increment silence counter
            if (rms < vadConfig.voiceStop) {
                vadContext.silenceFrames++
            }

            // Return true if we haven't exceeded max silence frames
            // This allows brief pauses within speech to be considered as voice
            return vadContext.silenceFrames < MAX_SILENCE_FRAMES
        }
    }

    /**
     * Send audio data to WebSocket
     *
     * This function:
     * 1. Converts 16-bit audio samples to bytes
     * 2. Encodes bytes as base64 string (for safe transmission)
     * 3. Wraps in JSON message
     * 4. Sends to server
     *
     * @param audioData Array of 16-bit audio samples
     */
    private fun sendAudioData(audioData: ShortArray) {
        try {
            // Check if WebSocket exists and is connected before sending
            if (webSocket != null && isConnected) {
                // Convert ShortArray to ByteArray
                // This is needed because base64 encoding works with bytes
                val byteArray = audioData.toByteArray()

                // Encode the byte array as base64 string
                // Base64 is a safe encoding that can be transmitted as text
                // Base64.NO_WRAP means no line breaks in the output
                val base64Audio = Base64.encodeToString(byteArray, Base64.NO_WRAP)

                // Create a JSON message with the audio data
                val message = JSONObject().apply {
                    put("type", "audio")              // Message type identifier
                    put("audio", base64Audio)         // Base64-encoded audio data
                }

                // Send the JSON message to the server
                webSocket?.send(message.toString())
            }
        } catch (e: Exception) {
            // Log any errors that occur during sending
            Log.e(TAG, "Error sending audio data", e)
        }
    }

    /**
     * Send JSON message to WebSocket
     *
     * This is a generic function to send any type of message to the server
     *
     * @param type The message type (e.g., "ping", "greeting", "pause")
     * @param data Optional map of additional data to include in the message
     */
    fun sendMessage(type: String, data: Map<String, Any> = emptyMap()) {
        try {
            // Create a JSON object for the message
            val message = JSONObject().apply {
                // Add the message type
                put("type", type)

                // Add the current timestamp (milliseconds since epoch)
                // This helps the server track when messages were sent
                put("timestamp", System.currentTimeMillis())

                // Add any additional data provided by the caller
                // Iterate through the data map and add each key-value pair
                data.forEach { (key, value) ->
                    put(key, value)
                }
            }

            // Send the JSON message to the server
            webSocket?.send(message.toString())
        } catch (e: Exception) {
            // Log any errors that occur during sending
            Log.e(TAG, "Error sending message", e)
        }
    }

    // ========================================================================
    // WEBSOCKET EVENT HANDLERS
    // ========================================================================
    // These functions are called automatically by the WebSocket library
    // when various events occur

    /**
     * Called when WebSocket connection is successfully established
     *
     * @param webSocket The WebSocket connection object
     * @param response The HTTP response from the server
     */
    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        // Set flag to indicate connection is active
        isConnected = true

        // Notify the caller that connection state has changed
        onConnectionStateChange?.invoke(true)

        // Log the successful connection
        Log.d(TAG, "WebSocket connected")

        // Send a ping message to verify the connection is working
        sendMessage("ping")
    }

    /**
     * Called when a text message is received from the server
     *
     * @param webSocket The WebSocket connection object
     * @param text The text message received
     */
    override fun onMessage(webSocket: WebSocket, text: String) {
        try {
            // Parse the incoming text as JSON
            val json = JSONObject(text)

            // Extract and log the message type
            Log.d(TAG, "Message received: ${json.getString("type")}")

            // TODO: Handle different message types here
            // Examples:
            // - "response.audio.delta" - audio from assistant
            // - "response.text.delta" - text from assistant
            // - "conversation.item.input_audio_transcription.completed" - transcription
        } catch (e: Exception) {
            // If JSON parsing fails, log the error
            Log.e(TAG, "Error parsing message", e)
        }
    }

    /**
     * Called when WebSocket connection fails
     *
     * @param webSocket The WebSocket connection object
     * @param t The exception that caused the failure
     * @param response The HTTP response (if available)
     */
    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        // Set flag to indicate connection is lost
        isConnected = false

        // Notify the caller that connection state has changed
        onConnectionStateChange?.invoke(false)

        // Log the error with details
        Log.e(TAG, "WebSocket failure", t)

        // Notify the caller about the error
        onError?.invoke("Connection error: ${t.message}")
    }

    /**
     * Called when WebSocket connection is closed
     *
     * @param webSocket The WebSocket connection object
     * @param code The close code (1000 = normal closure)
     * @param reason The reason for closing
     */
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        // Set flag to indicate connection is closed
        isConnected = false

        // Notify the caller that connection state has changed
        onConnectionStateChange?.invoke(false)

        // Log the closure with reason
        Log.d(TAG, "WebSocket closed: $reason")
    }

    companion object {
        // ====================================================================
        // CONSTANTS - These are shared across all instances of the class
        // ====================================================================

        // Tag for logging (used in Log.d, Log.e, etc.)
        private const val TAG = "WebSocketVADManager"

        // Audio recording sample rate: 24000 Hz (24 kHz)
        // This is the number of audio samples per second
        // Higher sample rate = better audio quality but more data
        private const val SAMPLE_RATE = 24000

        // Audio channel configuration: MONO (single channel)
        // MONO = 1 channel (vs STEREO = 2 channels)
        private const val CHANNEL_CONFIG = android.media.AudioFormat.CHANNEL_IN_MONO

        // Audio format: PCM 16-bit
        // PCM = Pulse Code Modulation (raw audio format)
        // 16-bit = each sample is 16 bits (2 bytes), range: -32768 to 32767
        private const val AUDIO_FORMAT = android.media.AudioFormat.ENCODING_PCM_16BIT

        // Maximum number of consecutive silence frames before stopping voice detection
        // If we have 30 frames of silence, we consider voice has stopped
        private const val MAX_SILENCE_FRAMES = 30
    }
}

// ============================================================================
// DATA CLASSES - These hold configuration and state data
// ============================================================================

/**
 * VADConfig - Configuration parameters for Voice Activity Detection
 *
 * This data class holds all the tunable parameters that control how
 * the VAD algorithm detects voice
 */
data class VADConfig(
    // Energy threshold to START detecting voice (0.05 = 5% of max amplitude)
    // If RMS energy goes above this, we might have voice
    val voiceTrigger: Float,

    // Energy threshold to STOP detecting voice (0.03 = 3% of max amplitude)
    // If RMS energy drops below this, we consider it silence
    val voiceStop: Float,

    // Smoothing factor for exponential moving average (0.95 = 95% old + 5% new)
    // Higher value = more smoothing = slower response but more stable
    val smoothingTimeConstant: Float,

    // Minimum number of consecutive frames needed to confirm voice
    // Prevents false positives from brief noise spikes
    val minVoiceFrames: Int,

    // Minimum energy threshold to filter out background noise
    // Audio below this energy is considered noise
    val energyThreshold: Float,

    // Size of audio buffer for processing (in samples)
    // Larger buffer = more data to analyze but higher latency
    val bufferSize: Int
)

/**
 * VADContext - Current state of the Voice Activity Detection
 *
 * This class tracks the current state of voice detection
 * It's updated as each audio frame is processed
 */
class VADContext {
    // Flag: true if we're currently streaming voice audio
    var isStreaming = false

    // Counter: number of consecutive silence frames
    // Incremented when no voice is detected, reset when voice is detected
    var silenceFrames = 0

    // Counter: number of consecutive voice frames
    // Incremented when voice is detected, reset when silence is detected
    var consecutiveVoiceFrames = 0

    // Current zero-crossing rate (0.0 to 1.0)
    // Updated as each frame is processed
    var zeroCrossingRate = 0f

    // History of previous RMS and ZCR values
    // Used for smoothing calculations
    // Kept as a list of the last 10 entries
    val history = mutableListOf<VADHistoryEntry>()
}

/**
 * VADHistoryEntry - A single entry in the VAD history
 *
 * Stores RMS energy and zero-crossing rate for a single audio frame
 * Used for smoothing calculations
 */
data class VADHistoryEntry(
    val rms: Float,  // RMS energy of this frame
    val zcr: Float   // Zero-crossing rate of this frame
)

// ============================================================================
// EXTENSION FUNCTION - Adds a method to ShortArray
// ============================================================================

/**
 * Convert ShortArray to ByteArray
 *
 * This extension function converts an array of 16-bit integers (ShortArray)
 * to an array of 8-bit integers (ByteArray)
 *
 * Why? Because:
 * - Audio samples are 16-bit integers (ShortArray)
 * - Base64 encoding works with bytes (ByteArray)
 * - We need to convert between these formats
 *
 * How? By splitting each 16-bit value into two 8-bit values:
 * - Low byte: bits 0-7
 * - High byte: bits 8-15
 *
 * @return ByteArray with twice the size of the original ShortArray
 */
fun ShortArray.toByteArray(): ByteArray {
    // Create a ByteArray with twice the size (each Short = 2 Bytes)
    val bytes = ByteArray(size * 2)

    // Iterate through each Short value
    for (i in indices) {
        // Extract the low byte (bits 0-7)
        // 'and 0xFF' masks to keep only the lowest 8 bits
        bytes[i * 2] = (this[i].toInt() and 0xFF).toByte()

        // Extract the high byte (bits 8-15)
        // 'shr 8' shifts right by 8 bits to get the high byte
        // 'and 0xFF' masks to keep only the lowest 8 bits
        bytes[i * 2 + 1] = ((this[i].toInt() shr 8) and 0xFF).toByte()
    }

    return bytes
}

