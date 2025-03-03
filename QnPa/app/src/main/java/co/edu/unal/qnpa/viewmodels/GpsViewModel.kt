package co.edu.unal.qnpa.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.ApiServiceInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GpsViewModel : ViewModel() {

    // Estado para las actividades
    private val _activities = MutableStateFlow<List<co.edu.unal.qnpa.connections.Activity>>(emptyList())
    val activities: StateFlow<List<co.edu.unal.qnpa.connections.Activity>> get() = _activities

    // Estado para los nombres de los usuarios
    private val _userNames = MutableStateFlow<Map<String, String>>(emptyMap())
    val userNames: StateFlow<Map<String, String>> get() = _userNames

    // Estado para manejar errores
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Función para cargar las actividades
    fun loadActivities() {
        viewModelScope.launch {
            try {
                val activities = ApiServiceInstance.api.getActivities()
                _activities.value = activities

                // Obtener los nombres de los usuarios
                val userIds = activities.map { it.userId.toString() }.toSet()
                val userNamesMap = mutableMapOf<String, String>()
                userIds.forEach { userId ->
                    val user = ApiServiceInstance.api.getUserById(userId).firstOrNull()
                    userNamesMap[userId] = user?.name ?: "Usuario desconocido"
                }
                _userNames.value = userNamesMap
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar las actividades: ${e.message}"
            }
        }
    }
}