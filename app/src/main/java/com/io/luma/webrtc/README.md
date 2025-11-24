# WebRTC Implementation for Android

## 🎯 Overview

This is a **pure WebRTC implementation** for Android that matches the working HTML client and iOS implementation. It provides real-time voice communication with the Luma AI server using WebRTC technology.

### Key Features
- ✅ **Pure WebRTC** - No hacks, no shortcuts, follows WebRTC best practices
- ✅ **Matches HTML Client** - Same audio constraints and configuration
- ✅ **Matches iOS Implementation** - Same architecture and flow
- ✅ **Production Ready** - Connects to `wss://api-mvp.lumalife.de/ws/webrtc/{user_id}`
- ✅ **Full Audio Support** - Echo cancellation, noise suppression, auto gain control
- ✅ **Proper Audio Session** - Android AudioManager configuration for voice communication
- ✅ **ICE/STUN Support** - NAT traversal using Google's STUN servers
- ✅ **Latency Tracking** - Measures response time from speech to AI response

## 📁 Architecture

```
webrtc/
├── WebRTCConfiguration.kt          # Configuration (ICE servers, audio constraints, URLs)
├── WebRTCAudioManager.kt           # Android audio session management
├── WebRTCWebSocketService.kt       # WebSocket signaling service
├── WebRTCPeerConnectionManager.kt  # RTCPeerConnection management
├── WebRTCService.kt                # Main service coordinating all components
├── WebRTCViewModel.kt              # ViewModel for UI state management
├── WebRTCScreen.kt                 # Sample Compose UI
└── README.md                       # This file
```

## 🚀 Quick Start

### 1. Basic Usage

```kotlin
import com.io.luma.webrtc.*

// In your Composable
@Composable
fun MyVoiceAIScreen() {
    val context = LocalContext.current
    val userId = TokenManager.getInstance().getId().toString()
    
    val viewModel = remember {
        WebRTCViewModel(
            context = context,
            userId = userId,
            environment = WebRTCConfiguration.ServerEnvironment.Production
        )
    }
    
    Column {
        // Connection status
        Text("Status: ${viewModel.connectionStatusText}")
        
        // AI response
        Text(viewModel.receivedText)
        
        // Connect button
        Button(onClick = {
            if (viewModel.isConnected) {
                viewModel.disconnect()
            } else {
                viewModel.connect()
            }
        }) {
            Text(if (viewModel.isConnected) "Disconnect" else "Connect")
        }
        
        // Mute button
        if (viewModel.isConnected) {
            Button(onClick = { viewModel.toggleMute() }) {
                Text(if (viewModel.isMuted) "Unmute" else "Mute")
            }
        }
    }
}
```

### 2. Using the Pre-built Screen

```kotlin
import com.io.luma.webrtc.WebRTCScreen

@Composable
fun MyApp() {
    WebRTCScreen(
        userId = TokenManager.getInstance().getId().toString(),
        environment = WebRTCConfiguration.ServerEnvironment.Production
    )
}
```

## 🔧 Configuration

### Server Environments

```kotlin
// Production (default)
WebRTCConfiguration.ServerEnvironment.Production
// URL: wss://api-mvp.lumalife.de/ws/webrtc/{user_id}

// Local development
WebRTCConfiguration.ServerEnvironment.Local(host = "192.168.1.100", port = 8000)
// URL: ws://192.168.1.100:8000/ws/webrtc/{user_id}
```

### Audio Configuration

The implementation uses the same audio constraints as the HTML client:

```kotlin
// Automatically configured in WebRTCConfiguration
- Echo Cancellation: ON
- Noise Suppression: ON
- Auto Gain Control: ON
- Sample Rate: 16kHz
- Channels: Mono
```

## 📡 WebSocket Message Flow

### 1. Connection Flow
```
Client                          Server
  |                               |
  |--- WebSocket Connect -------->|
  |<------ "ready" --------------|
  |--- "offer" (SDP) ------------>|
  |<------ "answer" (SDP) --------|
  |<---> ICE candidates exchange->|
  |<------ Connected -------------|
```

### 2. Message Types

#### From Server:
- `ready` - Server is ready for WebRTC setup
- `answer` - SDP answer from server
- `ice_candidate` - ICE candidate from server
- `transcript` - User's speech transcription
- `agent_response` - AI's response
- `contact_show/hide` - UI control for contacts
- `calendar_show/hide` - UI control for calendar
- `error` - Error message

#### To Server:
- `offer` - SDP offer from client
- `ice_candidate` - ICE candidate from client
- `control` - Control messages (mute, etc.)

## 🎤 Audio Session Management

The `WebRTCAudioManager` handles Android audio configuration:

```kotlin
// Automatically configured when connecting
- Audio Mode: MODE_IN_COMMUNICATION (enables AEC/NS)
- Speakerphone: Enabled by default
- Audio Focus: AUDIOFOCUS_GAIN for voice communication
- Interruption Handling: Handles phone calls, notifications
```

## 🔊 Features

### Microphone Control
```kotlin
viewModel.toggleMute()  // Mute/unmute microphone
```

### Speaker Control
```kotlin
viewModel.toggleSpeaker()  // Enable/disable speakerphone
```

### Latency Tracking
```kotlin
viewModel.latency  // Response latency in milliseconds
```

### UI Callbacks
```kotlin
viewModel.setOnCalendarShow { events ->
    // Handle calendar events
}

viewModel.setOnContactShow { contacts ->
    // Handle contacts
}
```

## 📱 Permissions

Required permissions (already added to AndroidManifest.xml):
```xml
<uses-permission android:name="android.permission.RECORD_AUDIO"/>
<uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS"/>
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
```

## 🐛 Debugging

All components use detailed logging with tags:
- `WebRTCService` - Main service logs
- `WebRTCPeerConnection` - Peer connection logs
- `WebRTCWebSocket` - WebSocket logs
- `WebRTCAudioManager` - Audio session logs

Use Android Logcat to monitor:
```bash
adb logcat | grep WebRTC
```

## ✅ Testing Checklist

- [ ] Microphone permission granted
- [ ] Internet connection available
- [ ] WebSocket connects successfully
- [ ] ICE candidates exchange
- [ ] Audio track received from server
- [ ] Can hear AI responses
- [ ] Microphone captures voice
- [ ] Mute/unmute works
- [ ] Speaker toggle works
- [ ] Latency is reasonable (<2000ms)

## 🔗 Integration Points

### After Onboarding
```kotlin
// In your navigation
navController.navigate("webrtc_screen")
```

### As a Floating Button
```kotlin
FloatingActionButton(onClick = { showWebRTC = true }) {
    Icon(Icons.Default.Mic, "Voice AI")
}

if (showWebRTC) {
    WebRTCScreen()
}
```

## 📚 References

- HTML Client: See the working HTML implementation
- iOS Implementation: `LumaAPP/LumaApp/LumaApp/Socket/WebRTCManager.swift`
- WebRTC Docs: https://webrtc.org/
- Google WebRTC Android: https://webrtc.googlesource.com/src/

## 🎯 Production URL

```
wss://api-mvp.lumalife.de/ws/webrtc/{user_id}
```

Replace `{user_id}` with the actual user ID from `TokenManager.getInstance().getId()`

