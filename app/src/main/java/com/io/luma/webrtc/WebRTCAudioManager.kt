package com.io.luma.webrtc

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.util.Log

/**
 * WebRTC Audio Manager
 * Handles Android audio session configuration for WebRTC
 * Ensures proper audio routing and focus management
 */
class WebRTCAudioManager(private val context: Context) {
    
    private val audioManager: AudioManager = 
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    
    private var audioFocusRequest: AudioFocusRequest? = null
    private var previousAudioMode: Int = AudioManager.MODE_NORMAL
    private var previousSpeakerphoneState: Boolean = false
    
    companion object {
        private const val TAG = "WebRTCAudioManager"
    }
    
    /**
     * Configure audio session for WebRTC communication
     * Matches iOS RTCAudioSession configuration
     */
    fun configureAudioSession() {
        Log.d(TAG, "🎯 ========== CONFIGURING AUDIO SESSION ==========")
        
        try {
            // Save previous state
            previousAudioMode = audioManager.mode
            previousSpeakerphoneState = audioManager.isSpeakerphoneOn
            
            // Set audio mode to COMMUNICATION for voice chat
            // This enables acoustic echo cancellation and noise suppression
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            Log.d(TAG, "✅ Audio mode set to MODE_IN_COMMUNICATION")
            
            // Enable speakerphone for hands-free operation
            audioManager.isSpeakerphoneOn = true
            Log.d(TAG, "✅ Speakerphone enabled")
            
            // Request audio focus
            requestAudioFocus()
            
            Log.d(TAG, "🎯 ========================================")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to configure audio session", e)
        }
    }
    
    /**
     * Request audio focus for voice communication
     */
    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { focusChange ->
                    handleAudioFocusChange(focusChange)
                }
                .build()
            
            val result = audioManager.requestAudioFocus(audioFocusRequest!!)
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.d(TAG, "✅ Audio focus granted")
            } else {
                Log.w(TAG, "⚠️ Audio focus request failed")
            }
        } else {
            @Suppress("DEPRECATION")
            val result = audioManager.requestAudioFocus(
                { focusChange -> handleAudioFocusChange(focusChange) },
                AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN
            )
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                Log.d(TAG, "✅ Audio focus granted")
            } else {
                Log.w(TAG, "⚠️ Audio focus request failed")
            }
        }
    }
    
    /**
     * Handle audio focus changes (e.g., phone calls, notifications)
     */
    private fun handleAudioFocusChange(focusChange: Int) {
        when (focusChange) {
            AudioManager.AUDIOFOCUS_GAIN -> {
                Log.d(TAG, "🔊 Audio focus gained")
            }
            AudioManager.AUDIOFOCUS_LOSS -> {
                Log.d(TAG, "🔇 Audio focus lost")
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT -> {
                Log.d(TAG, "🔇 Audio focus lost (transient)")
            }
            AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {
                Log.d(TAG, "🔉 Audio focus lost (can duck)")
            }
        }
    }
    
    /**
     * Enable speakerphone
     */
    fun enableSpeaker() {
        audioManager.isSpeakerphoneOn = true
        Log.d(TAG, "🔊 Speakerphone enabled")
    }
    
    /**
     * Disable speakerphone
     */
    fun disableSpeaker() {
        audioManager.isSpeakerphoneOn = false
        Log.d(TAG, "🔇 Speakerphone disabled")
    }
    
    /**
     * Release audio session and restore previous state
     */
    fun releaseAudioSession() {
        Log.d(TAG, "🔧 Releasing audio session")
        
        try {
            // Abandon audio focus
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioFocusRequest?.let {
                    audioManager.abandonAudioFocusRequest(it)
                }
            } else {
                @Suppress("DEPRECATION")
                audioManager.abandonAudioFocus(null)
            }
            
            // Restore previous state
            audioManager.mode = previousAudioMode
            audioManager.isSpeakerphoneOn = previousSpeakerphoneState
            
            Log.d(TAG, "✅ Audio session released")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Failed to release audio session", e)
        }
    }
}

