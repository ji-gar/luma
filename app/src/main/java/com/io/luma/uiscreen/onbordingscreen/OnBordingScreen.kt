package com.io.luma.uiscreen.onbordingscreen

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.io.luma.R
import com.io.luma.brodcast.NotificationReceiver
import com.io.luma.commonmethod.getCalendarFromDateAndTime
import com.io.luma.commonmethod.scheduleNotification
import com.io.luma.customcompose.CustomButton
import com.io.luma.customcompose.CustomOutlineButton
import com.io.luma.room.ActivityOffline
import com.io.luma.room.AppDatabase
import com.io.luma.ui.theme.manropebold
import com.io.luma.ui.theme.textColor
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnBordingScreen(navController: NavController, showNotification: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition(label = "move")
    var context = LocalContext.current
    var db = remember { mutableStateOf(AppDatabase.getInstance(context)) }


    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -20f, // Move upward
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "offsetY"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.statusBars)
            .background(color = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {

            Image(
                painter = painterResource(R.drawable.onbordingluma),
                modifier = Modifier.offset(y = offsetY.dp),
                contentDescription = ""
            )
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .align(alignment = Alignment.BottomCenter)
        )
        {
            // Top shadow layer


            // Card content
            OutlinedCard(
                modifier = Modifier.fillMaxSize(),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                border = BorderStroke(
                    1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
                )
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    com.io.luma.customcompose.height(10)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 13.sdp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Welcome",
                            style = TextStyle(
                                color = textColor,
                                fontSize = 24.ssp,
                                fontFamily = manropebold
                            ),
                            textAlign = TextAlign.Center
                        )

                        Image(
                            painter = painterResource(R.drawable.cancle),
                            contentDescription = "",
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(30.sdp) // align image to the right

                        )
                    }
                    com.io.luma.customcompose.height(13)
                    HorizontalDivider(
                        color = Color(0xFF4E73FF).copy(alpha = 0.2f),
                        thickness = 1.dp
                    )
                    com.io.luma.customcompose.height(20)

                    Text(
                        "Do you want to\njoin?",
                        style = TextStyle(
                            color = textColor,
                            fontSize = 27.ssp,
                            fontWeight = FontWeight.W700
                        ),
                        modifier = Modifier.fillMaxWidth(),

                        textAlign = TextAlign.Center
                    )
                    com.io.luma.customcompose.height(20)
                    CustomButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 13.sdp),
                        text = "schedule notification"
                    ) {


                        GlobalScope.launch(Dispatchers.IO) {
                            val today = LocalDate.now()
                            val formatter =
                                DateTimeFormatter.ofPattern("yyyy-MM-dd") // choose any pattern

                            val dateFormat = today.format(formatter)
                            val startTime = LocalTime.now().plusSeconds(10)
                            val formatted =
                                startTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                            val activity = ActivityOffline(
                                activityId = UUID.randomUUID().toString(),
                                patientId = "123",
                                title = "Morning Rounties",
                                activityDescription = "Breakfast Time",
                                activityType = "Daily",
                                startTime = formatted,
                                date = dateFormat,
                                isActive = true,
                                addedBy = "Jigar",
                                createdAt = "$dateFormat $formatted",
                                updatedAt = "2025-11-07 20:00:00"
                            )
                            db.value.activityDao().insertActivity(activity)

                            withContext(Dispatchers.Main) {
                                db.value.activityDao().getAllInfo().observeForever { activityList ->
                                    activityList?.let { list ->
                                        list.forEach { activity ->
                                            val date = activity.date
                                            val startTime = activity.startTime

                                            if (!date.isNullOrEmpty() && !startTime.isNullOrEmpty()) {
                                                val calendar =
                                                    getCalendarFromDateAndTime(date, startTime)
                                                calendar?.let {
                                                    if (it.timeInMillis > System.currentTimeMillis()) {
                                                        scheduleNotification(context, activity, it)
                                                        Log.d(
                                                            "Notification",
                                                            "Scheduled for ${it.time}"
                                                        )
                                                    } else {
                                                        Log.d(
                                                            "Notification",
                                                            "Skipped past time ${date} ${startTime}"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // You can navigate if needed after insertion
//                         navController.navigate(NavRoute.OnBordingScreen2)
                    }

                    Spacer(modifier = Modifier.height(20.sdp))

                    CustomOutlineButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 13.sdp),
                        text = "Done"
                    ) {
                        db.value.activityDao().getAllInfo().observeForever { activityList ->
                            activityList?.let { list ->
                                list.forEach { activity ->
                                    val date = activity.date
                                    val startTime = activity.startTime

                                    if (!date.isNullOrEmpty() && !startTime.isNullOrEmpty()) {
                                        val calendar = getCalendarFromDateAndTime(date, startTime)
                                        calendar?.let {
                                            if (it.timeInMillis > System.currentTimeMillis()) {
//                                                scheduleNotification(context, activity, it)
                                                Log.d("Notification", "Scheduled for ${it.time}")
                                            } else {
                                                Log.d(
                                                    "Notification",
                                                    "Skipped past time ${date} ${startTime}"
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

//                         navController.navigate(NavRoute.SignupOptionStep2)
                    }


                }
            }
        }


//        Column(modifier = Modifier.fillMaxSize().background(color = Color.Transparent)) {
//
//            Box(modifier = Modifier.fillMaxWidth().fillMaxHeight().weight(1f).background(color = Color.White),
//                contentAlignment = Alignment.Center) {
//
//                Column(modifier = Modifier.fillMaxSize(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painter = painterResource(R.drawable.lumaperson),
//                        contentDescription = "")
//
//                }
//            }
//
//
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .fillMaxHeight()
//                    .weight(0.5f)
//                    .align(alignment = Alignment.BottomCenter)
//            )
//            {
//                // Top shadow layer
//
//
//                // Card content
//                OutlinedCard(
//                    modifier = Modifier.fillMaxSize(),
//                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 16.dp),
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color.White,
//                    ),
//                    border = BorderStroke(1.dp, Color(0xFF4E73FF).copy(alpha = 0.2f) // 20% opacity
//                    )
//                ) {
//                    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 13.sdp),
//                        horizontalAlignment = Alignment.CenterHorizontally) {
//                        com.io.luma.customcompose.height(10)
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                "Welcome",
//                                style = TextStyle(
//                                    color = textColor,
//                                    fontSize = 24.ssp,
//                                    fontFamily = manropebold
//                                ),
//                                textAlign = TextAlign.Center
//                            )
//
//                            Image(
//                                painter = painterResource(R.drawable.cancle),
//                                contentDescription = "",
//                                modifier = Modifier
//                                    .align(Alignment.CenterEnd).size(30.sdp) // align image to the right
//
//                            )
//                        }
//
//                        com.io.luma.customcompose.height(20)
//
//                        Text("Do you want to\njoin?",
//                            style = TextStyle(
//                                color = textColor,
//                                fontSize = 27.ssp
//                            ),
//                            modifier = Modifier.fillMaxWidth(),
//                            fontFamily = manropebold,
//                            textAlign = TextAlign.Center
//                        )
//                        com.io.luma.customcompose.height(20)
//
//                        CustomButton(modifier = Modifier.fillMaxWidth(),
//                            "Yes") {
//
//
//                            navController.navigate(NavRoute.OnBordingScreen2)
//                        }
//
//                        com.io.luma.customcompose.height(20)
//
//                        CustomOutlineButton (modifier = Modifier.fillMaxWidth(),
//                            "No") {
//
//
//                           // navController.navigate(NavRoute.SignupOptionStep5)
//                        }
//
//
//                    }
//                }
//            }
//
//        }


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

}



