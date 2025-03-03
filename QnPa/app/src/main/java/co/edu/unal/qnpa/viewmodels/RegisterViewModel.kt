package co.edu.unal.qnpa.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.ApiServiceInstance
import co.edu.unal.qnpa.connections.User
import kotlinx.coroutines.launch


class RegisterViewModel : ViewModel() {

    // Función para verificar si un correo ya está registrado
    private suspend fun isEmailRegistered(email: String): Boolean {
        return try {
            Log.d("RegisterViewModel", "Verificando si el correo $email ya está registrado...")
            val users = ApiServiceInstance.api.getUsers()
            users.any { it.email == email }
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "Error al verificar el correo: ${e.message}", e)
            false
        }
    }

    // Función para verificar si un nombre de usuario ya está registrado
    private suspend fun isUsernameRegistered(username: String): Boolean {
        return try {
            Log.d("RegisterViewModel", "Verificando si el nombre de usuario $username ya está registrado...")
            val users = ApiServiceInstance.api.getUsers()
            users.any { it.name == username }
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "Error al verificar el nombre de usuario: ${e.message}", e)
            false
        }
    }

    // Función para registrar un nuevo usuario
    fun registerUser(
        username: String,
        email: String,
        password: String,
        age: Int,
        imageUrl: String?, // Nuevo parámetro para la URL de la imagen
        onSuccess: (userId: String, email: String) -> Unit, // Devolver userId y email
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Verificar si el correo ya está registrado
                if (isEmailRegistered(email)) {
                    onError("El correo electrónico ya está registrado")
                    return@launch
                }

                // Verificar si el nombre de usuario ya está registrado
                if (isUsernameRegistered(username)) {
                    onError("El nombre de usuario ya está en uso")
                    return@launch
                }

                // Crear un nuevo objeto User con la URL de la imagen
                val newUser = User(
                    id = null,
                    name = username,
                    email = email,
                    age = age,
                    description = null,
                    password = password,
                    imageUrl = imageUrl // Incluir la URL de la imagen
                )

                Log.d("RegisterViewModel", "Intentando registrar al usuario: $newUser")

                // Llamar a la API para crear el usuario
                val response = ApiServiceInstance.api.createUser(newUser)

                Log.d("RegisterViewModel", "Respuesta del servidor: $response")

                // Si el registro es exitoso, devolver el userId y el email
                onSuccess(response.id.toString(), email) // Asegúrate de que la API devuelva un ID
            } catch (e: Exception) {
                // Manejar errores
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}