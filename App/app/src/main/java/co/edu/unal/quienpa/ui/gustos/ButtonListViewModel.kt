package co.edu.unal.quienpa.ui.buttonlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ButtonListViewModel : ViewModel() {

    private val _selectedButtons = MutableLiveData<Set<Int>>()
    val selectedButtons: LiveData<Set<Int>> = _selectedButtons

    fun selectButton(buttonId: Int) {
        val currentSelection = _selectedButtons.value?.toMutableSet() ?: mutableSetOf()
        if (currentSelection.contains(buttonId)) {
            currentSelection.remove(buttonId)
        } else {
            currentSelection.add(buttonId)
        }
        _selectedButtons.value = currentSelection
    }
}
