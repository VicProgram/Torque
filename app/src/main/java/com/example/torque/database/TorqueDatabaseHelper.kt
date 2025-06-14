package com.example.torque.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.torque.Usuario
import com.example.torque.garaje.Moto
import com.example.torque.mantenimientos.MantenimientoDetalle
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class TorqueDatabaseHelper(private val context: Context) :
    SQLiteOpenHelper(context, "torque.db", null, 2) {

    private val dbPath = context.applicationContext.getDatabasePath("torque.db").absolutePath

    //--------------------------BASEDEDATOS-------------------------------------------//
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


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Aquí puedes poner la lógica para migrar la base de datos si es necesario.
    }
    //--------------------------MOTOS-------------------------------------------

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
            cursor.getString(cursor.getColumnIndexOrThrow("cilindrada"))
            cursor.getInt(cursor.getColumnIndexOrThrow("cv"))
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

        db.beginTransaction()
        return try {
            // Primero, desmarcar todas las motos como principal
            val contentValuesFalse = ContentValues().apply {
                put("esPrincipal", false)
            }
            db.update("MiGaraje", contentValuesFalse, null, null)

            // Luego marcar solo la moto con idMoto como principal
            val contentValuesTrue = ContentValues().apply {
                put("esPrincipal", true)
            }
            val result = db.update("MiGaraje", contentValuesTrue, "idMoto = ?", arrayOf(idMoto))

            db.setTransactionSuccessful()
            result > 0
        } finally {
            db.endTransaction()
        }
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


    //--------------------------USUARIOS-------------------------------------------
    override fun onCreate(db: SQLiteDatabase?) {
        // No crear nada si copiamos la base de datos desde assets
    }

    companion object {
        private const val TABLE_USUARIOS = "Usuarios" // Nombre de la tabla de usuarios
        private const val COLUMN_ID_USUARIO = "idUsuario" // Columna para el ID del usuario
        private const val COLUMN_NOMBRE_USUARIO = "nombre"
        private const val COLUMN_EMAIL_USUARIO = "email"
        private const val COLUMN_PASSWORD_HASH_USUARIO = "passwordHash"
        private const val COLUMN_FOTO_PERFIL_USUARIO = "fotoPerfil"
    }


    fun insertarUsuario(
        nombre: String,
        email: String,
        password: String // Cambiamos passwordHash a password
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NOMBRE_USUARIO, nombre)
            put(COLUMN_EMAIL_USUARIO, email)
            put(COLUMN_PASSWORD_HASH_USUARIO, password)
            put(COLUMN_FOTO_PERFIL_USUARIO, "")
        }
        return db.insert(TABLE_USUARIOS, null, values)
    }


    fun obtenerUsuarioPorCredencial(credencial: String): Usuario? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(
                COLUMN_ID_USUARIO,
                COLUMN_NOMBRE_USUARIO,
                COLUMN_EMAIL_USUARIO,
                COLUMN_PASSWORD_HASH_USUARIO,
                COLUMN_FOTO_PERFIL_USUARIO

            ),
            "$COLUMN_EMAIL_USUARIO = ? OR $COLUMN_NOMBRE_USUARIO = ?",
            arrayOf(credencial, credencial),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                Usuario(

                    idUsuario = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_USUARIO)),
                    nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NOMBRE_USUARIO)),
                    email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL_USUARIO)),
                    passwordHash = it.getString(
                        it.getColumnIndexOrThrow(
                            COLUMN_PASSWORD_HASH_USUARIO
                        )
                    )
                )
            } else {
                null
            }
        }
    }

    fun obtenerUsuarioPorId(userId: Int): Usuario? {
        val db = readableDatabase
        var usuario: Usuario? = null
        val cursor = db.query(
            TABLE_USUARIOS,
            arrayOf(
                COLUMN_ID_USUARIO,
                COLUMN_NOMBRE_USUARIO,
                COLUMN_EMAIL_USUARIO,
                COLUMN_FOTO_PERFIL_USUARIO
            ),
            "$COLUMN_ID_USUARIO = ?",
            arrayOf(userId.toString()),
            null, null, null
        )

        cursor.use {
            if (it.moveToFirst()) {
                val id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_USUARIO))
                val nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NOMBRE_USUARIO))
                val email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL_USUARIO))
                val fotoPerfil = it.getString(it.getColumnIndexOrThrow(COLUMN_FOTO_PERFIL_USUARIO))
                usuario = Usuario(id, nombre, email, fotoPerfil)
            }
        }
        db.close()
        return usuario
    }

    //--------------------------Mantenimientos-------------------------------------------

    fun insertarMantenimiento(m: MantenimientoDetalle) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombreMantenimiento", m.nombreMantenimiento)
            put("date", m.date)
            put("kilometers", m.kilometers)
            put("marca", m.marca)
            put("modelo", m.modelo)
            put("anno", m.anno)
            put("precio", m.precio)
            put("tiempo", m.tiempo)
            put("notas", m.notas)
            put("tareas", m.tareas)
        }
        db.insert("Mantenimientos", null, values)
    }


    fun obtenerMantenimientos(): List<MantenimientoDetalle> {
        val lista = mutableListOf<MantenimientoDetalle>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Mantenimientos", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("mantenimientoId"))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombreMantenimiento"))
                val fecha = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val km = cursor.getInt(cursor.getColumnIndexOrThrow("kilometers"))
                val marca = cursor.getString(cursor.getColumnIndexOrThrow("marca"))
                val modelo = cursor.getString(cursor.getColumnIndexOrThrow("modelo"))
                val anno = cursor.getInt(cursor.getColumnIndexOrThrow("anno"))
                val precio = cursor.getString(cursor.getColumnIndexOrThrow("precio"))
                val tiempo = cursor.getString(cursor.getColumnIndexOrThrow("tiempo"))
                val notas = cursor.getString(cursor.getColumnIndexOrThrow("notas"))
                val tareas = cursor.getString(cursor.getColumnIndexOrThrow("tareas"))

                lista.add(
                    MantenimientoDetalle(
                        mantenimientoId = id,
                        nombreMantenimiento = nombre,
                        date = fecha,
                        kilometers = km,
                        marca = marca,
                        modelo = modelo,
                        anno = anno,
                        precio = precio,
                        tiempo = tiempo,
                        notas = notas,
                        tareas = tareas
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return lista
    }

    fun Cursor.getStringOrNull(columnName: String): String? =
        if (isNull(getColumnIndexOrThrow(columnName))) null else getString(
            getColumnIndexOrThrow(
                columnName
            )
        )

    fun Cursor.getIntOrNull(columnName: String): Int? =
        if (isNull(getColumnIndexOrThrow(columnName))) null else getInt(
            getColumnIndexOrThrow(
                columnName
            )
        )

}


