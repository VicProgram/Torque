package com.example.torque.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.torque.room.dao.MotoDao
import com.example.torque.room.entity.MotoEntity
import com.example.torque.room.dao.MaintenanceDao
import com.example.torque.room.dao.PhotoDao
import com.example.torque.room.entity.MaintenanceEntity
import com.example.torque.room.entity.PhotoEntity

@Database(
    entities = [MotoEntity::class, MaintenanceEntity::class, PhotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class `AppDatabase.kt` : RoomDatabase() {
    abstract fun motoDao(): MotoDao
    abstract fun maintenanceDao(): MaintenanceDao
    abstract fun photoDao(): PhotoDao

    companion object {
        @Volatile private var INSTANCE: `AppDatabase.kt`? = null

        fun getInstance(context: Context): `AppDatabase.kt` =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    `AppDatabase.kt`::class.java,
                    "torque_room.db"
                ).build().also { INSTANCE = it }
            }
    }
}