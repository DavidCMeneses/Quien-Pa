package co.edu.unal.qnpa

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.unal.qnpa.viewmodels.RegisterViewModel
import coil.compose.rememberImagePainter
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit = {},
    navigateToCategories: () -> Unit = {},
    viewModel: RegisterViewModel = viewModel(),
    sessionManager: SessionManager // Recibir el SessionManager
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) } // URI de la imagen seleccionada
    var imageUrl by remember { mutableStateOf<String?>(null) } // URL de la imagen subida
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Lanzador para seleccionar una imagen
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            uploadImageToFirebase(it,
                onSuccess = { url ->
                    imageUrl = url // Guardar la URL de la imagen
                },
                onError = { error ->
                    errorMessage = error
                }
            )
        }
    }

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
            Text(
                text = "¿Quieres hacer parte de Quien Pa?",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Registrate", fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Mostrar mensaje de error si existe
            errorMessage?.let {
                Text(text = it, color = Color.Red)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Campo para seleccionar una imagen
            Button(onClick = { launcher.launch("image/*") }) {
                Text(text = "Seleccionar Foto de Perfil")
            }
            Spacer(modifier = Modifier.height(4.dp))
            // Mostrar la imagen seleccionada (opcional)
            imageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(uri),
                    contentDescription = "Imagen seleccionada",
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Campo para el nombre de usuario
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text(text = "Nombre de Usuario") }
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Campo para el correo electrónico
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = "Correo Electrónico") }
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Campo para la edad
            OutlinedTextField(
                value = age,
                onValueChange = { age = it },
                label = { Text(text = "Edad") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number // Teclado numérico
                )
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Campo para la contraseña
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = "Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Campo para validar la contraseña
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(text = "Validar Contraseña") },
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Botón para completar el registro
            Button(onClick = {
                // Validar el formato de la contraseña
                if (!isValidPassword(password)) {
                    errorMessage = "La contraseña debe contener al menos una letra mayúscula, un número y un carácter especial."
                    return@Button
                }

                // Validar que las contraseñas coincidan
                if (password != confirmPassword) {
                    errorMessage = "Las contraseñas no coinciden"
                    return@Button
                }

                // Validar que la edad sea un número válido
                val ageInt = age.toIntOrNull()
                if (ageInt == null || ageInt <= 0) {
                    errorMessage = "Por favor, ingresa una edad válida."
                    return@Button
                }

                // Registrar al usuario
                viewModel.registerUser(
                    username = username,
                    email = email,
                    password = password,
                    age = ageInt,
                    imageUrl = imageUrl,
                    onSuccess = { userId, email ->
                        // Guardar la sesión del usuario
                        sessionManager.saveSession(userId, email)
                        // Navegar a la pantalla de categorías
                        navigateToCategories()
                    },
                    onError = { error ->
                        errorMessage = error
                    }
                )
            }) {
                Text(text = "Continuar")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// Función para validar la contraseña
fun isValidPassword(password: String): Boolean {
    val passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&#^()\\-_=+\\[\\]{};:'\",.<>/])[A-Za-z\\d@\$!%*?&#^()\\-_=+\\[\\]{};:'\",.<>/]{8,}\$".toRegex()
    return passwordRegex.matches(password)
}

// Función para subir la imagen a Firebase
fun uploadImageToFirebase(
    uri: Uri,
    onSuccess: (String) -> Unit, // Callback para la URL de descarga
    onError: (String) -> Unit // Callback para manejar errores
) {
    // Obtener una referencia a Firebase Storage
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference

    // Crear un nombre único para la imagen
    val imageName = "images/${UUID.randomUUID()}.jpg"
    val imageRef: StorageReference = storageRef.child(imageName)

    // Subir la imagen a Firebase Storage
    imageRef.putFile(uri)
        .addOnSuccessListener {
            // Si la subida es exitosa, obtener la URL de descarga
            imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                onSuccess(downloadUri.toString()) // Devolver la URL de descarga
            }.addOnFailureListener { e ->
                onError("Error al obtener la URL de descarga: ${e.message}")
            }
        }
        .addOnFailureListener { e ->
            onError("Error al subir la imagen: ${e.message}")
        }
}
/*
username
password
rate
profile_pic
description
 */