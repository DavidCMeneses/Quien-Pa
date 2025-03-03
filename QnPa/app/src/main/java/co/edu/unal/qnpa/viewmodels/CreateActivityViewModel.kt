package co.edu.unal.qnpa.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.unal.qnpa.connections.Activity
import co.edu.unal.qnpa.connections.ApiServiceInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateActivityViewModel : ViewModel() {

    fun createActivity(
        name: String,
        description: String,
        date: String,
        latitude: String,
        longitude: String,
        place: String,
        userId: Int,
        createdOn: String,
        imageUrl: String?, // Nuevo parámetro para la URL de la imagen
        onSuccess: (activityId: String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Crear el objeto Activity
                val activity = Activity(
                    id = null,
                    name = name,
                    description = description,
                    latitude = latitude,
                    longitude = longitude,
                    userId = userId,
                    date = date,
                    place = place,
                    imageUrl = imageUrl, // Incluir la URL de la imagen
                    createdOn = createdOn
                )

                // Enviar la actividad al backend
                val response = ApiServiceInstance.api.createActivity(activity)

                // Llamar a onSuccess con el ID de la actividad creada
                onSuccess(response.id ?: throw IllegalStateException("ID de actividad no recibido"))
            } catch (e: Exception) {
                // Manejar errores
                onError(e.message ?: "Error desconocido al crear la actividad")
            }
        }
    }
}