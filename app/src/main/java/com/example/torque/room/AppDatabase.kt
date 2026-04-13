package com.example.torque.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.torque.room.entity.dao.MotoDao
import com.example.torque.room.entity.dao.MantenimientoDao
import com.example.torque.room.entity.dao.FotoDao
import com.example.torque.room.entity.MotoEntity
import com.example.torque.room.entity.MantenimientoEntity
import com.example.torque.room.entity.FotoEntity

@Database(
    entities = [MotoEntity::class, MantenimientoEntity::class, FotoEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun motoDao(): MotoDao
    abstract fun mantenimientoDao(): MantenimientoDao
    abstract fun fotoDao(): FotoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "torque_room.db"
                ).build().also { INSTANCE = it }
            }
    }
}