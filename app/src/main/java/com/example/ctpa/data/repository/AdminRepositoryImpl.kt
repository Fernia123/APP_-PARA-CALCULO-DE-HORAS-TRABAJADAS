package com.example.ctpa.data.repository


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.example.ctpa.domain.model.AdminStats
import com.example.ctpa.domain.model.AttendanceRecord
import com.example.ctpa.domain.model.WorkerStatus
import com.example.ctpa.domain.repository.AdminRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdminRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdminRepository {

    override suspend fun getAdminStats(): Flow<AdminStats> = flow {
        try {
            // Aquí consultamos Firestore para obtener stats reales
            // Por ahora emitimos datos de prueba para que veas el diseño
            emit(
                AdminStats(
                    totalActiveWorkers = 12,
                    totalPayrollToday = 14909.52,
                    totalHoursLogged = 65190.70,
                    avgRate = 16.00,
                    overtime = "2:14:30",
                    pendingApprovals = 3
                )
            )
        } catch (e: Exception) {
            emit(AdminStats())
        }
    }

    override suspend fun getActiveWorkers(): Flow<List<AttendanceRecord>> = flow {
        try {
            // Datos de prueba basados en el diseño
            emit(
                listOf(
                    AttendanceRecord(
                        id = "087",
                        workerId = "087",
                        workerName = "Mario Silva",
                        elapsedMinutes = 272, // 4h 32m
                        currentPay = 72.53,
                        location = "Sector 7B - Brake Pads",
                        status = WorkerStatus.ACTIVE
                    ),
                    AttendanceRecord(
                        id = "091",
                        workerId = "091",
                        workerName = "Elvira Rios",
                        elapsedMinutes = 375, // 6h 15m
                        currentPay = 93.75,
                        location = "Central Warehouse Unit B",
                        status = WorkerStatus.ON_BREAK
                    ),
                    AttendanceRecord(
                        id = "099",
                        workerId = "099",
                        workerName = "Jorge Quijano",
                        elapsedMinutes = 167, // 2h 47m
                        currentPay = 44.53,
                        location = "Plant 1 - Dock 3",
                        status = WorkerStatus.ACTIVE
                    )
                )
            )
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getOvertimeAlerts(): Flow<List<AttendanceRecord>> = flow {
        try {
            emit(
                listOf(
                    AttendanceRecord(
                        id = "104",
                        workerId = "104",
                        workerName = "Carlos Rodriguez",
                        elapsedMinutes = 496, // 8h 16m
                        currentPay = 132.26,
                        location = "Sector 7 Plant",
                        status = WorkerStatus.ACTIVE,
                        isOvertime = true,
                        overtimeMinutes = 16
                    )
                )
            )
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun approveOvertime(workerId: String) {
        // Actualizar en Firestore
        firestore.collection("overtime_requests")
            .document(workerId)
            .update("status", "approved")
            .await()
    }

    override suspend fun rejectOvertime(workerId: String) {
        firestore.collection("overtime_requests")
            .document(workerId)
            .update("status", "rejected")
            .await()
    }

    override suspend fun forceClockOut(workerId: String) {
        firestore.collection("attendance")
            .document(workerId)
            .update("clockOutTime", com.google.firebase.firestore.FieldValue.serverTimestamp())
            .await()
    }
}