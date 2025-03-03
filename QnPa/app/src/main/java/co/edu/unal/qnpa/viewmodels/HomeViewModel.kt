package co.edu.unal.qnpa.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.Activity
import co.edu.unal.qnpa.connections.ApiServiceInstance
import co.edu.unal.qnpa.connections.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class HomeViewModel : ViewModel() {
    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> get() = _activities

    private val _filteredActivities = MutableStateFlow<List<Activity>>(emptyList())
    val filteredActivities: StateFlow<List<Activity>> get() = _filteredActivities

    private val _categories = MutableStateFlow<Map<String, List<Category>>>(emptyMap())
    val categories: StateFlow<Map<String, List<Category>>> get() = _categories

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    init {
        Log.d("HomeViewModel", "Inicializando HomeViewModel")
        loadActivities()  // 🚀 Llamar a loadActivities() al inicio
    }


    fun loadActivities() {
        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "Cargando actividades...")
                val activities = ApiServiceInstance.api.getActivities()
                Log.d("HomeViewModel", "Actividades obtenidas: ${activities.size}")

                val sortedActivities = activities.sortedByDescending { it.createdOn }
                _activities.value = sortedActivities
                _filteredActivities.value = sortedActivities

                val categoriesMap = mutableMapOf<String, List<Category>>()
                for (activity in sortedActivities) {
                    val categories = ApiServiceInstance.api.getCategoriesByActivity(activity.id ?: "")
                    categoriesMap[activity.id ?: ""] = categories
                }
                _categories.value = categoriesMap
                Log.d("HomeViewModel", "Categorías cargadas para actividades")
            } catch (e: Exception) {
                _errorMessage.value = "Error al cargar las actividades: ${e.message}"
                Log.e("HomeViewModel", "Error al cargar actividades", e)
            }
        }
    }
}

