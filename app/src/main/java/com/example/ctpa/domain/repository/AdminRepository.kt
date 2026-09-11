package com.example.ctpa.domain.repository



import com.example.ctpa.domain.model.AdminStats
import com.example.ctpa.domain.model.AttendanceRecord
import kotlinx.coroutines.flow.Flow

interface AdminRepository {
    // Estadísticas en tiempo real
    suspend fun getAdminStats(): Flow<AdminStats>

    // Trabajadores activos con su estado
    suspend fun getActiveWorkers(): Flow<List<AttendanceRecord>>

    // Alertas de overtime pendientes
    suspend fun getOvertimeAlerts(): Flow<List<AttendanceRecord>>

    // Acciones del admin
    suspend fun approveOvertime(workerId: String)
    suspend fun rejectOvertime(workerId: String)
    suspend fun forceClockOut(workerId: String)
}