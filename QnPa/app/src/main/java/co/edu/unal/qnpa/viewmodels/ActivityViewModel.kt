package co.edu.unal.qnpa.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.Activity
import co.edu.unal.qnpa.connections.ApiServiceInstance
import co.edu.unal.qnpa.connections.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {
    private val _activity = MutableStateFlow<Activity?>(null)
    val activity: StateFlow<Activity?> get() = _activity

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    private val _creatorName = MutableStateFlow<String?>(null)
    val creatorName: StateFlow<String?> get() = _creatorName

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> get() = _error

    fun loadActivityDetails(activityId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val activity = ApiServiceInstance.api.getActivityById(activityId)
                _activity.value = activity ?: throw Exception("Datos no disponibles")
                _categories.value = ApiServiceInstance.api.getCategoriesByActivity(activityId)
                val creator = activity.userId?.let { ApiServiceInstance.api.getUserById(it.toString()).firstOrNull() }
                _creatorName.value = creator?.name ?: "Usuario desconocido"
            } catch (e: Exception) {
                _error.value = "Error al cargar los datos: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}