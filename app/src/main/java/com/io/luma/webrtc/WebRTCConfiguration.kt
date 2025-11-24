package com.io.luma.webrtc

import org.webrtc.PeerConnection

/**
 * WebRTC Configuration
 * Centralized configuration for WebRTC connection settings
 * Matches the HTML client and iOS implementation
 */
object WebRTCConfiguration {
    
    /**
     * ICE Servers for NAT traversal
     * Using Google's public STUN servers (same as HTML client)
     */
    val iceServers = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
        PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer()
    )
    
    /**
     * Audio Constraints
     * Matches HTML client settings for echo cancellation, noise suppression, and auto gain control
     */
    object AudioConstraints {
        const val ECHO_CANCELLATION = "googEchoCancellation"
        const val NOISE_SUPPRESSION = "googNoiseSuppression"
        const val AUTO_GAIN_CONTROL = "googAutoGainControl"
        const val HIGH_PASS_FILTER = "googHighpassFilter"
        
        // Audio constraints map for local audio track
        val audioConstraints = mapOf(
            ECHO_CANCELLATION to "true",
            NOISE_SUPPRESSION to "true",
            AUTO_GAIN_CONTROL to "true",
            HIGH_PASS_FILTER to "true"
        )
    }
    
    /**
     * Audio Session Configuration
     */
    object AudioSessionConfig {
        const val SAMPLE_RATE = 16000  // 16kHz (matches HTML client)
        const val CHANNEL_COUNT = 1     // Mono audio
        const val AUDIO_VOLUME = 1.0    // Maximum volume
    }
    
    /**
     * Server Environment Configuration
     */
    sealed class ServerEnvironment {
        data class Local(val host: String = "localhost", val port: Int = 8000) : ServerEnvironment()
        object Production : ServerEnvironment()
        
        fun getWebSocketUrl(userId: String): String {
            return when (this) {
                is Local -> "ws://$host:$port/ws/webrtc/$userId"
                is Production -> "wss://api-mvp.lumalife.de/ws/webrtc/$userId"
            }
        }
    }
    
    /**
     * Connection Timeouts
     */
    object Timeouts {
        const val WEB_SOCKET_CONNECT_TIMEOUT = 10_000L  // 10 seconds
        const val WEB_SOCKET_READ_TIMEOUT = 30_000L     // 30 seconds
        const val ICE_GATHERING_TIMEOUT = 5_000L        // 5 seconds
    }
    
    /**
     * SDP Semantics
     */
    const val SDP_SEMANTICS = "unified-plan"
    
    /**
     * Stream IDs
     */
    const val LOCAL_STREAM_ID = "stream0"
    const val LOCAL_AUDIO_TRACK_ID = "audio0"
}

