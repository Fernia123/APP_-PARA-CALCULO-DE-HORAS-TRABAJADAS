package com.example.ctpa.data.repository


import com.example.ctpa.domain.repository.AuthRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.example.ctpa.domain.model.Worker
import com.example.ctpa.domain.repository.LoginResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AuthRepository {

    override suspend fun getWorkers(): Flow<List<Worker>> = flow {
        try {
            // Obtener trabajadores activos de Firestore
            val snapshot = firestore.collection("workers")
                .whereEqualTo("isActive", true)
                .get()
                .await()

            // Mapear documentos a objetos Worker
            val workers = snapshot.documents.mapNotNull { doc ->
                doc.toObject(Worker::class.java)?.copy(id = doc.id)
            }

            emit(workers)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun authenticate(pin: String): LoginResult {
        return try {
            // 1. Verificar si es la Clave Maestra (Admin)
            val adminDoc = firestore.collection("config")
                .document("admin_master")
                .get()
                .await()

            if (adminDoc.exists()) {
                val adminPin = adminDoc.getString("pin")
                if (adminPin == pin) {
                    return LoginResult.SuccessAdmin
                }
            }

            // 2. Verificar si es un PIN de Trabajador
            val workerSnapshot = firestore.collection("workers")
                .whereEqualTo("pin", pin)
                .limit(1)
                .get()
                .await()

            if (workerSnapshot.documents.isNotEmpty()) {
                val doc = workerSnapshot.documents.first()
                val worker = doc.toObject(Worker::class.java)
                if (worker != null) {
                    return LoginResult.SuccessWorker(worker.copy(id = doc.id))
                }
            }

            // 3. Si no coincide con ninguno
            LoginResult.InvalidPin

        } catch (e: Exception) {
            LoginResult.Error(e.message ?: "Error de conexión")
        }
    }
}