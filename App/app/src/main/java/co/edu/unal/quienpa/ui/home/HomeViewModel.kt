package co.edu.unal.quienpa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Buscar..."  // Placeholder para el texto de búsqueda
    }
    val text: LiveData<String> = _text
}
