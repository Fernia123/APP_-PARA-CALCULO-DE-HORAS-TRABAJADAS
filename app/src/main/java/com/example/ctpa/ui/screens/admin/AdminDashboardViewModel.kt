package com.example.ctpa.ui.screens.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctpa.domain.model.AdminStats
import com.example.ctpa.domain.model.AttendanceRecord
import com.example.ctpa.domain.repository.AdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdminDashboardUiState(
    val stats: AdminStats = AdminStats(),
    val activeWorkers: List<AttendanceRecord> = emptyList(),
    val overtimeAlerts: List<AttendanceRecord> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            adminRepository.getAdminStats().collect { stats ->
                _uiState.update { it.copy(stats = stats, isLoading = false) }
            }
        }

        viewModelScope.launch {
            adminRepository.getActiveWorkers().collect { workers ->
                _uiState.update { it.copy(activeWorkers = workers) }
            }
        }

        viewModelScope.launch {
            adminRepository.getOvertimeAlerts().collect { alerts ->
                _uiState.update { it.copy(overtimeAlerts = alerts) }
            }
        }
    }

    fun approveOvertime(workerId: String) {
        viewModelScope.launch {
            adminRepository.approveOvertime(workerId)
        }
    }

    fun rejectOvertime(workerId: String) {
        viewModelScope.launch {
            adminRepository.rejectOvertime(workerId)
        }
    }

    fun forceClockOut(workerId: String) {
        viewModelScope.launch {
            adminRepository.forceClockOut(workerId)
        }
    }
}