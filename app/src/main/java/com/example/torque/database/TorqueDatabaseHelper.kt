package com.example.torque.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.torque.garage.Moto
import com.example.torque.maintenance.MaintenanceDetailData

class TorqueDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "torque.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS MiGaraje (
                idMoto INTEGER PRIMARY KEY AUTOINCREMENT,
                marca TEXT NOT NULL,
                modelo TEXT NOT NULL,
                anno INTEGER NOT NULL,
                matricula TEXT,
                colorMoto TEXT,
                cilindrada TEXT,
                cv TEXT,
                estilo TEXT,
                kms INTEGER DEFAULT 0,
                fechaCompra TEXT,
                esPrincipal INTEGER DEFAULT 0,
                fotoUri TEXT
            )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS fotosMoto (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                idMoto INTEGER,
                rutaFoto TEXT
            )
        """)
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS Mantenimientos (
                mantenimientoId INTEGER PRIMARY KEY AUTOINCREMENT,
                idMoto INTEGER NOT NULL,
                nombreMantenimiento TEXT NOT NULL,
                date TEXT NOT NULL,
                kilometers INTEGER,
                intervaloKms INTEGER DEFAULT 0,
                precio TEXT,
                tiempo TEXT,
                notas TEXT,
                tareas TEXT,
                FOREIGN KEY(idMoto) REFERENCES MiGaraje(idMoto) ON DELETE CASCADE
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 3) {
            runCatching { db.execSQL("ALTER TABLE Mantenimientos ADD COLUMN idMoto INTEGER DEFAULT 0") }
            runCatching { db.execSQL("ALTER TABLE Mantenimientos ADD COLUMN intervaloKms INTEGER DEFAULT 0") }
        }
        if (oldVersion < 4) {
            runCatching { db.execSQL("ALTER TABLE MiGaraje ADD COLUMN fotoUri TEXT") }
            runCatching { db.execSQL("ALTER TABLE MiGaraje ALTER COLUMN matricula TEXT") } // SQLite doesn't support ALTER COLUMN easily, but we can try to just let it be.
            // Actually, in SQLite you don't really need to change NOT NULL to NULL if you don't want to recreate the table.
            // But let's at least add fotoUri.
        }
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    // ==================== MOTOS ====================

    fun getMotos(): List<Moto> {
        val list = mutableListOf<Moto>()
        readableDatabase.rawQuery("SELECT * FROM MiGaraje", null).use { c ->
            while (c.moveToNext()) list.add(c.toMoto())
        }
        return list
    }

    fun getMotoById(idMoto: Long): Moto? =
        readableDatabase.rawQuery("SELECT * FROM MiGaraje WHERE idMoto = ?", arrayOf(idMoto.toString())).use { c ->
            if (c.moveToFirst()) c.toMoto() else null
        }

    fun getPrincipalMoto(): Moto? =
        readableDatabase.rawQuery("SELECT * FROM MiGaraje WHERE esPrincipal = 1 LIMIT 1", null).use { c ->
            if (c.moveToFirst()) c.toMoto() else null
        }

    fun addMoto(moto: Moto): Boolean =
        writableDatabase.insert("MiGaraje", null, moto.toContentValues()) != -1L

    fun updateMoto(moto: Moto): Boolean =
        writableDatabase.update("MiGaraje", moto.toContentValues(), "idMoto = ?", arrayOf(moto.idMoto.toString())) > 0

    fun deleteMoto(idMoto: Long): Boolean =
        writableDatabase.delete("MiGaraje", "idMoto = ?", arrayOf(idMoto.toString())) > 0

    fun updateKms(idMoto: Long, kms: Int): Boolean =
        writableDatabase.update("MiGaraje", ContentValues().apply { put("kms", kms) }, "idMoto = ?", arrayOf(idMoto.toString())) > 0

    fun setPrincipal(idMoto: Long): Boolean {
        val db = writableDatabase
        db.beginTransaction()
        return try {
            db.update("MiGaraje", ContentValues().apply { put("esPrincipal", 0) }, null, null)
            val r = db.update("MiGaraje", ContentValues().apply { put("esPrincipal", 1) }, "idMoto = ?", arrayOf(idMoto.toString()))
            db.setTransactionSuccessful()
            r > 0
        } finally {
            db.endTransaction()
        }
    }

    // ==================== FOTOS ====================

    fun getPhotosByMotoId(idMoto: Long): List<String> {
        val photos = mutableListOf<String>()
        readableDatabase.query("fotosMoto", arrayOf("rutaFoto"), "idMoto = ?", arrayOf(idMoto.toString()), null, null, null).use { c ->
            while (c.moveToNext()) photos.add(c.getString(0))
        }
        return photos
    }

    fun insertPhoto(idMoto: Long, path: String): Boolean =
        writableDatabase.insert("fotosMoto", null, ContentValues().apply { put("idMoto", idMoto); put("rutaFoto", path) }) != -1L

    fun deletePhoto(idMoto: Long, path: String): Boolean =
        writableDatabase.delete("fotosMoto", "idMoto = ? AND rutaFoto = ?", arrayOf(idMoto.toString(), path)) > 0

    // ==================== MANTENIMIENTOS ====================

    fun getMantenimientosByMoto(idMoto: Long): List<MaintenanceDetailData> {
        val list = mutableListOf<MaintenanceDetailData>()
        readableDatabase.rawQuery("SELECT * FROM Mantenimientos WHERE idMoto = ?", arrayOf(idMoto.toString())).use { c ->
            while (c.moveToNext()) list.add(c.toMaintenance())
        }
        return list
    }

    fun getAllMantenimientos(): List<MaintenanceDetailData> {
        val list = mutableListOf<MaintenanceDetailData>()
        readableDatabase.rawQuery("SELECT * FROM Mantenimientos", null).use { c ->
            while (c.moveToNext()) list.add(c.toMaintenance())
        }
        return list
    }

    fun insertMaintenance(m: MaintenanceDetailData): Boolean =
        writableDatabase.insert("Mantenimientos", null, m.toContentValues()) != -1L

    fun updateMaintenance(m: MaintenanceDetailData): Boolean =
        writableDatabase.update("Mantenimientos", m.toContentValues(), "mantenimientoId = ?", arrayOf(m.maintenanceId.toString())) > 0

    fun deleteMaintenance(id: Int): Boolean =
        writableDatabase.delete("Mantenimientos", "mantenimientoId = ?", arrayOf(id.toString())) > 0

    // ==================== HELPERS ====================

    private fun android.database.Cursor.toMoto() = Moto(
        idMoto = getLong(getColumnIndexOrThrow("idMoto")),
        marca = getString(getColumnIndexOrThrow("marca")),
        modelo = getString(getColumnIndexOrThrow("modelo")),
        anno = getInt(getColumnIndexOrThrow("anno")),
        matricula = getString(getColumnIndexOrThrow("matricula")) ?: "",
        colorMoto = getString(getColumnIndexOrThrow("colorMoto")),
        cilindrada = getString(getColumnIndexOrThrow("cilindrada")),
        cv = getString(getColumnIndexOrThrow("cv")),
        estilo = getString(getColumnIndexOrThrow("estilo")) ?: "",
        kms = getInt(getColumnIndexOrThrow("kms")),
        fechaCompra = getString(getColumnIndexOrThrow("fechaCompra")) ?: "",
        esPrincipal = getInt(getColumnIndexOrThrow("esPrincipal")),
        fotoUri = try { getString(getColumnIndexOrThrow("fotoUri")) } catch (e: Exception) { null }
    )

    private fun android.database.Cursor.toMaintenance() = MaintenanceDetailData(
        maintenanceId = getInt(getColumnIndexOrThrow("mantenimientoId")),
        motoId = getLong(getColumnIndexOrThrow("idMoto")),
        maintenanceName = getString(getColumnIndexOrThrow("nombreMantenimiento")),
        date = getString(getColumnIndexOrThrow("date")),
        kilometers = getInt(getColumnIndexOrThrow("kilometers")),
        intervaloKms = getInt(getColumnIndexOrThrow("intervaloKms")),
        price = getString(getColumnIndexOrThrow("precio")),
        time = getString(getColumnIndexOrThrow("tiempo")),
        notes = getString(getColumnIndexOrThrow("notas")),
        tasks = getString(getColumnIndexOrThrow("tareas"))
    )

    private fun Moto.toContentValues() = ContentValues().apply {
        put("marca", marca); put("modelo", modelo); put("anno", anno)
        put("matricula", matricula); put("colorMoto", colorMoto)
        put("cilindrada", cilindrada); put("cv", cv); put("estilo", estilo)
        put("kms", kms); put("fechaCompra", fechaCompra); put("esPrincipal", esPrincipal)
        put("fotoUri", fotoUri)
    }

    private fun MaintenanceDetailData.toContentValues() = ContentValues().apply {
        put("idMoto", motoId); put("nombreMantenimiento", maintenanceName)
        put("date", date); put("kilometers", kilometers)
        put("intervaloKms", intervaloKms)
        put("precio", price); put("tiempo", time); put("notas", notes); put("tareas", tasks)
    }
}