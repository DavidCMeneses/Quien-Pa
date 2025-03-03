package co.edu.unal.qnpa.ui.elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.edu.unal.qnpa.connections.Category
import com.google.android.gms.maps.model.LatLng
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Composable
fun ActivityInfoBox(
    activity: co.edu.unal.qnpa.connections.Activity,
    categories: List<Category>,
    userNames: Map<String, String>,
    userLocation: LatLng, // Ubicación del usuario como parámetro
    navigateToActivity: (String) -> Unit
) {
    val activityLocation = LatLng(activity.latitude.toDouble(), activity.longitude.toDouble())

    // Calcular distancia en metros usando la fórmula de Haversine
    val distanceInMeters = remember {
        calculateDistanceInMeters(userLocation, activityLocation)
    }

    // Suposición: Se tarda 1 minuto cada 80 metros caminando
    val estimatedTimeMinutes = (distanceInMeters / 80).toInt()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "¿Quién Pa' ${activity.name}?",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Creado por: ${userNames[activity.userId.toString()] ?: "Usuario desconocido"}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "Categorías: ${categories.joinToString(", ") { it.name }}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Nuevo texto: Tiempo estimado desde la ubicación actual
        Text(
            text = "Tiempo de llegada estimado: $estimatedTimeMinutes min",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { navigateToActivity(activity.id ?: "") }
            ) {
                Text(text = "Saber más")
            }
        }
    }
}

fun calculateDistanceInMeters(start: LatLng, end: LatLng): Double {
    val R = 6371e3 // Radio de la Tierra en metros
    val lat1 = Math.toRadians(start.latitude)
    val lat2 = Math.toRadians(end.latitude)
    val deltaLat = Math.toRadians(end.latitude - start.latitude)
    val deltaLon = Math.toRadians(end.longitude - start.longitude)

    val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
            cos(lat1) * cos(lat2) *
            sin(deltaLon / 2) * sin(deltaLon / 2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return R * c // Distancia en metros
}
