package co.edu.unal.qnpa

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.viewmodels.LoginViewModel

@Composable
fun LoginScreen(
    navigateToHome: () -> Unit = {},
    navigateToRegister: () -> Unit = {},
    viewModel: LoginViewModel = viewModel(),
    sessionManager: SessionManager // Recibir el SessionManager
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val TAG = "LoginScreen"

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo de la aplicación
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Login Image",
                modifier = Modifier.size(230.dp)
            )

            // Título de bienvenida
            Text(
                text = "Bienvenido a Quien Pa",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Subtítulo
            Text(text = "Ingrese sus credenciales", fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar mensaje de error si existe
            errorMessage?.let {
                Text(text = it, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Campo para el correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Correo Electrónico") },
                modifier = Modifier.width(280.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // Campo para la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.width(280.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Botón para iniciar sesión
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Por favor, complete todos los campos."
                        Log.d(TAG, "Campos vacíos detectados.")
                    } else {
                        Log.d(TAG, "Iniciando proceso de login...")
                        viewModel.loginUser(
                            email = email,
                            password = password,
                            onSuccess = { userId ->
                                Log.d(TAG, "Login exitoso. Guardando userId en la sesión...")
                                // Guardar el userId y el email en la sesión
                                sessionManager.saveSession(userId, email)
                                Log.d(TAG, "Sesión guardada. Navegando a Home...")
                                // Navegar a la pantalla de inicio después del login
                                navigateToHome()
                            },
                            onError = { error ->
                                Log.e(TAG, "Error durante el login: $error")
                                // Mostrar el mensaje de error
                                errorMessage = error
                            }
                        )
                    }
                },
                modifier = Modifier.width(280.dp)
            ) {
                Text(text = "Iniciar sesión")
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Botón para ir a la pantalla de registro
            Button(
                onClick = {
                    Log.d(TAG, "Navegando a la pantalla de registro...")
                    navigateToRegister()
                },
                modifier = Modifier.width(280.dp)
            ) {
                Text(text = "Registrarse")
            }
            Spacer(modifier = Modifier.height(24.dp))

            // Enlace para recuperar contraseña
            TextButton(onClick = { /* Acción para recuperar contraseña */ }) {
                Text(text = "¿Olvidaste tu contraseña?")
            }
            Spacer(modifier = Modifier.height(14.dp))

            // Texto para iniciar sesión con redes sociales
            Text(text = "O iniciar sesión con:", fontSize = 15.sp)
            Spacer(modifier = Modifier.height(20.dp))

            // Botón de inicio de sesión con Google
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clickable { /* Acción para iniciar sesión con Google */ }
            )
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}