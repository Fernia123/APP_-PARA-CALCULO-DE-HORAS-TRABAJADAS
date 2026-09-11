package com.example.ctpa.ui.screens.login

import com.example.ctpa.domain.model.Worker
data class LoginUiState(
    val workers: List<Worker> = emptyList(),
    val selectedWorker: Worker? = null,
    val pin: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val navigateToAdmin: Boolean = false,
    val navigateToWorker: Worker? = null
)