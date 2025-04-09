import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.torque.copiarBaseDeDatos

class MiGarajeDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "torque.db", null, 1) {

    init {
        // Copiar la base de datos si no existe
        copiarBaseDeDatos(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // No es necesario crear la base de datos, ya que se copia desde assets
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Si alguna vez necesitas actualizar la base de datos
        db.execSQL("DROP TABLE IF EXISTS MiGaraje")
        onCreate(db)
    }
}
