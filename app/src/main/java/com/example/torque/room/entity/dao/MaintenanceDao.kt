package com.example.torque.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.torque.room.entity.MaintenanceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MaintenanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(maintenance: MaintenanceEntity): Long

    @Query("SELECT * FROM mantenimientos WHERE motoId = :motoId ORDER BY fecha DESC")
    fun getByMotoFlow(motoId: Long): Flow<List<MaintenanceEntity>>

    @Delete
    suspend fun delete(maintenance: MaintenanceEntity)
}