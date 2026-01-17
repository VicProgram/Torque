package com.example.torque.room.entity

@Entity(tableName = "motos")
data class MotoEntity(
    @PrimaryKey(autoGenerate = true) val idMoto: Long = 0,
    val marca: String,
    val modelo: String,
    val anno: Int = 0,
    val matricula: String? = null,
    val colorMoto: String? = null,
    val cilindrada: String? = null,
    val cv: String? = null,
    val estilo: String? = null,
    val kms: Int = 0,
    val fechaCompra: String? = null, // ISO yyyy-MM-dd preferible
    val esPrincipal: Int = 0,        // 0 = no, 1 = sí
    val fotoUri: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)