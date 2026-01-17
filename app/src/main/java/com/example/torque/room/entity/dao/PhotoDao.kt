package com.example.torque.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.torque.room.entity.PhotoEntity

@Dao
interface PhotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PhotoEntity): Long

    @Query("SELECT * FROM moto_photos WHERE motoId = :motoId ORDER BY createdAt DESC")
    fun getByMotoFlow(motoId: Long): Flow<List<PhotoEntity>>

    @Query("SELECT * FROM moto_photos WHERE motoId = :motoId")
    suspend fun getByMotoId(motoId: Long): List<PhotoEntity>

    @Delete
    suspend fun delete(photo: PhotoEntity)
}