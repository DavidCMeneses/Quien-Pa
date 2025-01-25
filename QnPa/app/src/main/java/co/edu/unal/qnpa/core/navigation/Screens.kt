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
data class Detail (val name: String)