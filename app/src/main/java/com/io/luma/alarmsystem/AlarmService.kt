package com.io.luma.alarmsystem

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.io.luma.R
import com.io.luma.brodcast.gradwabletoBitmap
import com.io.luma.uiscreen.FullscreenAlarmActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// AlarmService.kt
class AlarmService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private var isSoundStarted = false
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("AlarmService", "onStartCommand start")
        val title = intent?.getStringExtra("title") ?: "Alarm"
        val summary = intent?.getStringExtra("summary") ?: "It's time!"

        startForeground(
            AlarmConstants.ALARM_NOTIFICATION_ID,
            createAlarmNotification(title, summary)
        )

        // Start sound after tiny stabilization delay
        if (!isSoundStarted) {
            isSoundStarted = true

            // Start alarm sound
            serviceScope.launch {
                delay(150) // 100–200ms is ideal
                val fullNotification = createAlarmNotification(title, summary)
                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                manager.notify(AlarmConstants.ALARM_NOTIFICATION_ID, fullNotification)
                startAlarmSound()
            }

        }
        return START_STICKY
    }

    private fun createAlarmNotification(title: String, summary: String): Notification {
        // Fullscreen activity (alarm screen)

        val fullScreenIntent = Intent(this, FullscreenAlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            this,
            1,
            fullScreenIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val summary = HtmlCompat.fromHtml(
            "<b>Good Morning, Luma ☀️</b><br>" +
                    "Have a fresh start to your day!<br><br>" +
                    "<b>💧 Water Drinking Time</b><br>" +
                    "Take a short break & drink water.<br><br>" +
                    "<b>🍽 Breakfast Time</b><br>" +
                    "Fuel your body with a healthy meal.<br><br>",
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )


        return NotificationCompat.Builder(this, AlarmConstants.ALARM_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(summary)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)  // KEEP THIS
            .setPriority(NotificationCompat.PRIORITY_MAX)    // ensures popup
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setOngoing(true)
            .setSound(null, AudioManager.STREAM_RING)
            .setAutoCancel(false)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(gradwabletoBitmap(this))
            )
            .setLargeIcon(gradwabletoBitmap(this))
            .build()
    }

    private fun startAlarmSound() {
        Log.d("AlarmService", "startAlarmSound called")
        if (mediaPlayer != null) return  //prevent double play

        /*val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(this@AlarmService, alarmUri)
            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            isLooping = true
            prepare()
            start()
        }*/

        val afd = resources.openRawResourceFd(R.raw.alarm_alert)
        mediaPlayer = MediaPlayer().apply {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            afd.close()

            setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            isLooping = true
            prepare()
            start()
        }

    }

    private fun createMinimalNotification(): Notification {
        return NotificationCompat.Builder(this, AlarmConstants.ALARM_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Alarm")
            .setContentText("Starting...")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setOngoing(true)
            .build()
    }

    private fun stopAlarmSound() {
        mediaPlayer?.let {
            try {
                it.stop()
            } catch (_: Exception) {
            }
            it.release()
        }
        mediaPlayer = null
        isSoundStarted = false
    }

    override fun onDestroy() {
        stopAlarmSound()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
