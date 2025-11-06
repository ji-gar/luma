package com.io.luma.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.Date

@Entity(tableName = "activity_offline")
data class ActivityOffline(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "activity_id")
    val activityId: String,

    @ColumnInfo(name = "patient_id")
    val patientId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "activity_description")
    val activityDescription: String? = null,

    @ColumnInfo(name = "activity_type")
    val activityType: String,

    @ColumnInfo(name = "start_time")
    val startTime: String? = null,

    @ColumnInfo(name = "date")
    val date: String? = null,

    @ColumnInfo(name = "is_active")
    val isActive: Boolean = true,

    @ColumnInfo(name = "added_by")
    val addedBy: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "updated_at")
    val updatedAt: String
)
