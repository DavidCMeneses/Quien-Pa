package co.edu.unal.quienpa.ui.buttonlist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import co.edu.unal.quienpa.R
import co.edu.unal.quienpa.databinding.ItemButtonBinding

class ButtonAdapter(private val buttons: List<String>) : RecyclerView.Adapter<ButtonAdapter.ButtonViewHolder>() {

    private val selectedButtons = mutableSetOf<Int>()

    inner class ButtonViewHolder(val binding: ItemButtonBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(buttonText: String, position: Int) {
            binding.button.text = buttonText
            updateButtonAppearance(binding, position)

            binding.button.setOnClickListener {
                toggleButtonSelection(position)
                updateButtonAppearance(binding, position)
            }
        }

        private fun updateButtonAppearance(binding: ItemButtonBinding, position: Int) {
            if (selectedButtons.contains(position)) {
                binding.button.setBackgroundResource(R.color.selected_button) // Cambia el color del botón a verde
                binding.button.setTextColor(Color.WHITE) // Cambia el color del texto cuando está seleccionado
            } else {
                binding.button.setBackgroundResource(R.color.unselected_button) // Color original del botón
                binding.button.setTextColor(Color.BLACK) // Color original del texto
            }
        }

        private fun toggleButtonSelection(position: Int) {
            if (selectedButtons.contains(position)) {
                selectedButtons.remove(position)
            } else {
                selectedButtons.add(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ButtonViewHolder {
        val binding = ItemButtonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ButtonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ButtonViewHolder, position: Int) {
        holder.bind(buttons[position], position)
    }

    override fun getItemCount(): Int = buttons.size
}

