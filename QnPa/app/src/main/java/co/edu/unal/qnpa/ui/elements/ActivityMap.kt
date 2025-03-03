package co.edu.unal.qnpa.ui.elements

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun ActivityMap(
    latitude: String,
    longitude: String,
    modifier: Modifier = Modifier
) {
    // Convertir las coordenadas de String a Double
    val lat = latitude.toDoubleOrNull() ?: 0.0
    val lng = longitude.toDoubleOrNull() ?: 0.0
    val activityLocation = LatLng(lat, lng)

    // Estado de la cámara para centrar el mapa en la ubicación de la actividad
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(activityLocation, 15f) // Zoom inicial
    }

    // Mostrar el mapa con un marcador en la ubicación de la actividad
    GoogleMap(
        modifier = modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = MarkerState(position = activityLocation),
            title = "Ubicación de la actividad",
            snippet = "Lat: ${activityLocation.latitude}, Lng: ${activityLocation.longitude}"
        )
    }
}