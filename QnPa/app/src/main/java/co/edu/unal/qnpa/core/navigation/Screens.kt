package co.edu.unal.qnpa.core.navigation

import kotlinx.serialization.Serializable

@Serializable
object Login

@Serializable
object Register

@Serializable
object Categories

@Serializable
object Home

@Serializable
object Search

@Serializable
object UserProfile

@Serializable
object EditUserProfile

@Serializable
data class ActivityScreenRoute(val activityId: String) // Cambiar de "Activity" a "ActivityScreenRoute"

@Serializable
data class OtherUserProfileRoute(val userId: String) // Nueva ruta para OtherUserProfileScreen

@Serializable
data class Detail(val name: String)