package com.example.ctpa.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ctpa.domain.model.Worker
import com.example.ctpa.domain.repository.AuthRepository
import com.example.ctpa.domain.repository.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        loadWorkers()
    }

    private fun loadWorkers() {
        viewModelScope.launch {
            authRepository.getWorkers().collect { workers ->
                _uiState.update { it.copy(workers = workers) }
            }
        }
    }

    fun onWorkerSelected(worker: Worker) {
        _uiState.update {
            it.copy(
                selectedWorker = worker,
                pin = "",
                errorMessage = null
            )
        }
    }

    fun onPinDigitAdded(digit: String) {
        if (_uiState.value.pin.length < 4) {
            val newPin = _uiState.value.pin + digit
            _uiState.update { it.copy(pin = newPin) }

            if (newPin.length == 4) {
                authenticate(newPin)
            }
        }
    }

    fun onPinDigitDeleted() {
        val currentPin = _uiState.value.pin
        if (currentPin.isNotEmpty()) {
            _uiState.update { it.copy(pin = currentPin.dropLast(1), errorMessage = null) }
        }
    }

    fun onClockIn() {
        val pin = _uiState.value.pin
        if (pin.length == 4 && !_uiState.value.isLoading) {
            authenticate(pin)
        }
    }

    fun onForgotPin() {
        _uiState.update {
            it.copy(
                pin = "",
                errorMessage = "Restablece tu PIN contactando a tu administrador o usa la clave maestra."
            )
        }
    }

    fun onSwitchFacility() {
        _uiState.update {
            it.copy(
                pin = "",
                errorMessage = "Cambio de instalación disponible próximamente."
            )
        }
    }

    private fun authenticate(pin: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = authRepository.authenticate(pin)

            when (result) {
                is LoginResult.SuccessAdmin -> {
                    _uiState.update { it.copy(isLoading = false, navigateToAdmin = true) }
                }
                is LoginResult.SuccessWorker -> {
                    _uiState.update { it.copy(isLoading = false, navigateToWorker = result.worker) }
                }
                is LoginResult.InvalidPin -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "PIN incorrecto", pin = "") }
                }
                is LoginResult.Error -> {
                    _uiState.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
            }
        }
    }

    fun resetNavigation() {
        _uiState.update {
            it.copy(
                navigateToAdmin = false,
                navigateToWorker = null,
                pin = "",
                selectedWorker = null
            )
        }
    }
}