package com.example.torque.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.MediaStore
import com.example.torque.garaje.Moto
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class TorqueDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, "torque.db", null, 2) {

    private val dbPath = context.applicationContext.getDatabasePath("torque.db").absolutePath

    // Método para copiar la base de datos desde assets
    private fun copiarBaseDeDatos() {
        val dbFile = File(dbPath)
        if (!dbFile.exists()) {
            // Crea el directorio si no existe
            dbFile.parentFile?.mkdirs()

            // Copia la base de datos desde los assets
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
        }
    }

    // Método para abrir la base de datos
    fun abrirBaseDeDatos() {
        if (!File(dbPath).exists()) {
            copiarBaseDeDatos() // Si no existe la base de datos, copia la desde assets
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // No crear nada si copiamos la base de datos desde assets
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes poner la lógica para migrar la base de datos si es necesario.
        // Por ejemplo, agregar columnas o modificar la estructura de la base de datos.
    }

    // Obtener todas las motos de la base de datos
    // Método para obtener todas las motos de la base de datos
    fun obtenerMotos(): List<Moto> {
        val lista = mutableListOf<Moto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM MiGaraje", null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val idMoto = it.getInt(it.getColumnIndexOrThrow("idMoto"))
                    val marca =
                        it.getString(it.getColumnIndexOrThrow("marca"))  // Cambiado de "Marca" a "marca"
                    val modelo = it.getString(it.getColumnIndexOrThrow("modelo"))
                    val anno = it.getInt(it.getColumnIndexOrThrow("anno"))
                    val matricula = it.getString(it.getColumnIndexOrThrow("matricula"))
                    val colorMoto = it.getString(it.getColumnIndexOrThrow("colorMoto"))

                    lista.add(
                        Moto(
                            idMoto = idMoto,
                            marca = marca,  // Cambiado de "Marca" a "marca"
                            modelo = modelo,
                            cilindrada = "",
                            anno = anno,
                            cv = 0,
                            estilo = "",
                            matricula = matricula,
                            kms = 0,
                            fechaCompra = "",
                            colorMoto = colorMoto,
                            esPrincipal = 0,
                            fotoMoto = ""
                        )
                    )
                } while (it.moveToNext())
            }
        }

        db.close()
        return lista
    }

    // Obtener una moto específica por su id
    fun obtenerMotoPorId(idMoto: Int): Moto? {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM MiGaraje WHERE idMoto = ?", arrayOf(idMoto.toString()))

        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("idMoto"))
            val marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"))
            val modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo"))
            val anno = cursor.getInt(cursor.getColumnIndexOrThrow("anno"))
            val matricula = cursor.getString(cursor.getColumnIndexOrThrow("matricula"))
            val colorMoto = cursor.getString(cursor.getColumnIndexOrThrow("colorMoto"))
            val cilindrada = cursor.getString(cursor.getColumnIndexOrThrow("cilindrada"))
            val cv = cursor.getInt(cursor.getColumnIndexOrThrow("cv"))
            val estilo = cursor.getString(cursor.getColumnIndexOrThrow("estilo"))
            val kms = cursor.getInt(cursor.getColumnIndexOrThrow("kms"))
            val fechaCompra = cursor.getString(cursor.getColumnIndexOrThrow("fechaCompra"))

            cursor.close()

            Moto(
                idMoto = id,
                marca = marca,
                modelo = modelo,
                anno = anno,
                matricula = matricula,
                cilindrada = cilindrada,
                cv = cv,
                estilo = estilo,
                kms = kms,
                fechaCompra = fechaCompra,
                colorMoto = colorMoto,
                esPrincipal = 0,
                fotoMoto = ""
            )
        } else {
            cursor.close()
            null
        }
    }

    // Método para agregar una moto
    fun agregarMoto(moto: Moto): Boolean {
        val db = this.writableDatabase
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
            put("fotoMoto", moto.fotoMoto)
        }
        val result = db.insert("MiGaraje", null, values)
        return result != -1L
    }

    // Método para eliminar una moto
    fun eliminarMoto(idMoto: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            "MiGaraje", "idMoto = ?", arrayOf(idMoto)
        )
        return result > 0
    }

    // Método para hacer principal una moto
    fun hacerPrincipal(idMoto: String): Boolean {
        val db = writableDatabase
        return try {
            db.beginTransaction()

            // Desmarcar todas las motos
            db.execSQL("UPDATE MiGaraje SET esPrincipal = 0")

            // Marcar esta moto como principal
            db.execSQL("UPDATE MiGaraje SET esPrincipal = 1 WHERE idMoto = ?", arrayOf(idMoto))

            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        } finally {
            db.endTransaction()
        }
    }

    // Método para obtener la moto principal
    fun obtenerMotoPrincipal(): Moto? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM MiGaraje WHERE esPrincipal = 1 LIMIT 1", null)
        return if (cursor.moveToFirst()) {
            val moto = Moto(
                idMoto = cursor.getInt(cursor.getColumnIndexOrThrow("idMoto")),
                marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"))
                    ?: "Marca desconocida", // Valor por defecto si es null
                modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo"))
                    ?: "Modelo desconocido", // Valor por defecto si es null
                anno = cursor.getInt(cursor.getColumnIndexOrThrow("anno")),
                matricula = cursor.getString(cursor.getColumnIndexOrThrow("matricula"))
                    ?: "Matrícula desconocida", // Valor por defecto si es null
                colorMoto = cursor.getString(cursor.getColumnIndexOrThrow("colorMoto"))
                    ?: "Color desconocido", // Valor por defecto si es null
                cilindrada = cursor.getString(cursor.getColumnIndexOrThrow("cilindrada"))
                    ?: "Cilindrada desconocida", // Valor por defecto si es null
                cv = cursor.getInt(cursor.getColumnIndexOrThrow("cv")),
                estilo = cursor.getString(cursor.getColumnIndexOrThrow("estilo"))
                    ?: "Estilo desconocido", // Valor por defecto si es null
                kms = cursor.getInt(cursor.getColumnIndexOrThrow("kms")),
                fechaCompra = cursor.getString(cursor.getColumnIndexOrThrow("fechaCompra"))
                    ?: "Fecha desconocida", // Valor por defecto si es null
                esPrincipal = 1,
                fotoMoto = cursor.getString(cursor.getColumnIndexOrThrow("fotoMoto"))
                    ?: "" // Valor por defecto si es null
            )
            cursor.close()
            moto
        } else {
            cursor.close()
            null
        }
    }


    init {
        // Abre la base de datos y copia desde assets si no existe
        abrirBaseDeDatos()
    }

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        return cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            it.moveToFirst()
            it.getString(columnIndex)
        }
    }

    fun insertarFotoMoto(idMoto: Int, ruta: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("idMoto", idMoto)
            put("rutaFoto", ruta)
        }
        val result = db.insert("fotosMoto", null, values)
        return result != -1L
    }

    fun obtenerFotosDeMoto(idMoto: Int): List<String> {
        val fotos = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            "FotosMoto", // Nombre de la tabla
            arrayOf("fotosMoto"), // Columna fotosMoto
            "idMoto = ?", // Condición WHERE
            arrayOf(idMoto.toString()), // Convertir idMoto a String para consulta SQL
            null, null, null
        )

        // Recorrer el cursor para obtener las fotos
        if (cursor.moveToFirst()) {
            do {
                val foto = cursor.getString(cursor.getColumnIndex("fotosMoto"))
                fotos.add(foto)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return fotos
    }


    // Función para insertar una foto para una moto
    fun insertarFoto(idMoto: Int, fotoRuta: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("idMoto", idMoto)
            put("fotosMoto", fotoRuta) // Guardamos la ruta de la foto en fotosMoto
        }

        val result = db.insert("FotosMoto", null, values)
        return result != -1L // Si la inserción fue exitosa, retornará un id de fila
    }
}



