import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
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

    companion object {
        private const val DATABASE_NAME = "torque.db"
        private const val DATABASE_VERSION = 1
    }
}
