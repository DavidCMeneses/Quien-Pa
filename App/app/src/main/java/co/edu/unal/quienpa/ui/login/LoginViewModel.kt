package co.edu.unal.quienpa.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Pantalla de Inicio de Sesión"
    }
    val text: LiveData<String> = _text
}
