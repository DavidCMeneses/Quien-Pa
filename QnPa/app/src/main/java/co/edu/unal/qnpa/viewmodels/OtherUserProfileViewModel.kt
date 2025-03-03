package co.edu.unal.qnpa.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.ApiServiceInstance
import co.edu.unal.qnpa.connections.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class OtherUserProfileViewModel : ViewModel() {
    // Estado del usuario
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    // Estado de la calificación
    private val _rating = MutableStateFlow(0.0)
    val rating: StateFlow<Double> = _rating.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Función para obtener los datos de otro usuario y su calificación
    fun fetchUserData(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Obtener la información del otro usuario desde la API
                val user = ApiServiceInstance.api.getUserById(userId).firstOrNull()
                _user.value = user

                // Obtener la calificación del otro usuario y redondear al primer decimal
                val ratingResponse = ApiServiceInstance.api.getAverageRatingForUser(userId)
                val roundedRating = String.format(Locale.US, "%.1f", ratingResponse.averageRating ?: 0.0).toDouble()
                _rating.value = roundedRating
            } catch (e: Exception) {
                _error.value = "Error al obtener los datos del usuario: ${e.message}"
                Log.e("OtherUserProfileViewModel", "Error fetching user data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
