package co.edu.unal.quienpa.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import co.edu.unal.quienpa.R
import co.edu.unal.quienpa.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val usernameField = binding.username
        val passwordField = binding.password
        val loginButton = binding.loginButton

        loginButton.setOnClickListener {
            val username = usernameField.text.toString()
            val password = passwordField.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(activity, "Iniciar sesión exitoso", Toast.LENGTH_SHORT).show()
                // Navegar a HomeFragment y borrar el historial de LoginFragment
                findNavController().navigate(R.id.action_login_to_home)
            } else {
                Toast.makeText(activity, "Por favor, ingresa las credenciales", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRegister.setOnClickListener {
            // Navegar a la pantalla de registro
            findNavController().navigate(R.id.action_login_to_register)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
