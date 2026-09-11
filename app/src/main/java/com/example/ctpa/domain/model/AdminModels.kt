package com.example.ctpa.domain.model


// Registro de asistencia de un trabajador
data class AttendanceRecord(
    val id: String = "",
    val workerId: String = "",
    val workerName: String = "",
    val clockInTime: Long = 0L,
    val clockOutTime: Long? = null,
    val elapsedMinutes: Int = 0,
    val currentPay: Double = 0.0,
    val location: String = "",
    val status: WorkerStatus = WorkerStatus.ACTIVE,
    val isOvertime: Boolean = false,
    val overtimeMinutes: Int = 0
)

// Estados posibles del trabajador
enum class WorkerStatus {
    ACTIVE,
    ON_BREAK,
    CLOCKED_OUT
}

// Estadísticas generales del admin
data class AdminStats(
    val totalActiveWorkers: Int = 0,
    val totalPayrollToday: Double = 0.0,
    val totalHoursLogged: Double = 0.0,
    val avgRate: Double = 0.0,
    val overtime: String = "0:00:00",
    val pendingApprovals: Int = 0
)