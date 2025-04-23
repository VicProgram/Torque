package com.example.torque.database

import com.example.torque.Moto
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TorqueDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "torque.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        // No crear nada si copias la base
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Lógica de migración si actualizas
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
                    val marca = it.getString(it.getColumnIndexOrThrow("Marca"))
                    val modelo = it.getString(it.getColumnIndexOrThrow("Modelo"))
                    val anno = it.getInt(it.getColumnIndexOrThrow("Año"))
                    val matricula = it.getString(it.getColumnIndexOrThrow("Matricula"))
                    val colorMoto = it.getString(it.getColumnIndexOrThrow("Color_Moto"))

                    lista.add(
                        Moto(
                            idMoto = idMoto.toString(),
                            marca = marca,
                            modelo = modelo,
                            cilindrada = "",
                            anno = anno,
                            cv = 0,
                            estilo = "",
                            matricula = matricula,
                            kms = 0,
                            fecha_compra = "",
                            color_moto = colorMoto,
                            esPrincipal = 0,
                            foto_moto = ""
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
            val marca = cursor.getString(cursor.getColumnIndexOrThrow("Marca"))
            val modelo = cursor.getString(cursor.getColumnIndexOrThrow("Modelo"))
            val anno = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
            val matricula = cursor.getString(cursor.getColumnIndexOrThrow("Matricula"))
            val colorMoto =
                cursor.getString(cursor.getColumnIndexOrThrow("Color_Moto")) // Usar el nuevo campo
            val cilindrada = cursor.getString(cursor.getColumnIndexOrThrow("Cilindrada"))
            val cv = cursor.getInt(cursor.getColumnIndexOrThrow("Cv"))
            val estilo = cursor.getString(cursor.getColumnIndexOrThrow("Estilo"))
            val kms = cursor.getInt(cursor.getColumnIndexOrThrow("Kms"))
            val fechaCompra = cursor.getString(cursor.getColumnIndexOrThrow("Fecha_Compra"))

            cursor.close()

            Moto(
                idMoto = id.toString(),
                marca = marca,
                modelo = modelo,
                anno = anno,
                matricula = matricula,
                cilindrada = cilindrada,
                cv = cv,
                estilo = estilo,
                kms = kms,
                fecha_compra = fechaCompra,
                color_moto = colorMoto,
                esPrincipal = 0,
                foto_moto = ""
            )
        } else {
            cursor.close()
            null
        }
    }

    fun agregarMoto(moto: Moto): Boolean {
        val db = writableDatabase
        val cvmap = ContentValues().apply {
            put("Marca", moto.marca)
            put("Modelo", moto.modelo)
            put("Año", moto.anno)              // <-- Mismo nombre que en la tabla
            put("Matricula", moto.matricula)
            put("Color_Moto", moto.color_moto)
            put("Cilindrada", moto.cilindrada)
            put("Cv", moto.cv)
            put("Estilo", moto.estilo)
            put("Kms", moto.kms)
            put("Fecha_Compra", moto.fecha_compra)
        }
        val rowId = db.insert("MiGaraje", null, cvmap)
        db.close()
        return rowId != -1L
    }


    // TorqueDatabaseHelper.kt
    fun eliminarMoto(idMoto: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(
            "MiGaraje", // El nombre de la tabla donde guardas las motos
            "idMoto = ?",
            arrayOf(idMoto.toString())
        )
        return result > 0 // Si se elimina al menos una fila, la eliminación fue exitosa
    }

}
