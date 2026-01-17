package com.example.torque.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mantenimientos")
data class MaintenanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val motoId: Long,
    val maintenanceName: String,
    val description: String? = null,
    val km: Int? = null,
    val fecha: String? = null, // ISO date
    val coste: Double? = null,
    val createdAt: String? = null
)