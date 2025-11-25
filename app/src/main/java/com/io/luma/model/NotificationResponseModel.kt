package com.io.luma.model

data class NotificationResponseModel(
    val success: Boolean,
    val data: NotificationData,
    val message: String
)

data class NotificationData(
    val notifications: List<NotificationItem>,
    val pagination: Pagination
)

data class NotificationItem(
    val id: Int,
    val title: String,
    val message: String,
    val type: String,
    val isRead: Boolean,
    val createdAt: String, // ISO 8601 format: "2024-11-25T18:30:00Z"
    val patientName: String?,
    val patientId: Int?
)

data class Pagination(
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val itemsPerPage: Int
)