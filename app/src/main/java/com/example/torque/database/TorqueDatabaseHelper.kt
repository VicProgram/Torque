package com.example.torque.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.torque.garage.Moto
import com.example.torque.maintenance.MaintenanceDetail
import java.io.File
import java.io.FileOutputStream

class TorqueDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "torque.db"
        private const val DATABASE_VERSION = 2
    }

    private val dbPath = context.applicationContext.getDatabasePath(DATABASE_NAME).absolutePath

    init {
        openDatabase()
    }

    private fun copyDatabase() {
        val dbFile = File(dbPath)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            context.assets.open(DATABASE_NAME).use { inputStream ->
                FileOutputStream(dbFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                    outputStream.flush()
                }
            }
        }
    }

    private fun openDatabase() {
        if (!File(dbPath).exists()) {
            copyDatabase()
        }
    }

    override fun onCreate(db: SQLiteDatabase) {}

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    // ==================== MOTO OPERATIONS ====================

    fun getMotos(): List<Moto> {
        val list = mutableListOf<Moto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM MiGaraje", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    list.add(
                        Moto(
                            idMoto = it.getInt(it.getColumnIndexOrThrow("idMoto")),
                            marca = it.getString(it.getColumnIndexOrThrow("marca")),
                            modelo = it.getString(it.getColumnIndexOrThrow("modelo")),
                            anno = it.getInt(it.getColumnIndexOrThrow("anno")),
                            matricula = it.getString(it.getColumnIndexOrThrow("matricula")),
                            colorMoto = it.getString(it.getColumnIndexOrThrow("colorMoto")),
                            cilindrada = it.getString(it.getColumnIndexOrThrow("cilindrada")),
                            cv = it.getString(it.getColumnIndexOrThrow("cv")),
                            estilo = it.getString(it.getColumnIndexOrThrow("estilo")),
                            kms = it.getInt(it.getColumnIndexOrThrow("kms")),
                            fechaCompra = it.getString(it.getColumnIndexOrThrow("fechaCompra")),
                            esPrincipal = it.getInt(it.getColumnIndexOrThrow("esPrincipal")),
                            fotoMoto = ""
                        )
                    )
                } while (it.moveToNext())
            }
        }
        return list
    }

    fun getMotoById(idMoto: Int): Moto? {
        val db = readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM MiGaraje WHERE idMoto = ?", arrayOf(idMoto.toString()))
        return cursor.use {
            if (it.moveToFirst()) {
                Moto(
                    idMoto = it.getInt(it.getColumnIndexOrThrow("idMoto")),
                    marca = it.getString(it.getColumnIndexOrThrow("marca")),
                    modelo = it.getString(it.getColumnIndexOrThrow("modelo")),
                    anno = it.getInt(it.getColumnIndexOrThrow("anno")),
                    matricula = it.getString(it.getColumnIndexOrThrow("matricula")),
                    colorMoto = it.getString(it.getColumnIndexOrThrow("colorMoto")),
                    cilindrada = it.getString(it.getColumnIndexOrThrow("cilindrada")),
                    cv = it.getString(it.getColumnIndexOrThrow("cv")),
                    estilo = it.getString(it.getColumnIndexOrThrow("estilo")),
                    kms = it.getInt(it.getColumnIndexOrThrow("kms")),
                    fechaCompra = it.getString(it.getColumnIndexOrThrow("fechaCompra")),
                    esPrincipal = it.getInt(it.getColumnIndexOrThrow("esPrincipal")),
                    fotoMoto = ""
                )
            } else null
        }
    }

    fun addMoto(moto: Moto): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("marca", moto.marca)
            put("modelo", moto.modelo)
            put("anno", moto.anno)
            put("matricula", moto.matricula)
            put("colorMoto", moto.colorMoto)
            put("cilindrada", moto.cilindrada)
            put("cv", moto.cv)
            put("estilo", moto.estilo)
            put("kms", moto.kms)
            put("fechaCompra", moto.fechaCompra)
            put("esPrincipal", moto.esPrincipal)
        }
        return db.insert("MiGaraje", null, values) != -1L
    }

    fun deleteMoto(idMoto: Int): Boolean {
        val db = writableDatabase
        return db.delete("MiGaraje", "idMoto = ?", arrayOf(idMoto.toString())) > 0
    }

    fun setPrincipal(idMoto: Int): Boolean {
        val db = writableDatabase
        db.beginTransaction()
        return try {
            // Remove principal from all motos
            db.update("MiGaraje", ContentValues().apply { put("esPrincipal", 0) }, null, null)
            // Set new principal
            val result = db.update(
                "MiGaraje",
                ContentValues().apply { put("esPrincipal", 1) },
                "idMoto = ?",
                arrayOf(idMoto.toString())
            )
            db.setTransactionSuccessful()
            result > 0
        } finally {
            db.endTransaction()
        }
    }

    // ==================== PHOTO OPERATIONS ====================

    fun getPhotosByMotoId(idMoto: Int): List<String> {
        val photos = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.query(
            "FotosMoto",
            arrayOf("rutaFoto"),
            "idMoto = ?",
            arrayOf(idMoto.toString()),
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    photos.add(it.getString(it.getColumnIndexOrThrow("rutaFoto")))
                } while (it.moveToNext())
            }
        }
        return photos
    }

    fun insertPhoto(idMoto: Int, path: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("idMoto", idMoto)
            put("rutaFoto", path)
        }
        return db.insert("FotosMoto", null, values) != -1L
    }

    fun deletePhoto(idMoto: Int, path: String): Boolean {
        val db = writableDatabase
        return db.delete(
            "FotosMoto",
            "idMoto = ? AND rutaFoto = ?",
            arrayOf(idMoto.toString(), path)
        ) > 0
    }

    // ==================== MAINTENANCE OPERATIONS ====================

    fun getMaintenances(): List<MaintenanceDetail> {
        val list = mutableListOf<MaintenanceDetail>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Mantenimientos", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    list.add(
                        MaintenanceDetail(
                            maintenanceId = it.getInt(it.getColumnIndexOrThrow("mantenimientoId")),
                            maintenanceName = it.getString(it.getColumnIndexOrThrow("nombreMantenimiento")),
                            date = it.getString(it.getColumnIndexOrThrow("date")),
                            kilometers = it.getInt(it.getColumnIndexOrThrow("kilometers")),
                            brand = it.getString(it.getColumnIndexOrThrow("marca")),
                            model = it.getString(it.getColumnIndexOrThrow("modelo")),
                            year = it.getInt(it.getColumnIndexOrThrow("anno")),
                            price = it.getString(it.getColumnIndexOrThrow("precio")),
                            time = it.getString(it.getColumnIndexOrThrow("tiempo")),
                            notes = it.getString(it.getColumnIndexOrThrow("notas")),
                            tasks = it.getString(it.getColumnIndexOrThrow("tareas"))
                        )
                    )
                } while (it.moveToNext())
            }
        }
        return list
    }

    fun insertMaintenance(maintenance: MaintenanceDetail): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombreMantenimiento", maintenance.maintenanceName)
            put("date", maintenance.date)
            put("kilometers", maintenance.kilometers)
            put("marca", maintenance.brand)
            put("modelo", maintenance.model)
            put("anno", maintenance.year)
            put("precio", maintenance.price)
            put("tiempo", maintenance.time)
            put("notas", maintenance.notes)
            put("tareas", maintenance.tasks)
        }
        return db.insert("Mantenimientos", null, values) != -1L
    }
}