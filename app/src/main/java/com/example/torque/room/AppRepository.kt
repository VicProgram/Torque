package com.example.torque.room

import android.content.Context
import androidx.room.withTransaction
import com.example.torque.room.entity.MotoEntity
import com.example.torque.room.entity.MantenimientoEntity
import com.example.torque.room.entity.FotoEntity
import kotlinx.coroutines.flow.Flow

class AppRepository private constructor(private val db: AppDatabase) {

    fun obtenerMotosFlow(): Flow<List<MotoEntity>> = db.motoDao().obtenerTodasFlow()
    suspend fun insertarMoto(moto: MotoEntity): Long = db.motoDao().insertar(moto)
    suspend fun actualizarMoto(moto: MotoEntity) = db.motoDao().actualizar(moto)
    suspend fun eliminarMoto(moto: MotoEntity) = db.motoDao().eliminar(moto)
    suspend fun obtenerMotoPorId(id: Long) = db.motoDao().obtenerPorId(id)
    suspend fun contarMotos(): Int = db.motoDao().contarMotos()

    fun obtenerMotoPrincipalFlow(): Flow<MotoEntity?> = db.motoDao().obtenerPrincipalFlow()

    suspend fun establecerMotoPrincipal(idMoto: Long) {
        db.withTransaction {
            db.motoDao().resetearPrincipales()
            db.motoDao().marcarComoPrincipal(idMoto)
        }
    }

    fun obtenerMantenimientosPorMoto(idMoto: Long) = db.mantenimientoDao().obtenerPorMotoFlow(idMoto)
    suspend fun insertarMantenimiento(mantenimiento: MantenimientoEntity) = db.mantenimientoDao().insertar(mantenimiento)

    suspend fun actualizarKilometros(idMoto: Long, kilometros: Int) = db.motoDao().actualizarKilometros(idMoto, kilometros)
    suspend fun obtenerUltimoMantenimiento(idMoto: Long) = db.mantenimientoDao().obtenerUltimoPorMoto(idMoto)

    fun obtenerFotosPorMoto(idMoto: Long) = db.fotoDao().obtenerPorMotoFlow(idMoto)
    suspend fun insertarFoto(foto: FotoEntity) = db.fotoDao().insertar(foto)

    companion object {
        @Volatile
        private var instance: AppRepository? = null

        fun obtenerInstancia(context: Context): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(
                    AppDatabase.obtenerInstancia(context)
                ).also { instance = it }
            }
    }
}