package com.example.torque.garaje

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.torque.database.TorqueDatabaseHelper

class BorrarMoto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val motoId = intent.getIntExtra("idMoto", -1)

        if (motoId != -1) {
            // Lógica para eliminar la moto de la base de datos
            val dbHelper = TorqueDatabaseHelper(this)
            val success = dbHelper.eliminarMoto(motoId.toString())

            if (success) {
                // Si la eliminación fue exitosa, devolver RESULT_OK
                setResult(RESULT_OK)
            } else {
                // Si hubo algún problema, devolver un error
                setResult(RESULT_CANCELED)
            }

            // Finalizar la actividad
            finish()
        }
    }
}