package com.io.luma.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.io.luma.room.dao.ActivityOfflineDao

@Database(entities = [ActivityOffline::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityOfflineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "activity_database"
                ) // optional: resets db if schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}