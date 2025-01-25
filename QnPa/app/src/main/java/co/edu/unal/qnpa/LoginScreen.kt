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

@Preview
@Composable
fun LoginScreen(
    navigateToHome: () -> Unit = {},
    navigateToRegister: () -> Unit = {}

    ){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            //.padding(28.dp)
    )
    {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.logo), contentDescription = "Login Image", modifier = Modifier.size(230.dp))
        Text(text = "Bienvenido a Quien Pa", fontSize = 25.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Ingrese sus credenciales", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = email, onValueChange = {
            email = it
        }, label = { Text(text = "Usuario") })
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(value = password, onValueChange = {
            password = it
        }, label = { Text(text = "Contraseña") }, visualTransformation = PasswordVisualTransformation())

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            Log.i("Credentials", "Email: $email, Password: $password")
            navigateToHome()
        }) {
            Text(text = "Iniciar sesión")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            navigateToRegister()
        }){
            Text(text = "Registrarse")
        }
        Spacer(modifier = Modifier.height(24.dp))
        TextButton( onClick = { } ) {
            Text(text = "Olvidaste tu contraseña?")
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(text = "O iniciar sesión con:", fontSize = 15.sp)
        Spacer(modifier = Modifier.height(20.dp))
        Image(painter = painterResource(id = R.drawable.google), contentDescription = "Google Icon",
            modifier = Modifier
                .size(40.dp)
                .clickable {  } )
        Spacer(modifier = Modifier.height(60.dp))
    } }
}