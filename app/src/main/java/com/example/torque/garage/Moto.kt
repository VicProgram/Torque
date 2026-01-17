package com.example.torque.garage

data class Moto(
    val idMoto: Long = 0,
    val marca: String,
    val modelo: String,
    val anno: Int,
    val matricula: String,
    val colorMoto: String?,
    val cilindrada: String?,
    val cv: String?,
    val estilo: String = "",
    val kms: Int = 0,
    val fechaCompra: String = "",
    val esPrincipal: Int = 0,
    val fotoUri: String? = null
)