package co.edu.unal.qnpa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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

@Preview
@Composable
fun RegisterScreen(
    navigateToLogin: () -> Unit = {},
    navigateToCategories: () -> Unit = {}
){
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        //.padding(28.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "¿Quieres hacer parte de Quien Pa?", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Registrate", fontSize = 15.sp)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Nombre de Usuario") }
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Contraseña") }
            )
            Spacer(modifier = Modifier.height(4.dp))
            OutlinedTextField(
                value = "",
                onValueChange = {},
                label = { Text(text = "Validar Contraseña") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navigateToLogin()
            }){
                Text(text = "Completar Registro")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navigateToCategories()
            }){
                Text(text = "Categorias")
            }
        }
    }
}
/*
username
password
rate
profile_pic
description
 */