package co.edu.unal.qnpa.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.ApiServiceInstance
import kotlinx.coroutines.launch
import android.util.Log
import co.edu.unal.qnpa.connections.LoginRequest

class LoginViewModel : ViewModel() {
    private val TAG = "LoginViewModel"

    fun loginUser(
        email: String,
        password: String,
        onSuccess: (String) -> Unit, // Ahora recibe userId: String
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                Log.d(TAG, "Iniciando solicitud a la API...")
                // Llamar a la API para autenticar al usuario
                val response = ApiServiceInstance.api.loginUser(LoginRequest(email, password))

                Log.d(TAG, "Respuesta de la API recibida: $response")

                if (response.success == true) {
                    if (response.userId != null) {
                        Log.d(TAG, "Login exitoso. userId: ${response.userId}")
                        // Si el login es exitoso y el userId no es nulo, devolver el userId
                        onSuccess(response.userId)
                    } else {
                        Log.e(TAG, "Login exitoso, pero el userId es nulo.")
                        // Si el userId es nulo, mostrar un mensaje de error
                        onError("Error: No se recibió el userId.")
                    }
                } else {
                    Log.e(TAG, "Login fallido. Mensaje: ${response.message}")
                    // Si el login falla, mostrar el mensaje de error
                    onError(response.message ?: "Error desconocido")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error en la solicitud a la API: ${e.message}", e)
                // Manejar errores de red o API
                onError(e.message ?: "Error de conexión")
            }
        }
    }
}
