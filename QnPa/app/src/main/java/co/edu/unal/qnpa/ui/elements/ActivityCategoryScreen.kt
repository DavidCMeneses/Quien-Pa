package co.edu.unal.qnpa.ui.elements

import CategoriesViewModel
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
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
fun ActivityCategoriesSection(
    activityId: String, // ID de la actividad creada
    viewModel: CategoriesViewModel = viewModel()
) {
    // Obtener las categorías del ViewModel
    val categories by viewModel.categories.collectAsState()
    val selectedCategories by viewModel.selectedCategories.collectAsState()

    // Estado para manejar errores
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Obtener las categorías al iniciar la sección
    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Encabezado
        Text(
            text = "Categorías de la Actividad",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Selecciona las categorías que mejor describan tu actividad.",
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Lista de categorías
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
                        viewModel.toggleCategorySelection(category.id ?: "")
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

        // Botón para guardar las categorías seleccionadas
        Button(
            onClick = {
                viewModel.saveSelectedCategoriesForActivity(activityId) {
                    // Notificar éxito
                    errorMessage = "Categorías guardadas correctamente"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Guardar categorías")
        }

        // Mostrar mensaje de error o éxito
        errorMessage?.let {
            Text(
                text = it,
                color = if (it.contains("Error")) Color.Red else Color.Green,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}