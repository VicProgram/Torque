import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MiGarajeDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "torque.db", null, 1) {

    init {
        copiarBaseDeDatos(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        // No se usa
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS MiGaraje")
        onCreate(db)
    }

    fun obtenerMotos(): List<Moto> {
        val lista = mutableListOf<Moto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM MiGaraje", null)

        if (cursor.moveToFirst()) {
            do {
                val marca = cursor.getString(cursor.getColumnIndexOrThrow("Marca"))
                val modelo = cursor.getString(cursor.getColumnIndexOrThrow("Modelo"))
                val anno = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
                val matricula = cursor.getString(cursor.getColumnIndexOrThrow("Matricula"))

                lista.add(
                    Moto(
                        Marca = marca,
                        Modelo = modelo,
                        Anno = anno,
                        Matricula = matricula,
                        // Puedes rellenar con valores por defecto o modificar según lo que uses
                        idMoto = 0,
                        Cilindrada = 0,
                        Cv = 0,
                        Estilo = "",
                        Kms = 0,
                        Fecha_compra = "",
                        Foto = ""
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }

    fun copiarBaseDeDatos(context: Context) {
        val dbFile = context.getDatabasePath("torque.db")
        // Si no existe la base de datos, la copiamos desde assets
        if (!dbFile.exists()) {
            try {
                val inputStream: InputStream = context.assets.open("torque.db")
                val outputStream: OutputStream = FileOutputStream(dbFile)

                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

