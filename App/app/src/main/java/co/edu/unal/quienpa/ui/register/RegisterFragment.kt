package co.edu.unal.quienpa.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import co.edu.unal.quienpa.R
import co.edu.unal.quienpa.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var registerViewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        registerViewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnCreateAccount.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val nickname = binding.etNickname.text.toString()

            // Actualiza el ViewModel con los datos ingresados
            registerViewModel.setName(name)
            registerViewModel.setEmail(email)
            registerViewModel.setPassword(password)
            registerViewModel.setNickname(nickname)

            // Navega a la pantalla de la lista de botones
            findNavController().navigate(R.id.action_register_to_button_list)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
