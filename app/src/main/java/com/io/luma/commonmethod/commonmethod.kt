package com.io.luma.commonmethod

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.io.luma.brodcast.NotificationReceiver
import com.io.luma.room.ActivityOffline
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun getCalendarFromDateAndTime(dateString: String, timeString: String): Calendar? {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        val dateTime = sdf.parse("$dateString $timeString")
        Calendar.getInstance().apply { time = dateTime!! }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}


fun scheduleNotification(context: Context, activity: ActivityOffline, calendar: Calendar) {
    val intent = Intent(context, NotificationReceiver::class.java).apply {
        putExtra("title", activity.title)
        putExtra("description", activity.activityDescription ?: "")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        activity.activityId.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}
