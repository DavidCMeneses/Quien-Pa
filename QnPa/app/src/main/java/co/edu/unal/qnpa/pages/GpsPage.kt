package co.edu.unal.qnpa.pages

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.connections.ApiServiceInstance
import co.edu.unal.qnpa.connections.Category
import co.edu.unal.qnpa.ui.elements.ActivityInfoBox
import co.edu.unal.qnpa.ui.elements.formatDateTime
import co.edu.unal.qnpa.ui.elements.getUserLocation
import co.edu.unal.qnpa.viewmodels.GpsViewModel
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun GpsPage(
    paddingValues: PaddingValues = PaddingValues(),
    navigateToActivity: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: GpsViewModel = viewModel()
    val activities by viewModel.activities.collectAsState()
    val userNames by viewModel.userNames.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Estados para la ubicación
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }

    // Estado para la actividad seleccionada
    var selectedActivity by remember { mutableStateOf<co.edu.unal.qnpa.connections.Activity?>(null) }

    // Estado para las categorías de la actividad seleccionada
    var selectedActivityCategories by remember { mutableStateOf<List<Category>>(emptyList()) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            locationPermissionGranted = isGranted
            if (isGranted) {
                getUserLocation(context) { latLng ->
                    userLocation = latLng
                }
            }
        }
    )

    LaunchedEffect(Unit) {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        val hasPermission = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
        locationPermissionGranted = hasPermission
        if (!hasPermission) {
            locationPermissionLauncher.launch(permission)
        } else {
            getUserLocation(context) { latLng ->
                userLocation = latLng
            }
        }

        viewModel.loadActivities()
    }

    LaunchedEffect(selectedActivity) {
        selectedActivity?.let { activity ->
            val categories = ApiServiceInstance.api.getCategoriesByActivity(activity.id ?: "")
            selectedActivityCategories = categories
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding() + 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.LightGray.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                if (selectedActivity != null && userLocation != null) {
                    ActivityInfoBox(
                        activity = selectedActivity!!,
                        categories = selectedActivityCategories,
                        userNames = userNames,
                        userLocation = userLocation!!,
                        navigateToActivity = navigateToActivity
                    )
                } else {
                    Text(
                        text = "Elige un parche",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            errorMessage?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            if (userLocation != null) {
                MyGoogleMaps(
                    userLocation = userLocation!!,
                    activities = activities,
                    selectedActivity = selectedActivity, // ✅ Pasamos la actividad seleccionada
                    onMarkerClick = { activity ->
                        selectedActivity = activity
                    }
                )
            } else {
                Text(
                    text = "Obteniendo ubicación...",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                )
            }
        }
    }
}

@Composable
fun MyGoogleMaps(
    userLocation: LatLng,
    activities: List<co.edu.unal.qnpa.connections.Activity>,
    selectedActivity: co.edu.unal.qnpa.connections.Activity?, // ✅ Agregamos el parámetro para marcar el seleccionado
    onMarkerClick: (co.edu.unal.qnpa.connections.Activity) -> Unit
) {
    val context = LocalContext.current
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 15f)
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        // Marcador para la ubicación del usuario (Siempre azul)
        Marker(
            state = MarkerState(position = userLocation),
            title = "Tu ubicación",
            snippet = "Lat: ${userLocation.latitude}, Lng: ${userLocation.longitude}",
            icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
        )

        // Marcadores de actividades
        activities.forEach { activity ->
            val activityLocation = LatLng(activity.latitude.toDouble(), activity.longitude.toDouble())

            val markerColor = if (activity == selectedActivity) {
                BitmapDescriptorFactory.HUE_RED // 🔴 Marcador seleccionado
            } else {
                BitmapDescriptorFactory.HUE_GREEN // 🟢 Marcadores normales
            }

            Marker(
                state = MarkerState(position = activityLocation),
                title = activity.name,
                icon = BitmapDescriptorFactory.defaultMarker(markerColor), // ✅ Cambia el color según el seleccionado
                onClick = {
                    onMarkerClick(activity)
                    true
                }
            )
        }
    }
}

