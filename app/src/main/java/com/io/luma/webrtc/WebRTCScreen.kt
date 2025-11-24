package com.io.luma.webrtc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.io.luma.utils.TokenManager

/**
 * WebRTC Screen
 * Sample UI for WebRTC voice communication
 * Matches the HTML client interface
 */
@Composable
fun WebRTCScreen(
    userId: String = TokenManager.getInstance().getId().toString(),
    environment: WebRTCConfiguration.ServerEnvironment = WebRTCConfiguration.ServerEnvironment.Production
) {
    val context = LocalContext.current
    val viewModel = remember {
        WebRTCViewModel(context, userId, environment)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF667EEA),
                        Color(0xFF764BA2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Title
            Text(
                text = "🎤 Luma Voice AI",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(vertical = 20.dp)
            )
            
            // Main content card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Connection Status
                    ConnectionStatusIndicator(
                        status = viewModel.connectionStatusText,
                        isConnected = viewModel.isConnected
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Conversation Display
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "💬 Conversation",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF333333)
                            )
                            
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            Text(
                                text = viewModel.receivedText.ifEmpty { "Start talking..." },
                                fontSize = 16.sp,
                                color = Color(0xFF666666),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
                    // Stats
                    if (viewModel.isConnected) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem("Latency", "${viewModel.latency}ms")
                            viewModel.currentAgent?.let {
                                StatItem("Agent", it)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                    
                    // Control Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {
                                if (viewModel.isConnected) {
                                    viewModel.disconnect()
                                } else {
                                    viewModel.connect()
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (viewModel.isConnected) Color(0xFFF56565) else Color(0xFF667EEA)
                            )
                        ) {
                            Text(if (viewModel.isConnected) "Disconnect" else "Connect")
                        }
                    }

                    if (viewModel.isConnected) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.toggleMute() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (viewModel.isMuted) Color(0xFFED8936) else Color(0xFF48BB78)
                                )
                            ) {
                                Text(if (viewModel.isMuted) "🔇 Unmute" else "🎤 Mute")
                            }

                            Button(
                                onClick = { viewModel.toggleSpeaker() },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4299E1)
                                )
                            ) {
                                Text(if (viewModel.isSpeakerEnabled) "🔊 Speaker" else "🔇 Speaker")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ConnectionStatusIndicator(status: String, isConnected: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = when {
                    isConnected -> Color(0xFFE6FFE6)
                    status == "Connecting..." -> Color(0xFFFFFFC)
                    else -> Color(0xFFFEE)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(15.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = when {
                        isConnected -> Color(0xFF3C3)
                        status == "Connecting..." -> Color(0xFF993)
                        else -> Color(0xFFC33)
                    },
                    shape = CircleShape
                )
        )

        Spacer(modifier = Modifier.width(10.dp))

        Text(
            text = status,
            fontWeight = FontWeight.Bold,
            color = when {
                isConnected -> Color(0xFF3C3)
                status == "Connecting..." -> Color(0xFF993)
                else -> Color(0xFFC33)
            }
        )
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF999999)
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
    }
}


