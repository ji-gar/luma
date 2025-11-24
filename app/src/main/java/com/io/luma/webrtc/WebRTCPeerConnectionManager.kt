package com.io.luma.webrtc

import android.content.Context
import android.util.Log
import org.webrtc.*

/**
 * WebRTC Peer Connection Manager
 * Handles RTCPeerConnection setup and management
 * Follows pure WebRTC implementation (no hacks, no shortcuts)
 */
class WebRTCPeerConnectionManager(
    private val context: Context,
    private val listener: PeerConnectionEventListener
) {
    
    private var peerConnectionFactory: PeerConnectionFactory? = null
    private var peerConnection: PeerConnection? = null
    private var localAudioTrack: AudioTrack? = null
    private var remoteAudioTrack: AudioTrack? = null
    
    companion object {
        private const val TAG = "WebRTCPeerConnection"
    }
    
    interface PeerConnectionEventListener {
        fun onIceCandidate(candidate: IceCandidate)
        fun onIceConnectionChange(state: PeerConnection.IceConnectionState)
        fun onConnectionChange(state: PeerConnection.PeerConnectionState)
        fun onRemoteAudioTrack(track: AudioTrack)
    }
    
    /**
     * Initialize WebRTC
     * Must be called before any other WebRTC operations
     */
    fun initialize() {
        Log.d(TAG, "🔧 ========== INITIALIZING WEBRTC ==========")
        
        // Initialize PeerConnectionFactory
        val options = PeerConnectionFactory.InitializationOptions.builder(context)
            .setEnableInternalTracer(true)
            .createInitializationOptions()
        
        PeerConnectionFactory.initialize(options)
        
        // Create PeerConnectionFactory
        val encoderFactory = DefaultVideoEncoderFactory(null, false, false)
        val decoderFactory = DefaultVideoDecoderFactory(null)
        
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .setOptions(PeerConnectionFactory.Options())
            .createPeerConnectionFactory()
        
        Log.d(TAG, "✅ WebRTC factory initialized")
        Log.d(TAG, "🔧 ========================================")
    }
    
    /**
     * Setup peer connection
     * Creates RTCPeerConnection with proper configuration
     */
    fun setupPeerConnection() {
        Log.d(TAG, "🔗 Creating peer connection...")
        
        // Configure ICE servers
        val rtcConfig = PeerConnection.RTCConfiguration(WebRTCConfiguration.iceServers).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
            continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
        }
        
        Log.d(TAG, "🔧 ICE Servers configured:")
        WebRTCConfiguration.iceServers.forEach { server ->
            Log.d(TAG, "   - ${server.urls.joinToString(", ")}")
        }
        
        // Create peer connection
        peerConnection = peerConnectionFactory?.createPeerConnection(
            rtcConfig,
            object : PeerConnection.Observer {
                override fun onIceCandidate(candidate: IceCandidate) {
                    val candidateType = candidate.sdp.split(" ").getOrNull(7) ?: "unknown"
                    Log.d(TAG, "🧊 Generated ICE candidate ($candidateType)")
                    listener.onIceCandidate(candidate)
                }
                
                override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
                    Log.d(TAG, "🧊 ICE connection state: $state")
                    listener.onIceConnectionChange(state)
                }
                
                override fun onConnectionChange(state: PeerConnection.PeerConnectionState) {
                    Log.d(TAG, "🔄 WebRTC connection state: $state")
                    listener.onConnectionChange(state)
                }
                
                override fun onAddTrack(receiver: RtpReceiver, streams: Array<out MediaStream>) {
                    val track = receiver.track()
                    if (track is AudioTrack) {
                        Log.d(TAG, "📡 RX: Remote audio track received from server")
                        remoteAudioTrack = track
                        track.setEnabled(true)
                        track.setVolume(WebRTCConfiguration.AudioSessionConfig.AUDIO_VOLUME)
                        listener.onRemoteAudioTrack(track)
                        Log.d(TAG, "🔊 Remote audio track enabled (volume: 100%)")
                    }
                }
                
                override fun onSignalingChange(state: PeerConnection.SignalingState) {
                    Log.d(TAG, "📡 Signaling state: $state")
                }
                
                override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {
                    Log.d(TAG, "🧊 ICE gathering state: $state")
                }
                
                override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>) {}
                override fun onAddStream(stream: MediaStream) {}
                override fun onRemoveStream(stream: MediaStream) {}
                override fun onDataChannel(channel: DataChannel) {}
                override fun onRenegotiationNeeded() {}
                override fun onIceConnectionReceivingChange(receiving: Boolean) {
                    Log.d(TAG, "🧊 ICE connection receiving change: $receiving")
                }
            }
        )
        
        // Setup local audio track
        setupLocalAudioTrack()

        Log.d(TAG, "✅ Peer connection created")
    }

    /**
     * Setup local audio track
     * Creates audio track with proper constraints (matches HTML client)
     */
    private fun setupLocalAudioTrack() {
        Log.d(TAG, "🎤 Setting up local audio track...")

        // Create audio constraints (matches HTML client exactly)
        val audioConstraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair(
                WebRTCConfiguration.AudioConstraints.ECHO_CANCELLATION, "true"))
            mandatory.add(MediaConstraints.KeyValuePair(
                WebRTCConfiguration.AudioConstraints.NOISE_SUPPRESSION, "true"))
            mandatory.add(MediaConstraints.KeyValuePair(
                WebRTCConfiguration.AudioConstraints.AUTO_GAIN_CONTROL, "true"))
        }

        Log.d(TAG, "🎤 Pure WebRTC audio constraints (matches HTML)")
        Log.d(TAG, "   - Echo Cancellation: ON")
        Log.d(TAG, "   - Noise Suppression: ON")
        Log.d(TAG, "   - Auto Gain Control: ON")

        // Create audio source
        val audioSource = peerConnectionFactory?.createAudioSource(audioConstraints)

        // Create audio track
        localAudioTrack = peerConnectionFactory?.createAudioTrack(
            WebRTCConfiguration.LOCAL_AUDIO_TRACK_ID,
            audioSource
        )

        localAudioTrack?.setEnabled(true)

        // Add track to peer connection
        peerConnection?.addTrack(
            localAudioTrack,
            listOf(WebRTCConfiguration.LOCAL_STREAM_ID)
        )

        Log.d(TAG, "✅ Local audio track added (pure WebRTC)")
    }

    /**
     * Create SDP offer
     */
    fun createOffer(callback: (SessionDescription) -> Unit) {
        Log.d(TAG, "🔧 Creating SDP offer...")

        val constraints = MediaConstraints().apply {
            mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"))
        }

        peerConnection?.createOffer(object : SdpObserver {
            override fun onCreateSuccess(sdp: SessionDescription) {
                Log.d(TAG, "✅ SDP offer created")
                peerConnection?.setLocalDescription(object : SdpObserver {
                    override fun onSetSuccess() {
                        Log.d(TAG, "✅ Local description set (offer)")
                        callback(sdp)
                    }
                    override fun onSetFailure(error: String) {
                        Log.e(TAG, "❌ Failed to set local description: $error")
                    }
                    override fun onCreateSuccess(p0: SessionDescription?) {}
                    override fun onCreateFailure(p0: String?) {}
                }, sdp)
            }

            override fun onCreateFailure(error: String) {
                Log.e(TAG, "❌ Failed to create offer: $error")
            }

            override fun onSetSuccess() {}
            override fun onSetFailure(error: String?) {}
        }, constraints)
    }

    /**
     * Set remote SDP answer
     */
    fun setRemoteAnswer(answer: SessionDescription) {
        Log.d(TAG, "📥 RX: SDP answer from server")

        peerConnection?.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {
                Log.d(TAG, "✅ Remote description set - WebRTC negotiation complete")
            }

            override fun onSetFailure(error: String) {
                Log.e(TAG, "❌ Failed to set remote description: $error")
            }

            override fun onCreateSuccess(p0: SessionDescription?) {}
            override fun onCreateFailure(p0: String?) {}
        }, answer)
    }

    /**
     * Add ICE candidate
     */
    fun addIceCandidate(candidate: IceCandidate) {
        val candidateType = candidate.sdp.split(" ").getOrNull(7) ?: "unknown"
        Log.d(TAG, "🧊 RX: ICE candidate ($candidateType)")

        peerConnection?.addIceCandidate(candidate)
        Log.d(TAG, "✅ ICE candidate added")
    }

    /**
     * Mute/unmute local audio
     */
    fun setMicrophoneMuted(muted: Boolean) {
        localAudioTrack?.setEnabled(!muted)
        Log.d(TAG, if (muted) "🔇 Microphone muted" else "🔊 Microphone unmuted")
    }

    /**
     * Close peer connection and cleanup
     */
    fun close() {
        Log.d(TAG, "🔧 Closing peer connection...")

        localAudioTrack?.dispose()
        localAudioTrack = null

        remoteAudioTrack?.dispose()
        remoteAudioTrack = null

        peerConnection?.close()
        peerConnection = null

        Log.d(TAG, "✅ Peer connection closed")
    }

    /**
     * Dispose factory
     */
    fun dispose() {
        close()
        peerConnectionFactory?.dispose()
        peerConnectionFactory = null
        PeerConnectionFactory.stopInternalTracingCapture()
        PeerConnectionFactory.shutdownInternalTracer()
        Log.d(TAG, "✅ WebRTC disposed")
    }
}
