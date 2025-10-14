package com.io.luma.model

data class CalendarItem(
    val id: Int,
    val title: String,
    val description: String,
    val type: String,
    val date: String?,
    val start_time: String?,
    val end_time: String?,
    val timezone: String?,
    val all_day: Boolean,
    val important: Boolean,
    val created_at: String?,
    val updated_at: String?
)

data class CalendarResponse(
    val type: String,
    val calendar_items: List<CalendarItem>,
    val action: String
)

