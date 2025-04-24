package com.example.torque.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.torque.garaje.Moto
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class TorqueDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, "torque.db", null, 2) {

    private val dbPath = context.applicationContext.getDatabasePath("torque.db").absolutePath


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
    }

    // Obtener todas las motos de la base de datos
    fun obtenerMotos(): List<Moto> {
        val lista = mutableListOf<Moto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM MiGaraje", null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val idMoto = it.getInt(it.getColumnIndexOrThrow("idMoto"))
                    val marca = it.getString(it.getColumnIndexOrThrow("marca"))
                    val modelo = it.getString(it.getColumnIndexOrThrow("modelo"))
                    val anno = it.getInt(it.getColumnIndexOrThrow("anno"))
                    val matricula = it.getString(it.getColumnIndexOrThrow("matricula"))
                    val colorMoto = it.getString(it.getColumnIndexOrThrow("colorMoto"))

                    lista.add(
                        Moto(
                            idMoto = idMoto,
                            marca = marca,
                            modelo = modelo,
                            cilindrada = "",
                            anno = anno,
                            cv = "",
                            estilo = "",
                            matricula = matricula,
                            kms = 0,
                            fechaCompra = "",
                            colorMoto = colorMoto,
                            esPrincipal = 0,
                            fotoMoto = "" // No se guarda la foto directamente aquí, sino en FotosMoto
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
                cilindrada = "",
                cv = "",
                estilo = estilo,
                kms = kms,
                fechaCompra = fechaCompra,
                colorMoto = colorMoto,
                esPrincipal = 0,
                fotoMoto = "" // Foto no incluida directamente aquí
            )
        } else {
            cursor.close()
            null
        }
    }

    // Función para insertar foto en la base de datos
    fun insertarFoto(idMoto: Int, ruta: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("idMoto", idMoto)
            put("rutaFoto", ruta)
        }
        val result = db.insert("FotosMoto", null, contentValues)
        return result != -1L
    }

    // Función para hacer una moto principal
    fun hacerPrincipal(idMoto: String): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("esPrincipal", true)
        }
        val result = db.update("MiGaraje", contentValues, "idMoto = ?", arrayOf(idMoto))
        return result > 0
    }

    // Función para eliminar moto
    fun eliminarMoto(idMoto: String): Boolean {
        val db = writableDatabase
        val result = db.delete("MiGaraje", "idMoto = ?", arrayOf(idMoto))
        return result > 0
    }


    fun obtenerFotosDeMoto(idMoto: Int): List<String> {
        val fotos = mutableListOf<String>()
        val db = readableDatabase
        val cursor: Cursor = db.query(
            "fotosMoto", // Nombre de la tabla
            arrayOf("rutaFoto"), // Columna rutaFoto
            "idMoto = ?", // Condición WHERE
            arrayOf(idMoto.toString()), // Condición para el idMoto
            null, null, null
        )

        // Recorrer el cursor para obtener las rutas de las fotos
        if (cursor.moveToFirst()) {
            do {
                val foto = cursor.getString(cursor.getColumnIndex("rutaFoto"))
                fotos.add(foto)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return fotos
    }

    // Función para eliminar foto
    fun eliminarFoto(idMoto: Int, rutaFoto: String): Boolean {
        val db = writableDatabase
        val result = db.delete(
            "FotosMoto", "idMoto = ? AND rutaFoto = ?", arrayOf(idMoto.toString(), rutaFoto)
        )
        return result > 0
    }

    // Función para obtener la moto principal desde la base de datos
    fun obtenerMotoPrincipal(): Moto? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM MiGaraje WHERE esPrincipal = 1 LIMIT 1", null)

        if (cursor.moveToFirst()) {
            // Verifica que la columna exista antes de acceder a ella
            val idMotoIndex = cursor.getColumnIndex("idMoto")
            val marcaIndex = cursor.getColumnIndex("marca")
            val modeloIndex = cursor.getColumnIndex("modelo")
            // Verifica si los índices son válidos
            if (idMotoIndex != -1 && marcaIndex != -1 && modeloIndex != -1) {
                val moto = Moto(
                    idMoto = cursor.getInt(idMotoIndex),
                    marca = cursor.getString(marcaIndex),
                    modelo = cursor.getString(modeloIndex),
                    anno = 0,
                    matricula = "",
                    cilindrada = "",
                    cv = "",
                    estilo = "",
                    kms = 0,
                    fechaCompra = "",
                    colorMoto = "",
                    esPrincipal = 0,
                    fotoMoto = ""

                )
                cursor.close()
                return moto
            } else {
                // Si alguna columna no se encuentra, manejar el error adecuadamente
                Log.e("DatabaseError", "Una o más columnas no existen en el Cursor")
            }
        }
        cursor.close()
        return null
    }

    // Función para agregar una nueva moto a la base de datos
    fun agregarMoto(moto: Moto): Boolean {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
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
            put("esPrincipal", moto.esPrincipal) // Si estás agregando una moto principal
        }
        val result = db.insert("MiGaraje", null, contentValues)
        return result != -1L
    }


    init {
        // Abre la base de datos y copia desde assets si no existe
        abrirBaseDeDatos()
    }
}
