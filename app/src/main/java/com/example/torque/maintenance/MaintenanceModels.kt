package com.example.torque.maintenance

data class MaintenanceDetailData(
    val maintenanceId: Int = 0,
    val motoId: Long = 0,
    val maintenanceName: String,
    val date: String,
    val kilometers: Int = 0,
    val intervaloKms: Int = 0,
    val price: String? = null,
    val time: String? = null,
    val notes: String? = null,
    val tasks: String? = null
) {
    fun kmsRestantes(kmsActualesMoto: Int): Int =
        (kilometers + intervaloKms) - kmsActualesMoto

    fun progreso(kmsActualesMoto: Int): Float =
        ((kmsActualesMoto - kilometers).toFloat() / intervaloKms).coerceIn(0f, 1f)

    fun estaVencido(kmsActualesMoto: Int): Boolean =
        kmsRestantes(kmsActualesMoto) <= 0
}