package co.edu.unal.quienpa.ui.home


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import co.edu.unal.quienpa.R
import co.edu.unal.quienpa.databinding.FragmentHomeBinding

data class Item(val imageUrl: String, val text: String)

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val items = mutableListOf<Item>() // Lista para guardar los elementos finales.
        val adapter = BlockAdapter(items) { item ->
            Toast.makeText(context, "Clicked on: ${item.text}", Toast.LENGTH_SHORT).show()
        }

        // Configurar RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Preparar etiquetas y rutas
        val labels = listOf("Fútbol", "Natación", "Quidditch")
        val paths = listOf("images/futbol.png", "images/natacion.png", "images/quidditch.png")
        val imageUrls = mutableListOf<String?>(null, null, null) // Placeholder para las URL obtenidas.

        // Fetch all image URLs
        var fetchedCount = 0
        for ((index, path) in paths.withIndex()) {
            fetchImageUrl(path) { imageUrl ->
                imageUrls[index] = imageUrl // Asignar la URL obtenida al índice correcto.
                fetchedCount++

                if (fetchedCount == paths.size) {
                    // Se obtienen todas las imágenes, se actualizan los elementos y se notifica al adaptador.
                    for (i in labels.indices) {
                        items.add(Item(imageUrls[i] ?: "", labels[i]))
                    }
                    adapter.notifyDataSetChanged() // Notificar al adaptador cuando la lista completa esté lista.
                }
            }
        }

        // Configure icon clicks
        binding.iconLeft.setOnClickListener {
            Toast.makeText(context, "Icono izquierdo clickeado", Toast.LENGTH_SHORT).show()
        }

        binding.iconRight.setOnClickListener {
            Toast.makeText(context, "Icono derecho clickeado", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    /**
     * Obtiene una única URL de imagen e invoca una llamada de retorno con la URL.
     */
    private fun fetchImageUrl(path: String, onSuccess: (String) -> Unit) {
        val storageReference: StorageReference = firebaseStorage.reference.child(path)
        storageReference.downloadUrl.addOnSuccessListener { uri ->
            Log.d("HomeFragment", "Imagen cargada exitosamente: $uri")
            onSuccess(uri.toString()) // Pass the URL to the callback
        }.addOnFailureListener { exception ->
            Log.e("HomeFragment", "Falla al cargar la imagen en: $path: ${exception.message}")
            Toast.makeText(context, "Error al cargar la imagen de la ruta: $path", Toast.LENGTH_SHORT).show()
            onSuccess("") // Utilice una cadena vacía para evitar fallos.
        }
    }

}