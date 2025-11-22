package com.io.luma.alarmsystem

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build

fun createAlarmChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            AlarmConstants.ALARM_CHANNEL_ID,
            "Alarm",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Alarm notifications"
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(null, null)

            // This tries to play even in DND; only works if user grants policy access
            setBypassDnd(true)
        }

        manager.createNotificationChannel(channel)
    }
}
