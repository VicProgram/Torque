package com.example.torque.database

import android.content.Context
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

