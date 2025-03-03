package co.edu.unal.qnpa

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.ui.elements.ActivityItem
import co.edu.unal.qnpa.viewmodels.HomeViewModel
import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    goBack: () -> Unit = {},
    navigateToActivityDetails: (String) -> Unit = {}
) {
    val viewModel: HomeViewModel = viewModel()
    var searchText by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }

    // Lista de actividades disponibles
    val activities by viewModel.activities.collectAsState()

    // 📌 Log para verificar actividades recibidas
    Log.d("SearchScreen", "Actividades recibidas (${activities.size}): ${activities.map { it.name }}")

    // Filtrar actividades con Jaro-Winkler + Coincidencia de Subcadenas
    val filteredActivities = remember(searchText, activities) {
        if (searchText.isEmpty()) {
            activities
        } else {
            activities.map { it to jaroWinklerSimilarity(it.name.lowercase(), searchText.lowercase()) }
                .filter { (activity, similarity) ->
                    similarity > 0.3 || activity.name.lowercase().contains(searchText.lowercase())
                }
                .sortedByDescending { it.second }
                .map { it.first }
        }
    }

    Log.d("SearchScreen", "Texto de búsqueda: $searchText")
    Log.d("SearchScreen", "Actividades filtradas: ${filteredActivities.map { it.name }}")

    Scaffold(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SearchBar(
                query = searchText,
                onQueryChange = {
                    searchText = it
                    Log.d("SearchScreen", "Texto de búsqueda actualizado: $searchText")
                },
                onSearch = {
                    active = false
                    Log.d("SearchScreen", "Búsqueda realizada con: $searchText")
                },
                active = active,
                onActiveChange = {
                    active = it
                    Log.d("SearchScreen", "Estado activo cambiado: $active")
                },
                placeholder = { Text(text = "Buscar parche...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Volver",
                        modifier = Modifier
                            .padding(start = 16.dp, end = 8.dp)
                            .size(27.dp)
                            .clickable { goBack() }
                    )
                },
                trailingIcon = {
                    if (active) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Borrar búsqueda",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(27.dp)
                                .clickable {
                                    if (searchText.isNotEmpty()) {
                                        searchText = ""
                                    } else {
                                        active = false
                                    }
                                }
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = "Opciones",
                            modifier = Modifier
                                .padding(start = 8.dp, end = 16.dp)
                                .size(27.dp)
                                .clickable { /* Acción */ }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(top = 16.dp),
                ) {
                    items(filteredActivities) { activity ->
                        ActivityItem(
                            activity = activity,
                            categories = viewModel.categories.value[activity.id] ?: emptyList(),
                            onClick = { navigateToActivityDetails(it) }
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(top = 16.dp),
            ) {
                items(filteredActivities) { activity ->
                    ActivityItem(
                        activity = activity,
                        categories = viewModel.categories.value[activity.id] ?: emptyList(),
                        onClick = { navigateToActivityDetails(it) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (filteredActivities.isEmpty()) {
                Text(
                    text = "No se encontraron resultados",
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
        }
    }
}

/**
 * Implementación de Jaro-Winkler Similarity para mejorar la búsqueda
 */
fun jaroWinklerSimilarity(s1: String, s2: String): Double {
    val jaro = jaroSimilarity(s1, s2)
    val prefixLength = min(commonPrefixLength(s1, s2), 4) // Máximo 4 letras de prefijo cuentan
    return jaro + (prefixLength * 0.1 * (1 - jaro))
}

fun jaroSimilarity(s1: String, s2: String): Double {
    if (s1 == s2) return 1.0
    if (s1.isEmpty() || s2.isEmpty()) return 0.0

    val matchDistance = (max(s1.length, s2.length) / 2) - 1
    val s1Matches = BooleanArray(s1.length)
    val s2Matches = BooleanArray(s2.length)

    var matches = 0
    var transpositions = 0

    for (i in s1.indices) {
        val start = max(0, i - matchDistance)
        val end = min(i + matchDistance + 1, s2.length)
        for (j in start until end) {
            if (s2Matches[j]) continue
            if (s1[i] != s2[j]) continue
            s1Matches[i] = true
            s2Matches[j] = true
            matches++
            break
        }
    }

    if (matches == 0) return 0.0

    var k = 0
    for (i in s1.indices) {
        if (!s1Matches[i]) continue
        while (!s2Matches[k]) k++
        if (s1[i] != s2[k]) transpositions++
        k++
    }

    return ((matches / s1.length.toDouble()) +
            (matches / s2.length.toDouble()) +
            ((matches - transpositions / 2.0) / matches)) / 3.0
}

fun commonPrefixLength(s1: String, s2: String): Int {
    val maxLength = min(s1.length, s2.length)
    for (i in 0 until maxLength) {
        if (s1[i] != s2[i]) return i
    }
    return maxLength
}
