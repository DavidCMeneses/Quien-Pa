package co.edu.unal.qnpa

import CategoriesViewModel
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.unal.qnpa.ui.theme.Pink80
import co.edu.unal.qnpa.ui.theme.Purple40
import co.edu.unal.qnpa.ui.theme.Purple80
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CategoriesScreen(
    viewModel: CategoriesViewModel = viewModel(),
    sessionManager: SessionManager, // Para obtener el userId
    navigateToHome: () -> Unit // Navegar a la siguiente pantalla después de guardar
) {
    // Obtener las categorías del ViewModel
    val categories by viewModel.categories.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()

    // Estado para manejar errores
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obtener el userId del SessionManager
    val userId = sessionManager.getUserId()

    // Obtener las categorías y las seleccionadas previamente al iniciar la pantalla
    LaunchedEffect(userId) {
        if (userId != null) {
            viewModel.fetchCategories()
            viewModel.fetchSelectedCategories(userId)
        } else {
            errorMessage = "Error: No se pudo obtener el ID del usuario."
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Encabezado
            Box(
                modifier = Modifier
                    .height(170.dp)
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Pink80.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Inicio Rápido", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Gustos", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Escojamos los temas que más te llaman la atención, desde los deportes hasta los videojuegos.",
                        fontSize = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Lista de categorías
            Box(
                modifier = Modifier
                    .height(400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.heightIn(max = 1000.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        val isSelected = selectedCategories.contains(category.id)

                        Button(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(), // Ocupar todo el ancho disponible
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isSelected) Purple80 else Purple40
                            ),
                            onClick = {
                                viewModel.toggleCategorySelection(category.id ?: "")
                            }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Calcular el tamaño del texto dinámicamente
                                val textSize = if (category.name.length > 20) 10.sp else 12.sp
                                Text(
                                    text = category.name,
                                    fontSize = textSize, // Tamaño dinámico
                                    maxLines = 1, // Limitar a una línea
                                    overflow = TextOverflow.Ellipsis, // Truncar con puntos suspensivos
                                    modifier = Modifier.weight(1f) // Ocupar el espacio disponible
                                )
                                Spacer(modifier = Modifier.width(5.dp))
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
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para guardar
            Button(onClick = {
                val userId = sessionManager.getUserId()
                if (userId != null) {
                    viewModel.saveSelectedCategories(userId)
                    navigateToHome() // Navegar a la siguiente pantalla
                } else {
                    errorMessage = "Error: No se pudo obtener el ID del usuario."
                }
            }) {
                Text(text = "Guardar")
            }

            // Mostrar mensaje de error si existe
            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }
        }
    }
}