package com.example.ctpa.ui.screens.login


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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ctpa.domain.model.Worker
import com.example.ctpa.ui.components.*
import com.example.ctpa.ui.theme.*

// ===== PANTALLA REAL (con ViewModel) =====
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToAdmin: () -> Unit,
    onNavigateToWorker: (Worker) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.navigateToAdmin) {
        if (uiState.navigateToAdmin) {
            onNavigateToAdmin()
            viewModel.resetNavigation()
        }
    }
    LaunchedEffect(uiState.navigateToWorker) {
        uiState.navigateToWorker?.let { worker ->
            onNavigateToWorker(worker)
            viewModel.resetNavigation()
        }
    }

    LoginScreenContent(
        uiState = uiState,
        onWorkerSelected = viewModel::onWorkerSelected,
        onPinDigitAdded = viewModel::onPinDigitAdded,
        onPinDigitDeleted = viewModel::onPinDigitDeleted,
        onClockIn = viewModel::onClockIn,
        onForgotPin = viewModel::onForgotPin,
        onSwitchFacility = viewModel::onSwitchFacility
    )
}

// ===== CONTENIDO VISUAL (reutilizable y previsualizable) =====
@Composable
fun LoginScreenContent(
    uiState: LoginUiState,
    onWorkerSelected: (Worker) -> Unit,
    onPinDigitAdded: (String) -> Unit,
    onPinDigitDeleted: () -> Unit,
    onClockIn: () -> Unit,
    onForgotPin: () -> Unit,
    onSwitchFacility: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray50)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ===== HEADER =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "ShiftPulse",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Gray900
                    )
                    Text(
                        text = "FIELD OPERATIONS",
                        style = MaterialTheme.typography.labelSmall,
                        color = Gray500,
                        letterSpacing = 1.sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Emerald50)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.LocationOn,
                                contentDescription = null,
                                tint = Emerald500,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Site #42-North",
                                style = MaterialTheme.typography.labelSmall,
                                color = Emerald600,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    Text(
                        text = "EN / ES",
                        style = MaterialTheme.typography.labelMedium,
                        color = Gray500,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== WELCOME =====
            Text(
                text = "Welcome back!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Gray900,
                fontSize = 32.sp
            )
            Text(
                text = "Select worker profile & enter 4-digit PIN to clock in.",
                style = MaterialTheme.typography.bodyMedium,
                color = Gray500,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ===== WORKER CARD =====
            if (uiState.workers.isNotEmpty()) {
                val selected = uiState.selectedWorker ?: uiState.workers.first()
                WorkerCard(
                    worker = selected,
                    isSelected = true,
                    onClick = {
                        val index = uiState.workers.indexOf(selected)
                        val next = uiState.workers[(index + 1) % uiState.workers.size]
                        onWorkerSelected(next)
                    }
                )
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = White)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "No workers registered", color = Gray500)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== PIN SECTION =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "AUTHENTICATION PIN",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Gray700,
                    letterSpacing = 0.5.sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = null,
                        tint = Emerald500,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Secure Terminal",
                        style = MaterialTheme.typography.labelSmall,
                        color = Gray500
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            PinInput(
                pin = uiState.pin,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (uiState.pin.length == 3)
                    "Enter last digit to activate facial verification"
                else "Enter your 4-digit PIN",
                style = MaterialTheme.typography.bodySmall,
                color = if (uiState.pin.length == 3) Emerald500 else Gray500,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ===== KEYPAD =====
            Keypad(
                onDigitClick = onPinDigitAdded,
                onDeleteClick = onPinDigitDeleted
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ===== CLOCK IN BUTTON =====
            Button(
                onClick = onClockIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                enabled = uiState.selectedWorker != null && uiState.pin.length == 4 && !uiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Emerald500,
                    disabledContainerColor = Gray300
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Clock In / Entrada",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Verifies with photo",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "07:04 AM",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                Icons.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== FOOTER LINKS =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = onForgotPin) {
                    Icon(
                        Icons.Filled.Lock,
                        contentDescription = null,
                        tint = Gray500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Forgot PIN?", color = Gray500)
                }
                TextButton(onClick = onSwitchFacility) {
                    Icon(
                        Icons.Filled.CompareArrows,  // ✅ Cambiado de SwapHoriz
                        contentDescription = null,
                        tint = Gray500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Switch Facility", color = Gray500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ===== STATUS BAR =====
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.LocationOn,
                    contentDescription = null,
                    tint = Gray400,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Station #12  •  Terminal GPS Locked  •  v4.8",
                    style = MaterialTheme.typography.labelSmall,
                    color = Gray400
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ===== ERROR MESSAGE =====
            uiState.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = error,
                        color = Red500,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ===== PREVIEW =====
@Preview(showBackground = true, showSystemUi = true, widthDp = 393, heightDp = 852)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        Surface(color = Gray50) {
            val previewWorkers = listOf(
                Worker(
                    id = "104",
                    name = "Carlos Rodriguez",
                    hourlyRate = 16.0,
                    photoUrl = "",
                    isActive = true,
                    shiftStart = "07:00",
                    shiftEnd = "15:30",
                    facility = "Tech"
                )
            )

            LoginScreenContent(
                uiState = LoginUiState(
                    workers = previewWorkers,
                    selectedWorker = previewWorkers.first(),
                    pin = "123",
                    errorMessage = null
                ),
                onWorkerSelected = {},
                onPinDigitAdded = {},
                onPinDigitDeleted = {},
                onClockIn = {},
                onForgotPin = {},
                onSwitchFacility = {}
            )
        }
    }
}