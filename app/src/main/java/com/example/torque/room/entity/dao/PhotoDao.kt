package com.example.torque.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.torque.room.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

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