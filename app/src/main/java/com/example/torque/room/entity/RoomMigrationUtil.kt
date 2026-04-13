package com.example.torque.room

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.torque.database.TorqueDatabaseHelper
import com.example.torque.room.entity.MotoEntity
import com.example.torque.room.entity.PhotoEntity
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
                val oldMotos = dbHelper.getMotos() // Devuelve List<Moto>
                val repo = AppRepository.getInstance(context)
                val now = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)

                for (old in oldMotos) {
                    // Mapear Moto (no-nullable) a MotoEntity (nullable)
                    val motoEntity = MotoEntity(
                        // idMoto se auto-genera, no lo pasamos
                        marca = old.marca,  // String → String
                        modelo = old.modelo, // String → String
                        anno = old.anno,    // Int → Int
                        matricula = old.matricula, // String → String?
                        colorMoto = old.colorMoto, // String? → String?
                        cilindrada = old.cilindrada, // String? → String?
                        cv = old.cv,        // String? → String?
                        estilo = old.estilo, // String → String?
                        kms = old.kms,      // Int → Int
                        fechaCompra = old.fechaCompra, // String → String?
                        esPrincipal = old.esPrincipal, // Int → Int
                        fotoUri = old.fotoUri, // String? → String?
                        createdAt = now,
                        updatedAt = now
                    )

                    val newId = repo.insertMoto(motoEntity)

                    // Migrar fotos asociadas si existen
                    val photos = try {
                        dbHelper.getPhotosByMotoId(old.idMoto)
                    } catch (e: Exception) {
                        emptyList<String>()
                    }

                    photos.forEach { path ->
                        repo.insertPhoto(
                            PhotoEntity(
                                motoId = newId,
                                path = path,
                                createdAt = now
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}