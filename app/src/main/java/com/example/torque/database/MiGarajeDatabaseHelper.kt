package com.example.torque.database

import Moto
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class MiGarajeDatabaseHelper(context: Context) : SQLiteOpenHelper(context, "torque.db", null, 2) {

    init {
        copiarBaseDeDatos(context) // Copia solo si no existe
    }

    override fun onCreate(db: SQLiteDatabase) {
        // No se usa, ya que la base de datos se precarga
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // No es necesario modificar la estructura si solo deseas mantener la base actualizada
    }

    // Copiar la base de datos desde los assets si no existe en el dispositivo
    fun copiarBaseDeDatos(context: Context) {
        val dbFile = context.getDatabasePath("torque.db")
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

    // Obtener todas las motos de la base de datos
    fun obtenerMotos(): List<Moto> {
        val lista = mutableListOf<Moto>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM MiGaraje", null)

        if (cursor.moveToFirst()) {
            do {
                val idMoto = cursor.getInt(cursor.getColumnIndexOrThrow("idMoto"))
                val marca = cursor.getString(cursor.getColumnIndexOrThrow("Marca"))
                val modelo = cursor.getString(cursor.getColumnIndexOrThrow("Modelo"))
                val anno = cursor.getInt(cursor.getColumnIndexOrThrow("Año"))
                val matricula = cursor.getString(cursor.getColumnIndexOrThrow("Matricula"))

                lista.add(
                    Moto(
                        idMoto = idMoto.toString(),
                        Marca = marca,
                        Modelo = modelo,
                        Cilindrada = "",
                        Anno = anno,
                        Cv = 0,
                        Estilo = "",
                        Matricula = matricula,
                        Kms = 0,
                        Fecha_compra = "",
                        Color = ""
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
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
            val color_moto = cursor.getString(cursor.getColumnIndexOrThrow("Color_Moto"))
            val cilindrada = cursor.getString(cursor.getColumnIndexOrThrow("Cilindrada"))
            val cv = cursor.getInt(cursor.getColumnIndexOrThrow("Cv"))
            val estilo = cursor.getString(cursor.getColumnIndexOrThrow("Estilo"))
            val kms = cursor.getInt(cursor.getColumnIndexOrThrow("Kms"))
            val fechaCompra = cursor.getString(cursor.getColumnIndexOrThrow("Fecha_Compra"))

            cursor.close()

            Moto(
                idMoto = id.toString(),
                Marca = marca,
                Modelo = modelo,
                Anno = anno,
                Matricula = matricula,
                Cilindrada = cilindrada,
                Cv = cv,
                Estilo = estilo,
                Kms = kms,
                Fecha_compra = fechaCompra,
                Color = color_moto
            )
        } else {
            cursor.close()
            null
        }
    }
}
