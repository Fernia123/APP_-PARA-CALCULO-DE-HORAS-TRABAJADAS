package com.example.ctpa.domain.repository

import com.example.ctpa.domain.model.Worker

import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    // Obtiene la lista de trabajadores para mostrar en el selector
    suspend fun getWorkers(): Flow<List<Worker>>

    // Valida el PIN. Devuelve un resultado sellado (sealed)
    suspend fun authenticate(pin: String): LoginResult
}

// Clase sellada para manejar los diferentes resultados del login de forma segura
sealed class LoginResult {
    object SuccessAdmin : LoginResult()
    data class SuccessWorker(val worker: Worker) : LoginResult()
    object InvalidPin : LoginResult()
    data class Error(val message: String) : LoginResult()
}
