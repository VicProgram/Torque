package com.example.torque.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "motos")
data class MotoEntity(
    @PrimaryKey(autoGenerate = true) val idMoto: Long = 0,
    val marca: String = "",
    val modelo: String = "",
    val anno: Int = 0,
    val matricula: String? = null,
    val colorMoto: String? = null,
    val cilindrada: String? = null,
    val cv: String? = null,
    val estilo: String? = null,
    val kms: Int = 0,
    val fechaCompra: String? = null,
    val esPrincipal: Int = 0,
    val fotoUri: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)