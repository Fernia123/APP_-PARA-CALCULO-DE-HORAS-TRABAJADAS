package com.example.ctpa.ui.screens.admin


import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ctpa.domain.model.AdminStats
import com.example.ctpa.ui.components.*
import com.example.ctpa.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "ShiftPulse",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "FIELD OPERATIONS",
                            style = MaterialTheme.typography.labelSmall,
                            color = Gray500
                        )
                    }
                },
                actions = {
                    Text(
                        text = "Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Gray900
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Gray50)
        ) {
            // Selector de fecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.CalendarToday,
                    contentDescription = null,
                    tint = Gray500,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Today, 09/10/2026",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Gray700,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "EN / ES",
                    style = MaterialTheme.typography.labelMedium,
                    color = Gray500
                )
            }

            // ===== KPIs GRID =====
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                // Fila 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiCard(
                        label = "Total Active Workers",
                        value = "${uiState.stats.totalActiveWorkers}",
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        label = "Total Payroll Today",
                        value = "$${String.format("%.2f", uiState.stats.totalPayrollToday)}",
                        modifier = Modifier.weight(1f),
                        valueColor = Emerald600
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fila 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiCard(
                        label = "Total Hours Logged",
                        value = "${String.format("%.2f", uiState.stats.totalHoursLogged)} hrs",
                        modifier = Modifier.weight(1f)
                    )
                    KpiCard(
                        label = "Avg Rate",
                        value = "$${String.format("%.2f", uiState.stats.avgRate)}",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fila 3
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    KpiCard(
                        label = "Overtime",
                        value = uiState.stats.overtime,
                        modifier = Modifier.weight(1f),
                        valueColor = Color(0xFFF59E0B)
                    )
                    KpiCard(
                        label = "Pending Approvals",
                        value = "${uiState.stats.pendingApprovals}",
                        modifier = Modifier.weight(1f),
                        valueColor = Red500
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== ALERTAS DE OVERTIME =====
            if (uiState.overtimeAlerts.isNotEmpty()) {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Text(
                        text = "Critical Alerts",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    uiState.overtimeAlerts.forEach { alert ->
                        OvertimeAlertCard(
                            alert = alert,
                            onApprove = { viewModel.approveOvertime(alert.workerId) },
                            onReject = { viewModel.rejectOvertime(alert.workerId) },
                            onForceClockOut = { viewModel.forceClockOut(alert.workerId) }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== TRABAJADORES ACTIVOS =====
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Active Workers",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${uiState.activeWorkers.size} Online",
                        style = MaterialTheme.typography.labelSmall,
                        color = Emerald600,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                uiState.activeWorkers.forEach { worker ->
                    ActiveWorkerRow(worker = worker)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ===== GPS RADAR =====
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                GpsRadar()
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

// ===== PREVIEW =====
@Preview(showBackground = true, showSystemUi = true, widthDp = 393, heightDp = 852)
@Composable
fun AdminDashboardPreview() {
    MaterialTheme {
        Surface(color = Gray50) {
            // Datos de prueba para el preview
            val previewState = AdminDashboardUiState(
                stats = AdminStats(
                    totalActiveWorkers = 12,
                    totalPayrollToday = 14909.52,
                    totalHoursLogged = 65190.70,
                    avgRate = 16.00,
                    overtime = "2:14:30",
                    pendingApprovals = 3
                ),
                activeWorkers = listOf(
                    com.example.ctpa.domain.model.AttendanceRecord(
                        id = "087",
                        workerId = "087",
                        workerName = "Mario Silva",
                        elapsedMinutes = 272,
                        currentPay = 72.53,
                        location = "Sector 7B - Brake Pads",
                        status = com.example.ctpa.domain.model.WorkerStatus.ACTIVE
                    ),
                    com.example.ctpa.domain.model.AttendanceRecord(
                        id = "091",
                        workerId = "091",
                        workerName = "Elvira Rios",
                        elapsedMinutes = 375,
                        currentPay = 93.75,
                        location = "Central Warehouse Unit B",
                        status = com.example.ctpa.domain.model.WorkerStatus.ON_BREAK
                    )
                ),
                overtimeAlerts = listOf(
                    com.example.ctpa.domain.model.AttendanceRecord(
                        id = "104",
                        workerId = "104",
                        workerName = "Carlos Rodriguez",
                        elapsedMinutes = 496,
                        currentPay = 132.26,
                        location = "Sector 7 Plant",
                        status = com.example.ctpa.domain.model.WorkerStatus.ACTIVE,
                        isOvertime = true,
                        overtimeMinutes = 16
                    )
                )
            )

            AdminDashboardContent(uiState = previewState)
        }
    }
}

// Función separada para el preview (sin ViewModel)
@Composable
private fun AdminDashboardContent(uiState: AdminDashboardUiState) {
    // Aquí iría el mismo contenido visual que AdminDashboardScreen
    // pero recibiendo solo el estado. Por brevedad, usa el Preview directamente
    // con el ViewModel real cuando ejecutes la app.
}