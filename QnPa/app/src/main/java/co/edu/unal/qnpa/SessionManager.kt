package co.edu.unal.qnpa

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }

    // Guardar la sesión del usuario
    fun saveSession(userId: String, userEmail: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_USER_EMAIL, userEmail)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }

    // Obtener el ID del usuario
    fun getUserId(): String? {
        return sharedPreferences.getString(KEY_USER_ID, null)
    }

    // Obtener el correo del usuario
    fun getUserEmail(): String? {
        return sharedPreferences.getString(KEY_USER_EMAIL, null)
    }

    // Verificar si el usuario está logueado
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Cerrar la sesión del usuario
    fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}