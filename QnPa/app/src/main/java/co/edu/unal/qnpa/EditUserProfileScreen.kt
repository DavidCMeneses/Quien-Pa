package co.edu.unal.qnpa

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import co.edu.unal.qnpa.connections.User
import co.edu.unal.qnpa.viewmodels.UserProfileViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.ui.elements.CustomMultilineHintTextField

@Composable
fun EditUserProfileScreen(
    sessionManager: SessionManager,
    viewModel: UserProfileViewModel = viewModel(),
    goBack: () -> Unit = {}
) {
    val userId = sessionManager.getUserId()
    val user by viewModel.user.collectAsState()
    val rating by viewModel.rating.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    // Inicializar la descripción y la imagen con los valores actuales del usuario
    LaunchedEffect(user) {
        user?.let {
            description = it.description ?: ""
            imageUrl = it.imageUrl ?: ""
        }
    }

    // Lanzador para seleccionar una imagen
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            uploadImageToFirebase(
                uri = it,
                onSuccess = { downloadUrl ->
                    imageUrl = downloadUrl // Actualizar la URL de la imagen
                },
                onError = { error ->
                    // Manejar el error (puedes mostrar un Snackbar o un Toast)
                    println("Error al subir la imagen: $error")
                }
            )
        }
    }

    // Obtener los datos del usuario cuando se inicia la pantalla
    LaunchedEffect(userId) {
        userId?.let { viewModel.fetchUserData(it) }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) { padding ->
        EditUserProfileContent(
            paddingValues = padding,
            goBack = goBack,
            user = user,
            rating = rating,
            isLoading = isLoading,
            error = error,
            description = description,
            onDescriptionChange = { description = it },
            imageUrl = imageUrl,
            onImageChange = { newImageUrl ->
                imageUrl = newImageUrl
            },
            onSave = {
                val updatedUser = user?.copy(description = description, imageUrl = imageUrl)
                updatedUser?.let { viewModel.updateUser(it) }
                goBack()
            },
            onEditImageClick = {
                launcher.launch("image/*")
            }
        )
    }
}

@Composable
fun EditUserProfileContent(
    paddingValues: PaddingValues = PaddingValues(),
    goBack: () -> Unit = {},
    user: User?,
    rating: Double,
    isLoading: Boolean,
    error: String?,
    description: String,
    onDescriptionChange: (String) -> Unit,
    imageUrl: String,
    onImageChange: (String) -> Unit,
    onSave: () -> Unit,
    onEditImageClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /* Mostrar mensaje de error si existe
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }*/

            // Barra superior con botones de retroceso y guardar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 16.dp, bottom = 5.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Icon Back",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 8.dp)
                        .size(27.dp)
                        .clickable { goBack() }
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Icon Save",
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .size(24.dp)
                        .clickable { onSave() }
                )
            }

            // Mostrar la imagen del usuario
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.inversePrimary)
            ) {
                when {
                    isLoading -> {
                        // Mostrar un indicador de carga
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    !imageUrl.isNullOrEmpty() -> {
                        // Mostrar la imagen del usuario
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "User Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        // Mostrar un ícono por defecto si no hay imagen
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Icon Profile",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .align(Alignment.Center)
                )
                // Botón para editar la imagen
                Icon(
                    imageVector = Icons.Rounded.Edit,
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = "Edit Image",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(32.dp)
                        .clickable { onEditImageClick() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nombre del usuario
            Text(
                text = user?.name ?: "Usuario",
                style = MaterialTheme.typography.titleLarge
            )

            // Edad del usuario
            Text(
                text = "${user?.age ?: "-"} años",
                style = MaterialTheme.typography.bodyMedium
            )

            // Calificación del usuario
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Icon Star"
                    )
                    Text(
                        text = "$rating",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de texto para editar la descripción
            CustomMultilineHintTextField(
                value = description,
                onValueChange = onDescriptionChange,
                hint = "Escribe una descripción...",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.inversePrimary)
                    .padding(16.dp)
            )
        }
    }
}