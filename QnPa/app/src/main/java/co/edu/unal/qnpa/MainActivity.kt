package co.edu.unal.qnpa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import co.edu.unal.qnpa.core.navigation.NavigationWrapper
import co.edu.unal.qnpa.ui.theme.QnPaTheme
import com.google.firebase.FirebaseApp
import coil.ImageLoader
import coil.util.DebugLogger

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this) // Inicializar Firebase
        enableEdgeToEdge()

        // Inicializar el SessionManager
        val sessionManager = SessionManager(this)

        // Habilitar logs de Coil
        ImageLoader.Builder(this)
            .logger(DebugLogger())
            .build()

        // Determinar la pantalla inicial según el estado de la sesión
        val isLogged = sessionManager.isLoggedIn()

        setContent {
            QnPaTheme {
                NavigationWrapper(isLogged = isLogged)
            }
        }
    }
}
