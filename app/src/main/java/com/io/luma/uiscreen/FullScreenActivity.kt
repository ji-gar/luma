package com.io.luma.uiscreen

import android.app.Activity
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.io.luma.R
import com.io.luma.navroute.NavRoute
import com.io.luma.ui.theme.goldenYellow
import com.io.luma.ui.theme.skyblue

class FullscreenAlarmActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setShowWhenLocked(true)
        setTurnScreenOn(true)

        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )

        val title = intent.getStringExtra("title") ?: "Reminder"
        val desc = intent.getStringExtra("desc") ?: ""

        setContent {
            Surface(modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            goldenYellow,
                            Color.White,
                            Color.White,
                            Color.White,
                            skyblue
                        )
                    )
                )) {
                DailyRoutineReminderUI()

            }
        }
    }
}

@Composable
fun DailyRoutineReminderUI() {

    val context = LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    val activity = LocalContext.current as? Activity

    // Auto stop sound when user leaves screen
    // Auto start sound when screen opens
    LaunchedEffect(Unit) {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()

            mediaPlayer = MediaPlayer.create(context, R.raw.demo3).apply {
                isLooping = true
                start()
            }

        } catch (e: Exception) {
            Log.e("SOUND_ERROR", "Failed: ${e.message}")
        }
    }


// Auto stop when leaving screen
    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        }
    }


    val bgColor = Color(0xFF0F0F0F)
    val yellow = Color(0xFFFFD32A)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {

        Column(
          verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            // ---------- HEADER ----------
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                // Yellow icon circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(yellow, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.lumalifewide),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "DAILY ROUTINE REMINDER",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Let’s keep moving!",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ---------- MIDDLE CONTENT ----------
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                // ---- Text Left Side ----
                Column(

                ) {

                    RoutineTextSection(
                        "Hydrate yourself",
                        "Drink at least\n200–300 ml of water"
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    RoutineTextSection(
                        "5-minute breathing exercise",
                        "Follow a simple\ninhale–exhale pattern"
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    RoutineTextSection(
                        "Mind refresh",
                        "Quick positive affirmation"
                    )
                }


                // ---- Avatar Right Side ----
                Image(
                    painter = painterResource(R.drawable.onbordingluma),
                    contentDescription = null,
                    modifier = Modifier
                        .height(340.dp),


                )
            }



            // ---------- BUTTON ----------
            Button(
                onClick = {

                    //navController.navigate(NavRoute.WeekalyRouting)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = yellow)
            ) {
                Text(
                    text = "Start Routine",
                    color = Color.Black,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        activity?.finish()
                        // close app screen
                    },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .height(40.dp)
                        .width(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                ) {
                    Text(
                        text = "Dismiss",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun RoutineTextSection(title: String, subtitle: String) {
    Column {
        Text(
            text = title,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = subtitle,
            color = Color(0xFFD0D0D0),
            fontSize = 15.sp,
            lineHeight = 20.sp
        )
    }
}


