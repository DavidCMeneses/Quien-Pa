package co.edu.unal.qnpa.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.unal.qnpa.viewmodels.HomeScreenViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onOpenDrawer: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: HomeScreenViewModel // Recibir el ViewModel para acceder a la imagen del usuario
) {
    // Obtener la URL de la imagen del usuario desde el ViewModel
    val userImageUrl by viewModel.userImageUrl.collectAsState()

    TopAppBar(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(100.dp)),
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        windowInsets = WindowInsets(top = 0.dp),
        title = {
            Text(
                text = "Buscar parche...",
                fontSize = 15.sp,
                modifier = Modifier
                    .clickable {
                        onSearchClick()
                    }
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Rounded.Menu,
                contentDescription = "Icon",
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(27.dp)
                    .clickable { onOpenDrawer() }
            )
        },
        actions = {
            // Ícono de notificaciones
            Icon(
                imageVector = Icons.Rounded.Notifications,
                contentDescription = "Icon",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(30.dp)
            )

            // Ícono de perfil o imagen del usuario
            Box(
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(27.dp)
                    .clip(CircleShape) // Recortar en forma circular
                    .clickable {
                        onProfileClick()
                    }
            ) {
                if (!userImageUrl.isNullOrEmpty()) {
                    // Mostrar la imagen del usuario si está disponible
                    AsyncImage(
                        model = userImageUrl,
                        contentDescription = "Account Icon",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // Ajustar y recortar la imagen
                    )
                } else {
                    // Mostrar un ícono por defecto si no hay imagen
                    Icon(
                        imageVector = Icons.Rounded.AccountCircle,
                        contentDescription = "Account Icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    )
}