package com.io.luma.uiscreen.notification
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.io.luma.model.NotificationItem
import com.io.luma.model.NotificationResponseModel
import com.io.luma.navroute.NavRoute
import com.io.luma.network.Resource
import com.io.luma.viewmodel.NotificationViewModel

data class Notification(
    val id: Int,
    val date: String,
    val time: String,
    val title: String,
    val detail: String
)

fun generateSequentialNotifications(): List<Notification> {
    val calendar = java.util.Calendar.getInstance()
    val dateFormat = java.text.SimpleDateFormat("dd. MMMM", java.util.Locale.GERMAN)

    val notificationData = listOf(
        Triple("18:30 PM", "Monika hatte heute Schwierigkeiten, ihren Weg zu...",
            "Monika received a letter from her bank and would be happy if you could help her fill out the form and take it to the bank next time. She also tried to call you but wasn't able to reach you."),
        Triple("12:30 PM", "Monika hatte heute Schwierigkeiten, ihren Weg zu...",
            "Monika received a letter from her bank and would be happy if you could help her fill out the form."),
        Triple("09:50 AM", "Monika hatte heute Schwierigkeiten, ihren Weg zu...",
            "Monika needs assistance with her medication schedule."),
        Triple("18:30 PM", "Monika hatte heute Schwierigkeiten, ihren Weg zu fi...",
            "Monika forgot where she parked her car and needs help finding it."),
        Triple("12:10 PM", "Monika hatte heute Schwierigkeiten, ihren Weg zu fi...",
            "Monika wants to go shopping but isn't sure about the route."),
        Triple("10:00 AM", "Monika hatte heute Schwierigkeiten, ihren Weg zu...",
            "Monika has a doctor's appointment and needs directions.")
    )

    return notificationData.mapIndexed { index, (time, title, detail) ->
        calendar.timeInMillis = System.currentTimeMillis()

        val dateLabel = when {
            index < 3 -> {
                "Heute"
            }
            else -> {
                calendar.add(java.util.Calendar.DAY_OF_MONTH, -(index - 2))
                dateFormat.format(calendar.time)
            }
        }

        Notification(
            id = index + 1,
            date = dateLabel,
            time = time,
            title = title,
            detail = detail
        )
    }
}

@Composable
fun NotificationList(navController: NavController,viewModel: NotificationViewModel = viewModel()) {
    var selectedNotification by remember { mutableStateOf<Notification?>(null) }
    val notificationsState by viewModel.notifications.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getNotifications()
    }

    if (selectedNotification == null) {
        NotificationListScreen(
            notificationsState = notificationsState,
            onNotificationClick = { selectedNotification = it },
            onRefresh = { viewModel.refreshNotifications() }
        )
    } else {
        NotificationDetailScreen(
            notification = selectedNotification!!,
            onBack = { selectedNotification = null }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationListScreen(
    notificationsState: Resource<NotificationResponseModel>?,
    onNotificationClick: (Notification) -> Unit,
    onRefresh: () -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFF9E6)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = Color(0xFFFFF9E6)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (notificationsState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Resource.Success -> {
                    val notifications = convertToUINotifications(notificationsState.data.data.notifications)
                    NotificationList(
                        notifications = notifications,
                        onNotificationClick = onNotificationClick
                    )
                }
                is Resource.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = notificationsState.message ?: "An error occurred",
                            color = Color.Red,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = onRefresh) {
                            Text("Retry")
                        }
                    }
                }
                null -> {
                    // Initial state
                }
            }
        }
    }
}

@Composable
fun NotificationList(
    notifications: List<Notification>,
    onNotificationClick: (Notification) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        val groupedNotifications = notifications.groupBy { it.date }

        groupedNotifications.forEach { (date, notifs) ->
            item {
                Text(
                    text = date,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 12.dp)
                )
            }

            items(notifs) { notification ->
                NotificationItemNew(
                    notification = notification,
                    onClick = { onNotificationClick(notification) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NotificationItemNew(notification: Notification, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = notification.title,
                fontSize = 15.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = notification.time,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "View details",
                modifier = Modifier.size(20.dp),
                tint = Color.Gray
            )
        }
    }

    Divider(
        modifier = Modifier.padding(start = 16.dp),
        color = Color.LightGray.copy(alpha = 0.3f),
        thickness = 0.5.dp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailScreen(
    notification: Notification,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE6F7FF)
                )
            )
        },
        bottomBar = {
            BottomNavigationBar()
        },
        containerColor = Color(0xFFE6F7FF)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${notification.date}, ${notification.time}",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Text(
                        text = notification.detail,
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Button(
                onClick = { /* Handle call */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4DD4D1)
                )
            ) {
                Text(
                    text = "Call Monika",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar() {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home", fontSize = 12.sp) },
            selected = false,
            onClick = {}
        )

        NavigationBarItem(
            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color(0xFF4DD4D1), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            selected = false,
            onClick = {}
        )

        NavigationBarItem(
            icon = { Icon(Icons.Default.Send, contentDescription = "Feedback") },
            label = { Text("Feedback", fontSize = 12.sp) },
            selected = false,
            onClick = {}
        )
    }
}

// ==================== Helper Functions ====================
fun convertToUINotifications(apiNotifications: List<NotificationItem>): List<Notification> {
    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.getDefault())
    dateFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")

    val displayDateFormat = java.text.SimpleDateFormat("dd. MMMM", java.util.Locale.GERMAN)
    val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())

    val today = java.util.Calendar.getInstance()
    val yesterday = java.util.Calendar.getInstance().apply {
        add(java.util.Calendar.DAY_OF_MONTH, -1)
    }

    return apiNotifications.map { item ->
        val date = dateFormat.parse(item.createdAt)
        val calendar = java.util.Calendar.getInstance().apply {
            time = date
        }

        val dateLabel = when {
            isSameDay(calendar, today) -> "Heute"
            isSameDay(calendar, yesterday) -> "Gestern"
            else -> displayDateFormat.format(date)
        }

        val timeLabel = timeFormat.format(date)

        Notification(
            id = item.id,
            date = dateLabel,
            time = timeLabel,
            title = item.title,
            detail = item.message
        )
    }
}

fun isSameDay(cal1: java.util.Calendar, cal2: java.util.Calendar): Boolean {
    return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
            cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
}

