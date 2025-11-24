package com.io.luma.webrtc

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * WebRTC WebSocket Service
 * Handles WebSocket signaling communication with the server
 * Matches the HTML client and iOS implementation
 */
class WebRTCWebSocketService(
    private val serverUrl: String,
    private val listener: WebSocketEventListener
) {
    
    private var webSocket: WebSocket? = null
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ConnectionState> = _connectionState
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(WebRTCConfiguration.Timeouts.WEB_SOCKET_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
        .readTimeout(WebRTCConfiguration.Timeouts.WEB_SOCKET_READ_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()
    
    companion object {
        private const val TAG = "WebRTCWebSocket"
    }
    
    enum class ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED,
        ERROR
    }
    
    interface WebSocketEventListener {
        fun onConnected()
        fun onMessage(message: JSONObject)
        fun onDisconnected()
        fun onError(error: String)
    }
    
    /**
     * Connect to WebSocket server
     */
    fun connect() {
        if (_connectionState.value == ConnectionState.CONNECTED) {
            Log.w(TAG, "⚠️ Already connected")
            return
        }
        
        Log.d(TAG, "🔌 Connecting to: $serverUrl")
        _connectionState.value = ConnectionState.CONNECTING
        
        val request = Request.Builder()
            .url(serverUrl)
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "✅ WebSocket connected")
                _connectionState.value = ConnectionState.CONNECTED
                listener.onConnected()
            }
            
            override fun onMessage(webSocket: WebSocket, text: String) {
                try {
                    val json = JSONObject(text)
                    val type = json.optString("type", "unknown")
                    Log.d(TAG, "📨 RX: $type")
                    listener.onMessage(json)
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Failed to parse message", e)
                }
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "❌ WebSocket error: ${t.message}")
                _connectionState.value = ConnectionState.ERROR
                listener.onError(t.message ?: "Unknown error")
                
                // Attempt reconnection
                scope.launch {
                    delay(3000)
                    if (_connectionState.value == ConnectionState.ERROR) {
                        Log.d(TAG, "🔄 Attempting reconnection...")
                        connect()
                    }
                }
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "🔌 WebSocket closed: $reason")
                _connectionState.value = ConnectionState.DISCONNECTED
                listener.onDisconnected()
            }
        })
    }
    
    /**
     * Send message to server
     */
    fun send(message: JSONObject) {
        val success = webSocket?.send(message.toString()) ?: false
        if (success) {
            val type = message.optString("type", "unknown")
            Log.d(TAG, "📤 TX: $type")
        } else {
            Log.e(TAG, "❌ Failed to send message")
        }
    }
    
    /**
     * Disconnect from WebSocket server
     */
    fun disconnect() {
        Log.d(TAG, "🔌 Disconnecting...")
        webSocket?.close(1000, "Client disconnect")
        webSocket = null
        _connectionState.value = ConnectionState.DISCONNECTED
    }
    
    /**
     * Check if connected
     */
    fun isConnected(): Boolean {
        return _connectionState.value == ConnectionState.CONNECTED
    }
}

