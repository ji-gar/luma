package com.io.luma.alarmsystem

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == AlarmConstants.ACTION_STOP_ALARM) {
            // Stop service → onDestroy() stops sound and removes foreground
            context.stopService(Intent(context, AlarmService::class.java))

            val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.cancel(AlarmConstants.ALARM_NOTIFICATION_ID)
        }
    }
}
