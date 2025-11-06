package com.io.luma.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.io.luma.room.ActivityOffline
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityOfflineDao {

    // Insert a single activity (if already exists, replace)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityOffline)

    // Insert multiple activities at once
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<ActivityOffline>)

    @Query("SELECT * FROM activity_offline")
     fun getAllInfo() : LiveData<List<ActivityOffline>>

    // Update activity status

}
