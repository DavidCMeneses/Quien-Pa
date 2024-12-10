package co.edu.unal.quienpa.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import co.edu.unal.quienpa.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // Esta propiedad solo es válida entre onCreateView y onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Obtener una instancia del ViewModel asociado con este fragmento
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Inflar el layout del fragmento utilizando data binding
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Enlazar el TextView con el textDashboard definido en el layout
        val textView: TextView = binding.textDashboard
        // Observar cambios en el LiveData del ViewModel y actualizar el TextView en consecuencia
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Limpiar la referencia al binding para evitar fugas de memoria
        _binding = null
    }
}
