package com.io.luma.alarmsystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat
import com.io.luma.MainActivity
import com.io.luma.MainApp

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("AlarmReceiver", "onReceive called")

        val title = intent.getStringExtra("title") ?: "Alarm"
        val summary = intent.getStringExtra("summary") ?: "It's time!"

        if (MainApp.appState.isForeground) {
            MainActivity.appContainerState.triggerAlarmDialog(title, summary)
        } else {
            val serviceIntent = Intent(context, AlarmService::class.java).apply {
                putExtra("title", title)
                putExtra("summary", summary)
            }

            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}
