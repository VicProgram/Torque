import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import android.database.Cursor

class GestorDatabase(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    override fun onCreate(db: SQLiteDatabase) {
        val crearTablaMantenimiento = """
            CREATE TABLE mantenimiento (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                moto TEXT,
                fecha TEXT,
                descripcion TEXT
            )
        """.trimIndent()

        val crearTablaRevision = """
            CREATE TABLE revision (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                mantenimientoId INTEGER,
                fecha TEXT,
                descripcion TEXT,
                FOREIGN KEY(mantenimientoId) REFERENCES mantenimiento(id)
            )
        """.trimIndent()

        db.execSQL(crearTablaMantenimiento)
        db.execSQL(crearTablaRevision)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS revision")
        db.execSQL("DROP TABLE IF EXISTS mantenimiento")
        onCreate(db)
    }

    fun insertarMantenimiento(moto: String, fecha: String, descripcion: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("moto", moto)
            put("fecha", fecha)
            put("descripcion", descripcion)
        }
        db.insert("mantenimiento", null, values)
    }

    fun insertarRevision(mantenimientoId: Int, fecha: String, descripcion: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("mantenimientoId", mantenimientoId)
            put("fecha", fecha)
            put("descripcion", descripcion)
        }
        db.insert("revision", null, values)
    }

    fun obtenerMantenimientos(): List<String> {
        val lista = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM mantenimiento", null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val moto = cursor.getString(cursor.getColumnIndexOrThrow("moto"))
            val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            lista.add("ID: $id, Moto: $moto, Fecha: $fecha, Desc: $descripcion")
        }

        cursor.close()
        return lista
    }

    fun obtenerRevisionesDeMantenimiento(mantenimientoId: Int): List<String> {
        val lista = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM revision WHERE mantenimientoId = ?",
            arrayOf(mantenimientoId.toString())
        )

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"))
            val descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"))
            lista.add("ID: $id, Fecha: $fecha, Desc: $descripcion")
        }

        cursor.close()
        return lista
    }

    companion object {
        private const val DATABASE_NAME = "torque.db"
        private const val DATABASE_VERSION = 1
    }
}

//FUNCIONES BBDD

/*
val gestor = GestorDatabase(context)

// Insertar datos
gestor.insertarMantenimiento("Honda CB500", "2025-04-08", "Cambio aceite")
gestor.insertarRevision(1, "2025-04-09", "Revisión de frenos")

// Consultar
val mantenimientos = gestor.obtenerMantenimientos()
val revisiones = gestor.obtenerRevisionesDeMantenimiento(1)

 */


