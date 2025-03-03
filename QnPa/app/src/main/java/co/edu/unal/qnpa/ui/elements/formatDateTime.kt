package co.edu.unal.qnpa.ui.elements

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Función para formatear la fecha y hora
fun formatDateTime(dateTimeString: String): String {
    return try {
        // Parsear la fecha y hora en formato ISO 8601
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(dateTimeString)

        // Formatear la fecha en "MMM/dd/aaaa"
        val dateFormat = SimpleDateFormat("MMM/dd/yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)

        // Formatear la hora en "hh:mm a" (a.m. o p.m.)
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(date)

        "$formattedDate $formattedTime" // Combinar fecha y hora
    } catch (e: Exception) {
        "Fecha no disponible" // En caso de error
    }
}