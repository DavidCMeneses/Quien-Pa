package co.edu.unal.qnpa.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.ui.elements.ActivityItem
import co.edu.unal.qnpa.viewmodels.HomeViewModel

@Composable
fun HomePage(
    paddingValues: PaddingValues = PaddingValues(),
    navigateToActivityDetails: (String) -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel()
    val activities by viewModel.activities.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Cargar actividades al iniciar
    LaunchedEffect(Unit) {
        viewModel.loadActivities()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (errorMessage != null) {
            // Mostrar mensaje de error si hay algún problema
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(top = paddingValues.calculateTopPadding() + 16.dp),
            ) {
                items(activities) { activity ->
                    ActivityItem(
                        activity = activity,
                        categories = categories[activity.id] ?: emptyList(),
                        onClick = {
                            // Navegar a la pantalla de detalles de la actividad
                            navigateToActivityDetails(it)
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}