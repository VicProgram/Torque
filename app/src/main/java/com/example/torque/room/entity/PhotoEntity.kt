package com.example.torque.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moto_photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val motoId: Long,
    val path: String, // internal path or URI string
    val createdAt: String? = null
)