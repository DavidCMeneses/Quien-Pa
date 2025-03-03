package co.edu.unal.qnpa

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import co.edu.unal.qnpa.connections.User
import coil.compose.AsyncImage
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.viewmodels.OtherUserProfileViewModel

@Composable
fun OtherUserProfileScreen(
    userId: String, // Recibir el userId como parámetro
    viewModel: OtherUserProfileViewModel = viewModel(), // ViewModel específico para otros usuarios
    goBack: () -> Unit = {} // Función para retroceder
) {
    val user by viewModel.user.collectAsState()
    val rating by viewModel.rating.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Obtener los datos del usuario cuando se inicia la pantalla
    LaunchedEffect(userId) {
        viewModel.fetchUserData(userId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) { padding ->
        OtherUserProfileContent(
            paddingValues = padding,
            goBack = goBack,
            user = user,
            rating = rating,
            isLoading = isLoading,
            error = error
        )
    }
}

@Composable
fun OtherUserProfileContent(
    paddingValues: PaddingValues = PaddingValues(),
    goBack: () -> Unit = {},
    user: User?,
    rating: Double,
    isLoading: Boolean,
    error: String?
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
            // Barra superior con botón de retroceso
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
            }

            /* Mostrar mensaje de error si existe
            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }*/

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
                    !user?.imageUrl.isNullOrEmpty() -> {
                        // Mostrar la imagen del usuario
                        Log.d("OtherUserProfileScreen", "User Image URL: ${user?.imageUrl}")
                        AsyncImage(
                            model = user?.imageUrl,
                            contentDescription = "User Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Log.d("OtherUserProfileScreen", "User Image URL: ${user?.imageUrl}")
                        // Mostrar un ícono por defecto si no hay imagen
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Icon Profile",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
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

            // Descripción del usuario
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        Color.LightGray.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = user?.description ?: "No hay descripción disponible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (user?.description.isNullOrEmpty()) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
            }
        }
    }
}