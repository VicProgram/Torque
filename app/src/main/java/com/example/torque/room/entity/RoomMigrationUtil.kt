package com.example.torque.room

import android.content.Context
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.room.entity.MotoEntity
import com.example.torque.room.entity.PhotoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RoomMigrationUtil {
    private var migrated = false

    fun migrateIfNeeded(context: Context) {
        if (migrated) return
        migrated = true

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val dbHelper = TorqueDatabaseHelper(context)
                val oldMotos =
                    dbHelper.getMotos() // tu helper devuelve lista de Moto (clase antigua)
                val repo = AppRepository.getInstance(context)
                val now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

                // Migrate motos
                for (old in oldMotos) {
                    val motoEntity = MotoEntity(
                        marca = old.marca ?: "",
                        modelo = old.modelo ?: "",
                        anno = old.anno,
                        matricula = old.matricula,
                        colorMoto = old.colorMoto,
                        cilindrada = old.cilindrada,
                        cv = old.cv,
                        estilo = old.estilo,
                        kms = old.kms,
                        fechaCompra = old.fechaCompra,
                        esPrincipal = old.esPrincipal,
                        fotoUri = old.fotoMoto?.takeIf { it.isNotBlank() },
                        createdAt = now,
                        updatedAt = now
                    )
                    val newId = repo.insertMoto(motoEntity)

                    // Migrate photos if helper provides them
                    val photos = try {
                        dbHelper.getPhotosByMotoId(old.idMoto)
                    } catch (e: Exception) {
                        emptyList<String>()
                    }
                    photos.forEach { path ->
                        repo.insertPhoto(PhotoEntity(motoId = newId, path = path, createdAt = now))
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}