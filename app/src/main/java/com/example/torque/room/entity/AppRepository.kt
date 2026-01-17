package com.example.torque.room

import android.content.Context
import com.example.torque.room.entity.MotoEntity
import com.example.torque.room.entity.MaintenanceEntity
import com.example.torque.room.entity.PhotoEntity
import kotlinx.coroutines.flow.Flow

class AppRepository private constructor(private val db: AppDatabase) {

    fun getMotosFlow(): Flow<List<MotoEntity>> = db.motoDao().getAllFlow()
    suspend fun insertMoto(m: MotoEntity): Long = db.motoDao().insert(m)
    suspend fun updateMoto(m: MotoEntity) = db.motoDao().update(m)
    suspend fun deleteMoto(m: MotoEntity) = db.motoDao().delete(m)
    suspend fun getMotoById(id: Long) = db.motoDao().getById(id)

    fun getMaintenancesForMoto(motoId: Long) = db.maintenanceDao().getByMotoFlow(motoId)
    suspend fun insertMaintenance(m: MaintenanceEntity) = db.maintenanceDao().insert(m)

    fun getPhotosForMoto(motoId: Long) = db.photoDao().getByMotoFlow(motoId)
    suspend fun insertPhoto(p: PhotoEntity) = db.photoDao().insert(p)

    companion object {
        @Volatile private var instance: AppRepository? = null
        fun getInstance(context: Context): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(AppDatabase.getInstance(context)).also { instance = it }
            }
    }
}