package com.io.luma.webrtc

import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import org.webrtc.AudioTrack
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.SessionDescription

/**
 * WebRTC Service
 * Main service that coordinates all WebRTC components
 * Matches the iOS WebRTCService architecture
 */
class WebRTCService(
    private val context: Context,
    private val userId: String,
    private val environment: WebRTCConfiguration.ServerEnvironment = WebRTCConfiguration.ServerEnvironment.Production
) {

    private lateinit var audioManager: WebRTCAudioManager
    private lateinit var peerConnectionManager: WebRTCPeerConnectionManager
    private lateinit var webSocketService: WebRTCWebSocketService

    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState

    private val _receivedText = MutableStateFlow("")
    val receivedText: StateFlow<String> = _receivedText

    private val _currentAgent = MutableStateFlow<String?>(null)
    val currentAgent: StateFlow<String?> = _currentAgent

    private val _latency = MutableStateFlow(0)
    val latency: StateFlow<Int> = _latency

    private var lastSpeechTime: Long = 0

    var onCalendarShow: ((List<Any>) -> Unit)? = null
    var onCalendarHide: (() -> Unit)? = null
    var onContactShow: ((List<Any>) -> Unit)? = null
    var onContactHide: (() -> Unit)? = null

    companion object {
        private const val TAG = "WebRTCService"
    }

    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        ERROR
    }

    init {
        initializeComponents()
    }

    /**
     * Initialize all WebRTC components
     */
    private fun initializeComponents() {
        Log.d(TAG, "🔧 ========== INITIALIZING WEBRTC SERVICE ==========")

        // Initialize audio manager
        audioManager = WebRTCAudioManager(context)

        // Initialize peer connection manager
        peerConnectionManager = WebRTCPeerConnectionManager(
            context,
            object : WebRTCPeerConnectionManager.PeerConnectionEventListener {
                override fun onIceCandidate(candidate: IceCandidate) {
                    sendIceCandidate(candidate)
                }

                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
                    // Handle ICE connection state changes
                }

                override fun onConnectionChange(state: PeerConnection.PeerConnectionState) {
                    when (state) {
                        PeerConnection.PeerConnectionState.CONNECTED -> {
                            _connectionState.value = ConnectionState.CONNECTED
                        }
                        PeerConnection.PeerConnectionState.FAILED,
                        PeerConnection.PeerConnectionState.DISCONNECTED -> {
                            _connectionState.value = ConnectionState.DISCONNECTED
                        }
                        else -> {}
                    }
                }

                override fun onRemoteAudioTrack(track: AudioTrack) {
                    // Remote audio track is automatically played by WebRTC
                    Log.d(TAG, "🔊 Remote audio track ready for playback")
                }
            }
        )

        // Initialize WebSocket service
        val serverUrl = environment.getWebSocketUrl(userId)
        webSocketService = WebRTCWebSocketService(
            serverUrl,
            object : WebRTCWebSocketService.WebSocketEventListener {
                override fun onConnected() {
                    Log.d(TAG, "✅ WebSocket connected - Ready to setup WebRTC")
                }

                override fun onMessage(message: JSONObject) {
                    handleWebSocketMessage(message)
                }

                override fun onDisconnected() {
                    _connectionState.value = ConnectionState.DISCONNECTED
                }

                override fun onError(error: String) {
                    _connectionState.value = ConnectionState.ERROR
                }
            }
        )

        peerConnectionManager.initialize()

        Log.d(TAG, "🔧 ========================================")
    }

    /**
     * Connect to WebRTC server
     */
    fun connect() {
        Log.d(TAG, "🔌 Connecting to WebRTC server...")
        _connectionState.value = ConnectionState.CONNECTING

        // Configure audio session FIRST
        audioManager.configureAudioSession()

        // Connect WebSocket
        webSocketService.connect()
    }

    /**
     * Disconnect from WebRTC server
     */
    fun disconnect() {
        Log.d(TAG, "🔌 Disconnecting...")

        webSocketService.disconnect()
        peerConnectionManager.close()
        audioManager.releaseAudioSession()

        _connectionState.value = ConnectionState.DISCONNECTED
    }

    /**
     * Handle WebSocket messages
     */
    private fun handleWebSocketMessage(message: JSONObject) {
        val type = message.optString("type", "unknown")

        when (type) {
            "ready" -> {
                Log.d(TAG, "✅ Server ready - Setting up WebRTC peer connection")
                setupPeerConnectionAndCreateOffer()
            }

            "answer" -> {
                val answerObj = message.getJSONObject("answer")
                val sdp = answerObj.getString("sdp")
                val answer = SessionDescription(SessionDescription.Type.ANSWER, sdp)
                peerConnectionManager.setRemoteAnswer(answer)
            }

            "ice_candidate" -> {
                val candidateObj = message.getJSONObject("candidate")
                val sdp = candidateObj.getString("candidate")
                val sdpMLineIndex = candidateObj.getInt("sdpMLineIndex")
                val sdpMid = candidateObj.getString("sdpMid")

                val candidate = IceCandidate(sdpMid, sdpMLineIndex, sdp)
                peerConnectionManager.addIceCandidate(candidate)
            }

            "transcript" -> {
                // User spoke
                val text = message.getString("text")
                _receivedText.value = "You: $text"
                lastSpeechTime = System.currentTimeMillis()
                Log.d(TAG, "🎤 You said: \"$text\"")
            }

            "agent_response" -> {
                // AI responded
                val response = message.getString("response")
                val agent = message.optString("agent", "unknown")
                _receivedText.value = "Luma: $response"
                _currentAgent.value = agent

                // Calculate latency
                if (lastSpeechTime > 0) {
                    val latencyMs = (System.currentTimeMillis() - lastSpeechTime).toInt()
                    _latency.value = latencyMs
                    Log.d(TAG, "⏱️ Response latency: ${latencyMs}ms")
                }

                Log.d(TAG, "🤖 AI Response: \"${response.take(50)}...\"")
                Log.d(TAG, "   📍 Agent: $agent")
            }

            "contact_show" -> {
                val contacts = message.optJSONArray("contacts")
                onContactShow?.invoke(emptyList()) // Parse contacts as needed
                Log.d(TAG, "📋 UI Control: contact_show")
            }

            "contact_hide" -> {
                onContactHide?.invoke()
                Log.d(TAG, "📋 UI Control: contact_hide")
            }

            "contact_list" -> {
                Log.d(TAG, "📋 UI Control: contact_list")
            }

            "calendar_show" -> {
                val events = message.optJSONArray("events")
                onCalendarShow?.invoke(emptyList()) // Parse events as needed
                Log.d(TAG, "📋 UI Control: calendar_show")
            }

            "calendar_hide" -> {
                onCalendarHide?.invoke()
                Log.d(TAG, "📋 UI Control: calendar_hide")
            }

            "error" -> {
                val error = message.optString("error", "Unknown error")
                Log.e(TAG, "❌ Server error: $error")
            }

            else -> {
                Log.w(TAG, "⚠️ Unknown message type: $type")
            }
        }
    }

    /**
     * Setup peer connection and create offer
     */
    private fun setupPeerConnectionAndCreateOffer() {
        peerConnectionManager.setupPeerConnection()

        peerConnectionManager.createOffer { offer ->
            sendOffer(offer)
        }
    }

    /**
     * Send SDP offer to server
     */
    private fun sendOffer(offer: SessionDescription) {
        val message = JSONObject().apply {
            put("type", "offer")
            put("offer", JSONObject().apply {
                put("type", offer.type.canonicalForm())
                put("sdp", offer.description)
            })
        }

        webSocketService.send(message)
        Log.d(TAG, "📤 TX: SDP offer sent to server")
    }

    /**
     * Send ICE candidate to server
     */
    private fun sendIceCandidate(candidate: IceCandidate) {
        val message = JSONObject().apply {
            put("type", "ice_candidate")
            put("candidate", JSONObject().apply {
                put("candidate", candidate.sdp)
                put("sdpMLineIndex", candidate.sdpMLineIndex)
                put("sdpMid", candidate.sdpMid)
            })
        }

        webSocketService.send(message)
        Log.d(TAG, "📤 TX: ICE candidate sent to server")
    }

    /**
     * Mute/unmute microphone
     */
    fun setMicrophoneMuted(muted: Boolean) {
        peerConnectionManager.setMicrophoneMuted(muted)

        // Send control message to server
        val message = JSONObject().apply {
            put("type", "control")
            put("action", "mute")
            put("value", muted)
        }
        webSocketService.send(message)
    }

    /**
     * Enable speaker
     */
    fun enableSpeaker() {
        audioManager.enableSpeaker()
    }

    /**
     * Disable speaker
     */
    fun disableSpeaker() {
        audioManager.disableSpeaker()
    }

    /**
     * Cleanup
     */
    fun dispose() {
        disconnect()
        peerConnectionManager.dispose()
    }
}

