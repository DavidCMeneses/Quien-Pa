package co.edu.unal.qnpa.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.ApiServiceInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel : ViewModel() {
    // Estado de la URL de la imagen del usuario
    private val _userImageUrl = MutableStateFlow<String?>(null)
    val userImageUrl: StateFlow<String?> = _userImageUrl.asStateFlow()

    // Estado de carga
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado de error
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Función para cargar la imagen del usuario
    fun loadUserImage(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // Obtener el usuario desde la API
                val user = ApiServiceInstance.api.getUserById(userId).firstOrNull()
                _userImageUrl.value = user?.imageUrl
            } catch (e: Exception) {
                _error.value = "Error al cargar la imagen del usuario: ${e.message}"
                Log.e("HomeScreenViewModel", "Error loading user image", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}