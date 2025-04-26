package com.example.torque

data class Usuario(
    val idUsuario: Int? = null,
    val nombre: String,
    val email: String,
    val passwordHash: String
)