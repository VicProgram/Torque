package com.example.torque



data class mantenimientos(
    val id: Long,
    val nombre: String,
    val categoria: String,
    val estado: Boolean,
    val fecha: String, // Agregado
    val kilometros: Int // Agregado
)
