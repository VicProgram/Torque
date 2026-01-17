package com.example.torque.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.torque.room.entity.MaintenanceEntity

@Dao
interface MaintenanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(maintenance: MaintenanceEntity): Long

    @Query("SELECT * FROM mantenimientos WHERE motoId = :motoId ORDER BY fecha DESC")
    fun getByMotoFlow(motoId: Long): Flow<List<MaintenanceEntity>>

    @Delete
    suspend fun delete(maintenance: MaintenanceEntity)
}