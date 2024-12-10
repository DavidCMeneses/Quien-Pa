package co.edu.unal.quienpa.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.unal.quienpa.R
import co.edu.unal.quienpa.databinding.FragmentHomeBinding

data class Item(val imageRes: Int, val text: String)

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val items = listOf(
            Item(R.drawable.image1, "Texto 1"),
            Item(R.drawable.image1, "Texto 2"),
            Item(R.drawable.image1, "Texto 3")
        )

        val adapter = BlockAdapter(items) { item ->
            // Manejar click en el bloque
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Configurar clicks en los iconos
        binding.iconLeft.setOnClickListener {
            Toast.makeText(context, "Icono izquierdo clickeado", Toast.LENGTH_SHORT).show()
            // Navega a la pantalla correspondiente
        }

        binding.iconRight.setOnClickListener {
            Toast.makeText(context, "Icono derecho clickeado", Toast.LENGTH_SHORT).show()
            // Navega a la pantalla de notificaciones
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
