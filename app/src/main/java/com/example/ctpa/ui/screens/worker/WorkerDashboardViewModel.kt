package com.example.ctpa.ui.screens.worker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctpa.domain.model.Task
import com.example.ctpa.domain.model.TaskStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkerDashboardUiState(
    val elapsedSeconds: Long = 0L,
    val startTime: String = "08:00 AM",
    val targetTime: String = "8h 00m",
    val isOnBreak: Boolean = false,
    val isClockedOut: Boolean = false,
    val selectedTab: Int = 0, // 0: All, 1: Active, 2: Done
    val selectedSection: Int = 0, // 0: Tasks, 1: Schedule, 2: History, 3: Profile
    val tasks: List<Task> = emptyList(),
    val message: String? = null
)

@HiltViewModel
class WorkerDashboardViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerDashboardUiState())
    val uiState: StateFlow<WorkerDashboardUiState> = _uiState.asStateFlow()

    init {
        startTimer()
        loadMockTasks() // Cargamos las tareas del diseño de Figma
    }

    // 1. Lógica del Cronómetro
    private fun startTimer() {
        viewModelScope.launch {
            // Simulamos que ya lleva 4h 16m 42s corridos (como en el diseño)
            var seconds = (4 * 3600) + (16 * 60) + 42

            while (isActive) {
                delay(1000) // Actualiza cada segundo
                val state = _uiState.value
                if (state.isClockedOut) break
                if (!state.isOnBreak) {
                    seconds++
                    _uiState.update { it.copy(elapsedSeconds = seconds.toLong()) }
                }
            }
        }
    }

    // 2. Cargar tareas (Mock data basado en tu Figma)
    private fun loadMockTasks() {
        val mockTasks = listOf(
            Task(
                id = "1", title = "HVAC Unit 4 Filter Replacement & Inspection",
                description = "Check airflow pressure valves and replace secondary HEPA media.",
                status = TaskStatus.ACTIVE, estimatedMinutes = 30, elapsedMinutes = 20
            ),
            Task(
                id = "2", title = "Electrical Panel Calibration - Zone B",
                description = "Perform thermal baseline imaging on 480V feeder circuits.",
                status = TaskStatus.IN_PROGRESS, estimatedMinutes = 15, elapsedMinutes = 15
            ),
            Task(
                id = "3", title = "Safety Equipment Check & Log Sign-off",
                description = "Harness integrity, eye-wash station checks, and emergency exits.",
                status = TaskStatus.COMPLETED, signedBy = "Supervisor", signedTime = "05:04"
            ),
            Task(
                id = "4", title = "Cooling Tower Pressure Reading & Fluid Top-off",
                description = "Record gauge levels on primary condenser loop and add treatment if <40%.",
                status = TaskStatus.DONE, elapsedMinutes = 1
            ),
            Task(
                id = "5", title = "Shift Field Photo",
                description = "Document site conditions.",
                status = TaskStatus.DONE, photoCount = 3,
                photoUrls = listOf("url1", "url2", "url3") // Placeholders
            )
        )
        _uiState.update { it.copy(tasks = mockTasks) }
    }

    // 3. Acciones de UI
    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun toggleBreak() {
        if (!_uiState.value.isClockedOut) {
            _uiState.update { it.copy(isOnBreak = !it.isOnBreak) }
        }
    }

    fun onSectionSelected(index: Int) {
        _uiState.update { it.copy(selectedSection = index) }
    }

    fun refreshLog() {
        loadMockTasks()
        _uiState.update {
            it.copy(message = "Logs sincronizados correctamente.")
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun clockOut() {
        _uiState.update { it.copy(isClockedOut = true) }
    }

    fun restartShift() {
        _uiState.update {
            it.copy(
                isClockedOut = false,
                isOnBreak = false,
                elapsedSeconds = 0L
            )
        }
    }

    // Helper para formatear segundos a HH:MM:SS
    fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}