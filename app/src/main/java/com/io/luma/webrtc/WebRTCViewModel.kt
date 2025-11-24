package com.io.luma.webrtc

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * WebRTC ViewModel
 * Manages UI state and user interactions for WebRTC
 * Matches the iOS WebRTCViewModel architecture
 */
class WebRTCViewModel(
    context: Context,
    userId: String,
    environment: WebRTCConfiguration.ServerEnvironment = WebRTCConfiguration.ServerEnvironment.Production
) : ViewModel() {
    
    private val webRTCService = WebRTCService(context, userId, environment)
    
    // UI State
    var connectionState by mutableStateOf(WebRTCService.ConnectionState.DISCONNECTED)
        private set
    
    var receivedText by mutableStateOf("")
        private set
    
    var currentAgent by mutableStateOf<String?>(null)
        private set
    
    var latency by mutableStateOf(0)
        private set
    
    var isMuted by mutableStateOf(false)
        private set
    
    var isSpeakerEnabled by mutableStateOf(true)
        private set
    
    // Computed properties
    val isConnected: Boolean
        get() = connectionState == WebRTCService.ConnectionState.CONNECTED
    
    val connectionStatusText: String
        get() = when (connectionState) {
            WebRTCService.ConnectionState.DISCONNECTED -> "Disconnected"
            WebRTCService.ConnectionState.CONNECTING -> "Connecting..."
            WebRTCService.ConnectionState.CONNECTED -> "Connected"
            WebRTCService.ConnectionState.ERROR -> "Error"
        }
    
    init {
        // Observe connection state
        viewModelScope.launch {
            webRTCService.connectionState.collectLatest { state ->
                connectionState = state
            }
        }
        
        // Observe received text
        viewModelScope.launch {
            webRTCService.receivedText.collectLatest { text ->
                receivedText = text
            }
        }
        
        // Observe current agent
        viewModelScope.launch {
            webRTCService.currentAgent.collectLatest { agent ->
                currentAgent = agent
            }
        }
        
        // Observe latency
        viewModelScope.launch {
            webRTCService.latency.collectLatest { latencyMs ->
                latency = latencyMs
            }
        }
    }
    
    /**
     * Connect to WebRTC server
     */
    fun connect() {
        webRTCService.connect()
    }
    
    /**
     * Disconnect from WebRTC server
     */
    fun disconnect() {
        webRTCService.disconnect()
    }
    
    /**
     * Toggle microphone mute
     */
    fun toggleMute() {
        isMuted = !isMuted
        webRTCService.setMicrophoneMuted(isMuted)
    }
    
    /**
     * Toggle speaker
     */
    fun toggleSpeaker() {
        isSpeakerEnabled = !isSpeakerEnabled
        if (isSpeakerEnabled) {
            webRTCService.enableSpeaker()
        } else {
            webRTCService.disableSpeaker()
        }
    }
    
    /**
     * Set calendar callback
     */
    fun setOnCalendarShow(callback: (List<Any>) -> Unit) {
        webRTCService.onCalendarShow = callback
    }
    
    fun setOnCalendarHide(callback: () -> Unit) {
        webRTCService.onCalendarHide = callback
    }
    
    /**
     * Set contact callback
     */
    fun setOnContactShow(callback: (List<Any>) -> Unit) {
        webRTCService.onContactShow = callback
    }
    
    fun setOnContactHide(callback: () -> Unit) {
        webRTCService.onContactHide = callback
    }
    
    override fun onCleared() {
        super.onCleared()
        webRTCService.dispose()
    }
}

