package com.example.torque.database

import Moto
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

    // MiGarajeDatabaseHelper.kt

    fun obtenerMotoPorId(idMoto: Int): Moto? {
        val db = this.readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM MiGaraje WHERE idMoto = ?", arrayOf(idMoto.toString()))

        return if (cursor.moveToFirst()) {
            // Si la moto fue encontrada, devolver el objeto Moto
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("idMoto"))
            val marca = cursor.getString(cursor.getColumnIndexOrThrow("Marca"))
            val modelo = cursor.getString(cursor.getColumnIndexOrThrow("Modelo"))
            val anno = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
            val matricula = cursor.getString(cursor.getColumnIndexOrThrow("Matricula"))
            val color = cursor.getString(cursor.getColumnIndexOrThrow("Color"))
            val cilindrada = cursor.getString(cursor.getColumnIndexOrThrow("Cilindrada"))
            val cv = cursor.getInt(cursor.getColumnIndexOrThrow("Cv"))
            val estilo = cursor.getString(cursor.getColumnIndexOrThrow("Estilo"))
            val kms = cursor.getInt(cursor.getColumnIndexOrThrow("Kms"))
            val fechaCompra = cursor.getString(cursor.getColumnIndexOrThrow("Fecha_compra"))
            val foto = cursor.getString(cursor.getColumnIndexOrThrow("Foto"))

            cursor.close()

            // Devolver una instancia de Moto
            Moto(
                idMoto = id,
                Marca = marca,
                Modelo = modelo,
                Anno = anno,
                Matricula = matricula,
                Color = color,
                Cilindrada = cilindrada,
                Cv = cv,
                Estilo = estilo,
                Kms = kms,
                Fecha_compra = fechaCompra,
                Foto = foto
            )
        } else {
            cursor.close()
            null // Si no se encuentra la moto, devolver null
        }
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
        fun obtenerMotoPorId(idMoto: Int): Moto? {
            val db = this.readableDatabase
            val cursor =
                db.rawQuery("SELECT * FROM MiGaraje WHERE idMoto = ?", arrayOf(idMoto.toString()))

            return if (cursor.moveToFirst()) {
                // Si la moto fue encontrada, devolver el objeto Moto
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("idMoto"))
                val marca = cursor.getString(cursor.getColumnIndexOrThrow("Marca"))
                val modelo = cursor.getString(cursor.getColumnIndexOrThrow("Modelo"))
                val anno = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
                val matricula = cursor.getString(cursor.getColumnIndexOrThrow("Matricula"))
                val color = cursor.getString(cursor.getColumnIndexOrThrow("Color"))
                val cilindrada = cursor.getString(cursor.getColumnIndexOrThrow("Cilindrada"))
                val cv = cursor.getInt(cursor.getColumnIndexOrThrow("Cv"))
                val estilo = cursor.getString(cursor.getColumnIndexOrThrow("Estilo"))
                val kms = cursor.getInt(cursor.getColumnIndexOrThrow("Kms"))
                val fechaCompra = cursor.getString(cursor.getColumnIndexOrThrow("Fecha_compra"))
                val foto = cursor.getString(cursor.getColumnIndexOrThrow("Foto"))

                cursor.close()

                // Devolver una instancia de Moto
                Moto(
                    idMoto = id,
                    Marca = marca,
                    Modelo = modelo,
                    Anno = anno,
                    Matricula = matricula,
                    Color = color,
                    Cilindrada = cilindrada,
                    Cv = cv,
                    Estilo = estilo,
                    Kms = kms,
                    Fecha_compra = fechaCompra,
                    Foto = foto
                )
            } else {
                cursor.close()
                null // Si no se encuentra la moto, devolver null
            }
        }

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
                        idMoto = 0,
                        Cilindrada = "",
                        Cv = 0,
                        Estilo = "",
                        Kms = 0,
                        Fecha_compra = "",
                        Color = "",
                        Foto = ""
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return lista
    }



}



