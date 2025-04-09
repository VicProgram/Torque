import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MiGarajeDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "torque.db", null, 1) {

    // Este método ya no se necesita si no vas a crear tablas desde la app.
    override fun onCreate(db: SQLiteDatabase) {
        // No es necesario implementar este método si las tablas ya están creadas.
    }

    // Este método solo se necesita si hay cambios en el esquema, pero si las tablas ya están creadas, no es necesario.
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // No es necesario si no haces actualizaciones de esquema en la base de datos desde la app.
    }
}
