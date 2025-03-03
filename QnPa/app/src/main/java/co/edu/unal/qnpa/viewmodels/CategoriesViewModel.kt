import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.ApiServiceInstance
import co.edu.unal.qnpa.connections.AssignCategoryToActivityRequest
import co.edu.unal.qnpa.connections.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    // Estado para las categorías
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> get() = _categories

    // Estado para las categorías seleccionadas
    private val _selectedCategories = MutableStateFlow<Set<String>>(emptySet())
    val selectedCategories: StateFlow<Set<String>> get() = _selectedCategories

    // Estado para manejar errores
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    // Función para obtener las categorías desde la API
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val response = ApiServiceInstance.api.getCategories()
                _categories.value = response
            } catch (e: Exception) {
                // Manejar errores (por ejemplo, mostrar un mensaje al usuario)
                e.printStackTrace()
            }
        }
    }

    // Función para obtener las categorías seleccionadas previamente por el usuario
    fun fetchSelectedCategories(userId: String) {
        viewModelScope.launch {
            try {
                val response = ApiServiceInstance.api.getCategoriesByUser(userId)
                _selectedCategories.value = response.map { it.id ?: "" }.toSet()
            } catch (e: Exception) {
                // Manejar errores
                e.printStackTrace()
            }
        }
    }

    // Función para manejar la selección de categorías
    fun toggleCategorySelection(categoryId: String) {
        _selectedCategories.value = if (_selectedCategories.value.contains(categoryId)) {
            _selectedCategories.value - categoryId
        } else {
            _selectedCategories.value + categoryId
        }
    }

    // Función para guardar las categorías seleccionadas para una actividad
    fun saveSelectedCategoriesForActivity(activityId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _selectedCategories.value.forEach { categoryId ->
                    // Crear el objeto de solicitud
                    val request = AssignCategoryToActivityRequest(activityId = activityId)

                    // Llamar al nuevo endpoint
                    ApiServiceInstance.api.assignCategoryToActivity(
                        categoryId = categoryId,
                        request = request
                    )
                }
                // Notificar éxito
                onSuccess()
            } catch (e: Exception) {
                // Manejar errores
                _errorMessage.value = "Error al asignar categorías: ${e.message}"
            }
        }
    }

    // Limpiar las categorías seleccionadas
    fun clearSelectedCategories() {
        _selectedCategories.value = emptySet()
    }

    // Función para guardar las categorías seleccionadas en la API
    fun saveSelectedCategories(userId: String) {
        viewModelScope.launch {
            try {
                _selectedCategories.value.forEach { categoryId ->
                    ApiServiceInstance.api.assignUserToCategory(categoryId, userId)
                }
            } catch (e: Exception) {
                // Manejar errores
                e.printStackTrace()
            }
        }
    }
}