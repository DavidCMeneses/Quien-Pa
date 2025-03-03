package co.edu.unal.qnpa.ui.elements

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@SuppressLint("MissingPermission")
@Composable
fun SelectableMap(
    initialPosition: LatLng,
    onMarkerPositionChanged: (LatLng) -> Unit,
    modifier: Modifier = Modifier
) {
    var markerPosition by remember { mutableStateOf(initialPosition) }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialPosition, 15f) // Zoom inicial
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        onMapClick = { latLng ->
            markerPosition = latLng
            onMarkerPositionChanged(latLng)
        }
    ) {
        Marker(
            state = MarkerState(position = markerPosition),
            title = "Ubicación seleccionada",
            snippet = "Lat: ${markerPosition.latitude}, Lng: ${markerPosition.longitude}"
        )
    }
}

fun getUserLocation(context: Context, onLocationObtained: (LatLng) -> Unit) {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    onLocationObtained(latLng)
                }
            }
            .addOnFailureListener { exception ->
                // Manejar el error (opcional)
                println("Error al obtener la ubicación: ${exception.message}")
            }
    }
}