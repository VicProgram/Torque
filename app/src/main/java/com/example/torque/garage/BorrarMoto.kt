package com.example.torque.garage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.torque.room.AppRepository
import kotlinx.coroutines.launch

class BorrarMoto : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val idMoto = intent.getLongExtra("idMoto", -1L)

        if (idMoto != -1L) {
            val repositorio = AppRepository.obtenerInstancia(this)
            
            lifecycleScope.launch {
                try {
                    val moto = repositorio.obtenerMotoPorId(idMoto)
                    if (moto != null) {
                        repositorio.eliminarMoto(moto)
                        setResult(RESULT_OK)
                    } else {
                        setResult(RESULT_CANCELED)
                    }
                } catch (e: Exception) {
                    setResult(RESULT_CANCELED)
                } finally {
                    finish()
                }
            }
        } else {
            finish()
        }
    }
}