package com.example.torque.mantenimientos

data class MantenimientoDetalle(
    val mantenimientoId: Int = 0,
    val nombreMantenimiento: String,
    val date: String,
    val kilometers: Int?,  // Usa "kilometers" en inglés, consistente con uso posterior
    val marca: String?,
    val modelo: String?,
    val precio: String?
)
