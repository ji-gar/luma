package com.io.luma.brodcast

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.io.luma.R
import com.io.luma.uiscreen.FullscreenAlarmActivity
import kotlin.random.Random

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Activity Reminder"
        val desc = intent.getStringExtra("description") ?: "You have an upcoming activity"

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "activity_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Activity Notifications",
                NotificationManager.IMPORTANCE_HIGH // must be HIGH
            ).apply {
                description = "Activity alert notifications"
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            manager.createNotificationChannel(channel)
        }

        // ✅ Create a FULLSCREEN INTENT for your alarm-style UI
        val fullScreenIntent = Intent(context, FullscreenAlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("title", title)
            putExtra("desc", desc)
        }

        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
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

        // Optional: You can use Broadcasts for buttons if needed, but for now
        // let’s keep focus on showing the full-screen UI
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(summary)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(fullScreenPendingIntent, true) // 👈 shows even when locked
            .setAutoCancel(false)
            .setOngoing(true)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(gradwabletoBitmap(context))

            )
            .setLargeIcon(gradwabletoBitmap(context))
            .build()

        manager.notify(1001, notification)
    }
}

fun gradwabletoBitmap(context: Context): Bitmap{

    var bitmap= BitmapFactory.decodeResource(context.resources,R.drawable.onbordingluma)
    return bitmap

}


//class NotificationReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent) {
//        val title = intent.getStringExtra("title") ?: "Activity Reminder"
//        val desc = intent.getStringExtra("description") ?: "You have an upcoming activity"
//
//        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        val channelId = "activity_channel"
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Activity Notifications",
//                NotificationManager.IMPORTANCE_HIGH // must be HIGH
//            ).apply {
//                description = "Activity alert notifications"
//                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
//            }
//            manager.createNotificationChannel(channel)
//        }
//
//
//
//
//        val fullScreenIntent = Intent(context, FullscreenAlarmActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            putExtra("title", title)
//            putExtra("desc", desc)
//        }
//
//        val fullScreenPendingIntent = PendingIntent.getActivity(
//            context,
//            0,
//            fullScreenIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//        val answerPendingIntent = PendingIntent.getBroadcast(
//            context,
//            2,
//            fullScreenIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val declinePendingIntent = PendingIntent.getBroadcast(
//            context,
//            1,
//            fullScreenIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//
//
//        val notification = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(android.R.drawable.ic_popup_reminder)
//            .setContentTitle(title)
//            .setContentText(desc)
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setCategory(NotificationCompat.CATEGORY_CALL)
//            .setOngoing(true)
//            .setAutoCancel(false)
//            .setFullScreenIntent(fullScreenPendingIntent, true)
//            .addAction(R.drawable.ic_call_answer, "Answer", answerPendingIntent)
//            .addAction(R.drawable.ic_call_decline, "Decline", declinePendingIntent)
//            .build()
//
//
//        manager.notify(1, notification)
//    }
//}
