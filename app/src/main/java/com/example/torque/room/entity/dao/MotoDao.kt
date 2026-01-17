package com.example.torque.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.torque.room.entity.MotoEntity

@Dao
interface MotoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moto: MotoEntity): Long

    @Update
    suspend fun update(moto: MotoEntity)

    @Delete
    suspend fun delete(moto: MotoEntity)

    @Query("SELECT * FROM motos ORDER BY createdAt DESC")
    fun getAllFlow(): Flow<List<MotoEntity>>

    @Query("SELECT * FROM motos WHERE idMoto = :id")
    suspend fun getById(id: Long): MotoEntity?
}