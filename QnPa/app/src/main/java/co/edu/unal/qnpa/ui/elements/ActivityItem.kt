package co.edu.unal.qnpa.ui.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import co.edu.unal.qnpa.connections.Activity
import co.edu.unal.qnpa.connections.Category
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import android.util.Log
import androidx.compose.foundation.clickable
import coil.compose.AsyncImage

@Composable
fun ActivityItem(
    activity: Activity,
    categories: List<Category>,
    onClick: (String) -> Unit // Ahora recibe el ID de la actividad
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(250.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
            .padding(8.dp)
            .clickable { onClick(activity.id ?: "") }, // Pasar el ID de la actividad
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Box superior que representa la imagen (70% del alto)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (!activity.imageUrl.isNullOrEmpty()) {
                Log.d("ActivityItem", "Cargando imagen desde: ${activity.imageUrl}")

                AsyncImage(
                    model = activity.imageUrl,
                    contentDescription = "Imagen de la actividad",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Log.d("ActivityItem", "No hay URL de imagen")
                Text(
                    text = "Sin imagen",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Texto del título de la actividad
        Text(
            text = "¿Quién Pa' ${activity.name}?",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Texto que enlista las categorías de la actividad
        Text(
            text = "Categorías: ${categories.joinToString(", ") { it.name }}",
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
            fontSize = 14.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}