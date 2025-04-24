package com.example.torque.database

import android.content.ContentValues
import android.content.Context
import com.example.torque.mantenimientos.MantenimientoDetalle
import java.io.FileOutputStream
import java.io.IOException

// Función para copiar la base de datos desde assets
fun copiarBaseDeDatos(context: Context) {
    val dbFile = context.getDatabasePath("torque.db")
    if (dbFile.exists()) return

    try {
        context.assets.open("torque.db").use { inputStream ->
            FileOutputStream(dbFile).use { outputStream ->
                val buffer = ByteArray(1024)
                var length: Int
                while (inputStream.read(buffer).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }
                outputStream.flush()
            }
        }
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

// Función para guardar un mantenimiento en la base de datos
fun guardarMantenimiento(context: Context, m: MantenimientoDetalle) {
    val dbHelper = TorqueDatabaseHelper(context)  // Crear instancia de TorqueDatabaseHelper
    val db = dbHelper.writableDatabase  // Obtener writableDatabase de la instancia

    val values = ContentValues().apply {
        put("idMoto", m.idMoto)  // Agregar el idMoto
        put("nombre", m.nombreMantenimiento)  // Agregar el nombre del mantenimiento
        put("km", m.km)  // Agregar los kilómetros
        put("precio", m.precio)  // Agregar el precio
        put("tiempo", m.tiempo)  // Agregar el tiempo de servicio
        put("notas", m.notas)  // Agregar las notas
        put("fecha", m.fecha)  // Agregar la fecha del mantenimiento
    }

    // Insertar los valores en la tabla Mantenimientos
    db.insert("Mantenimientos", null, values)
    db.close()  // Cerrar la base de datos

}

