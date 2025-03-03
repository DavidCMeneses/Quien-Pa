package co.edu.unal.qnpa.pages

import CategoriesViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.SessionManager
import co.edu.unal.qnpa.ui.elements.CustomMultilineHintTextField
import co.edu.unal.qnpa.ui.elements.SelectableMap
import co.edu.unal.qnpa.ui.elements.getUserLocation
import co.edu.unal.qnpa.ui.theme.Purple40
import co.edu.unal.qnpa.ui.theme.Purple80
import co.edu.unal.qnpa.uploadImageToFirebase
import co.edu.unal.qnpa.viewmodels.CreateActivityViewModel
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.model.LatLng
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun CreatePage(
    paddingValues: PaddingValues = PaddingValues(),
    sessionManager: SessionManager,
) {
    val viewModel: CreateActivityViewModel = viewModel()
    val categoriesViewModel: CategoriesViewModel = viewModel()
    val context = LocalContext.current

    // Estados del formulario
    var parcheName by remember { mutableStateOf("") }
    var parcheDescription by remember { mutableStateOf("") }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.NOON) }
    var selectedLatLng by remember { mutableStateOf<LatLng?>(null) }
    var placeName by remember { mutableStateOf("") }

    // Estados para la imagen
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Estados para la ubicación
    var userLocation by remember { mutableStateOf<LatLng?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(false) }

    // Estados para las categorías
    val categories by categoriesViewModel.categories.collectAsState()
    val selectedCategories by categoriesViewModel.selectedCategories.collectAsState()

    // Solicitar permisos de ubicación
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

    // Solicitar permisos al iniciar
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
        // Obtener las categorías al iniciar
        categoriesViewModel.fetchCategories()
    }

    // Formatear fecha y hora
    val formattedDate by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("MMM dd yyyy")
                .format(pickedDate)
        }
    }
    val formattedTime by remember {
        derivedStateOf {
            DateTimeFormatter
                .ofPattern("hh:mm")
                .format(pickedTime)
        }
    }

    // Diálogos para seleccionar fecha y hora
    val dateDialogState = rememberMaterialDialogState()
    val timeDialogState = rememberMaterialDialogState()

    // Lanzador para seleccionar una imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            uploadImageToFirebase(it,
                onSuccess = { url ->
                    imageUrl = url // Guardar la URL de la imagen
                },
                onError = { error ->
                    errorMessage = error
                }
            )
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
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        // Nombre del parche
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(20.dp)
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Columna para el texto "¿Quién Pa'" y el TextField
                Column(
                    modifier = Modifier
                        .weight(1f) // Ocupa el espacio restante
                        .padding(start = 32.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "¿Quién Pa'",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 26.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el texto y el TextField
                    TextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        value = parcheName,
                        onValueChange = {
                            parcheName = it
                        },
                        label = { Text(text = "Parche") },
                        singleLine = true
                    )
                }

                // Texto "?" a la derecha
                Text(
                    text = "?",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 26.sp,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    modifier = Modifier
                        .padding(end = 32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Descripción del parche
            CustomMultilineHintTextField(
                value = parcheDescription,
                onValueChange = {
                    parcheDescription = it
                },
                hint = "Descripción del parche\n Ahora en multiples \n Dimensiones",
                modifier = Modifier
                    .width(350.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(horizontal = 36.dp, vertical = 36.dp)
            )
            Spacer(modifier = Modifier.height(18.dp))
            Divider(
                modifier = Modifier
                    .width(350.dp)
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Elegir Fecha y Hora",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    dateDialogState.show()
                },
                modifier = Modifier
                    .width(350.dp)
                    .height(50.dp),
            ) {
                Text(text = "Elegir fecha")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = formattedDate)
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    timeDialogState.show()
                },
                modifier = Modifier
                    .width(350.dp)
                    .height(50.dp),
            ) {
                Text(text = "Elegir hora")
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = formattedTime)
            Spacer(modifier = Modifier.height(18.dp))
            Divider(
                modifier = Modifier
                    .width(350.dp)
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Elegir ubicación",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
            )

            // Mostrar el mapa con la ubicación del usuario
            if (userLocation != null) {
                SelectableMap(
                    initialPosition = userLocation!!,
                    onMarkerPositionChanged = { latLng ->
                        selectedLatLng = latLng
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            } else {
                Text(
                    text = "Obteniendo ubicación...",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                )
            }

            // Mostrar la latitud y longitud debajo del mapa
            if (selectedLatLng == null) {
                Text(
                    text = "Selecciona un punto en el mapa",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 36.dp),
                value = placeName,
                onValueChange = {
                    placeName = it
                },
                label = { Text(text = "Nombre del lugar") }
            )
            Spacer(modifier = Modifier.height(18.dp))
            Divider(
                modifier = Modifier
                    .width(350.dp)
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(18.dp))

            // Botón para seleccionar una imagen
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
            ) {
                Text(text = "Agregar imagen de la actividad")
            }

            // Mostrar la imagen seleccionada (opcional)
            imageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Divider(
                modifier = Modifier
                    .width(350.dp)
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(18.dp))
            // Sección de categorías
            Text(
                text = "Seleccionar categorías",
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 300.dp) // Limitar la altura máxima
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    val isSelected = selectedCategories.contains(category.id)

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (!isSelected) Purple80 else Purple40
                        ),
                        onClick = {
                            categoriesViewModel.toggleCategorySelection(category.id ?: "")
                        }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.name,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                                contentDescription = if (isSelected) "Selected" else "Add",
                                tint = if (isSelected) Color.Green else Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            Divider(
                modifier = Modifier
                    .width(350.dp)
                    .height(2.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(18.dp))
            Button(
                onClick = {
                    // Validar los datos del formulario
                    if (parcheName.isBlank() || parcheDescription.isBlank() || placeName.isBlank() || selectedLatLng == null) {
                        Toast.makeText(context, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        // Combinar fecha y hora del evento
                        val dateTime = LocalDateTime.of(pickedDate, pickedTime)
                        val formattedDateTime = dateTime.atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

                        // Obtener la fecha y hora actual en formato ISO
                        val createdOn = LocalDateTime.now()
                            .atOffset(ZoneOffset.UTC)
                            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))

                        // Obtener el ID del usuario autenticado
                        val userId = sessionManager.getUserId()

                        if (userId != null) {
                            // Convertir userId a Int
                            val userIdInt = userId.toIntOrNull()
                            if (userIdInt != null) {
                                // Crear la actividad
                                viewModel.createActivity(
                                    name = parcheName,
                                    description = parcheDescription,
                                    date = formattedDateTime,
                                    latitude = selectedLatLng!!.latitude.toString(),
                                    longitude = selectedLatLng!!.longitude.toString(),
                                    place = placeName,
                                    userId = userIdInt,
                                    createdOn = createdOn,
                                    imageUrl = imageUrl, // Enviar la URL de la imagen
                                    onSuccess = { activityId ->
                                        // Asignar las categorías seleccionadas
                                        categoriesViewModel.saveSelectedCategoriesForActivity(activityId) {
                                            // Notificar éxito
                                            Toast.makeText(context, "Parche creado exitosamente", Toast.LENGTH_SHORT).show()

                                            // Reiniciar el formulario
                                            parcheName = ""
                                            parcheDescription = ""
                                            pickedDate = LocalDate.now()
                                            pickedTime = LocalTime.NOON
                                            selectedLatLng = null
                                            placeName = ""
                                            imageUri = null
                                            imageUrl = null
                                            categoriesViewModel.clearSelectedCategories()
                                        }
                                    },
                                    onError = { error ->
                                        // Mostrar un mensaje de error
                                        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            } else {
                                Toast.makeText(context, "Error: ID de usuario no válido", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
            ) {
                Text(text = "Crear parche")
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Diálogos para seleccionar fecha y hora
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton("Aceptar")
            negativeButton("Cancelar")
        }
    ) {
        this.datepicker(
            initialDate = LocalDate.now(),
            title = "Elegir fecha",
            allowedDateValidator = {
                it.isEqual(LocalDate.now()) || it.isAfter(LocalDate.now())
            }
        ) {
            pickedDate = it
        }
    }

    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton("Aceptar")
            negativeButton("Cancelar")
        }
    ) {
        this.timepicker(
            initialTime = LocalTime.NOON,
            title = "Elegir hora",
            timeRange = LocalTime.MIN..LocalTime.MAX
        ) {
            pickedTime = it
        }
    }
}