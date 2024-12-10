package co.edu.unal.quienpa.ui.buttonlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.unal.quienpa.R
import co.edu.unal.quienpa.databinding.FragmentButtonListBinding

class ButtonListFragment : Fragment() {

    private var _binding: FragmentButtonListBinding? = null
    private val binding get() = _binding!!
    private lateinit var buttonListViewModel: ButtonListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        buttonListViewModel = ViewModelProvider(this).get(ButtonListViewModel::class.java)

        _binding = FragmentButtonListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val buttons = listOf("Botón 1", "Botón 2", "Botón 3", "Botón 4", "Botón 5")
        val adapter = ButtonAdapter(buttons)

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        binding.btnAccept.setOnClickListener {
            // Navega a la pantalla de inicio utilizando findNavController
            findNavController().navigate(R.id.action_button_list_to_home)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
