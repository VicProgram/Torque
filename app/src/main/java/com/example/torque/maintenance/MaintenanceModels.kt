package com.example.torque.maintenance

data class MaintenanceDetailData(
    val maintenanceId: Int = 0,
    val maintenanceName: String,
    val date: String,
    val kilometers: Int?,
    val brand: String?,
    val model: String?,
    val year: Int?,
    val price: String?,
    val time: String?,
    val notes: String?,
    val tasks: String?
)
