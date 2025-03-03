package co.edu.unal.qnpa.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.unal.qnpa.viewmodels.HomeScreenViewModel
import coil.compose.AsyncImage
import com.vanpra.composematerialdialogs.MaterialDialog

@Composable
fun DrawerContent(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel, // Recibir el ViewModel
    onProfileClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onCategoriesClick: () -> Unit = {},
    onProfileEditClick: () -> Unit = {}
) {
    // Obtener la URL de la imagen del usuario desde el ViewModel
    val userImageUrl by viewModel.userImageUrl.collectAsState()

    Surface(
        modifier = Modifier
            .width(200.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Título del Drawer
            Text(
                text = "Quien Pa",
                fontSize = 24.sp,
                modifier = Modifier.padding(16.dp)
            )

            HorizontalDivider() // Línea divisoria

            Spacer(modifier = Modifier.height(4.dp))

            // Ítem de Perfil
            NavigationDrawerItem(
                icon = {
                    // Mostrar la imagen del usuario si está disponible, de lo contrario, mostrar un ícono por defecto
                    if (!userImageUrl.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(27.dp)
                                .clip(CircleShape) // Recortar la imagen en forma circular
                        ) {
                            AsyncImage(
                                model = userImageUrl,
                                contentDescription = "Account Icon",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop // Ajustar y recortar la imagen
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.AccountCircle,
                            contentDescription = "Account Icon",
                            modifier = Modifier
                                .size(27.dp)
                        )
                    }
                },
                label = {
                    Text(text = "Perfil")
                },
                selected = false,
                onClick = {
                    onProfileClick() // Navegar a la pantalla de perfil
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Ítem de Editar Perfil
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Create,
                        contentDescription = "Edit Icon",
                        modifier = Modifier.size(27.dp)
                    )
                },
                label = {
                    Text(text = "Editar Perfil")
                },
                selected = false,
                onClick = {
                    onProfileEditClick()
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Ítem de Modificar Gustos
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = "Categories Icon",
                        modifier = Modifier.size(27.dp)
                    )
                },
                label = {
                    Text(text = "Modificar gustos")
                },
                selected = false,
                onClick = {
                    onCategoriesClick() // Navegar a la pantalla de categorías
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Ítem de Configuración
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = "Config Icon",
                        modifier = Modifier.size(27.dp)
                    )
                },
                label = {
                    Text(text = "Configuración")
                },
                selected = false,
                onClick = {
                    // Lógica para la configuración
                }
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Ítem de Cerrar Sesión
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.ExitToApp,
                        contentDescription = "Exit Icon",
                        modifier = Modifier.size(27.dp)
                    )
                },
                label = {
                    Text(text = "Cerrar Sesión")
                },
                selected = false,
                onClick = {
                    onLogoutClick() // Ejecutar la función de logout
                }
            )
        }
    }
}