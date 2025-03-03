package co.edu.unal.qnpa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.connections.Activity
import co.edu.unal.qnpa.connections.Category
import co.edu.unal.qnpa.ui.elements.ActivityMap
import co.edu.unal.qnpa.ui.elements.formatDateTime
import co.edu.unal.qnpa.viewmodels.ActivityViewModel
import coil.compose.AsyncImage

@Composable
fun ActivityScreen(
    activityId: String,
    onBackClick: () -> Unit,
    navigateToOtherUserProfile: (String) -> Unit,
    viewModel: ActivityViewModel = viewModel()
) {
    val activity by viewModel.activity.collectAsState()
    val categories by viewModel.categories.collectAsState()
    val creatorName by viewModel.creatorName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(activityId) {
        viewModel.loadActivityDetails(activityId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) { padding ->
        ActivityContent(
            paddingValues = padding,
            onBackClick = onBackClick,
            activity = activity,
            categories = categories,
            creatorName = creatorName,
            isLoading = isLoading,
            error = error,
            navigateToOtherUserProfile = navigateToOtherUserProfile
        )
    }
}

@Composable
fun ActivityContent(
    paddingValues: PaddingValues = PaddingValues(),
    onBackClick: () -> Unit = {},
    activity: Activity?,
    categories: List<Category>,
    creatorName: String?,
    isLoading: Boolean,
    error: String?,
    navigateToOtherUserProfile: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding(), bottom = paddingValues.calculateBottomPadding())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
                        .clickable { onBackClick() }
                )
            }

            error?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            activity?.let { activity ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    if (!activity.imageUrl.isNullOrEmpty()) {
                        AsyncImage(
                            model = activity.imageUrl,
                            contentDescription = "Imagen de la actividad",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop,
                        )
                    } else {
                        Text(text = "Sin imagen", color = Color.White, fontSize = 16.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "¿Quién Pa' ${activity.name}?", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Creado por: ${creatorName ?: "Usuario desconocido"}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp).clickable {
                        activity.userId?.let { navigateToOtherUserProfile(it.toString()) }
                    },
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = activity.description ?: "Sin descripción", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Fecha y hora: ${activity.date?.let { formatDateTime(it) } ?: "No disponible"}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Categorías: ${categories.joinToString(", ") { it.name }}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))
                Spacer(modifier = Modifier.height(16.dp))
                ActivityMap(latitude = (activity.latitude ?: 0.0).toString(), longitude = (activity.longitude ?: 0.0).toString(), modifier = Modifier.fillMaxWidth().height(300.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Ubicación: ${activity.place ?: "No especificado"}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp))
            }

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }
}

