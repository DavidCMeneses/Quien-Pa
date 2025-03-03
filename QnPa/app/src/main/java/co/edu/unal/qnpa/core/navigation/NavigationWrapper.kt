package co.edu.unal.qnpa.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import co.edu.unal.qnpa.ActivityScreen
import co.edu.unal.qnpa.CategoriesScreen
import co.edu.unal.qnpa.DetailScreen
import co.edu.unal.qnpa.EditUserProfileScreen
import co.edu.unal.qnpa.HomeScreen
import co.edu.unal.qnpa.LoginScreen
import co.edu.unal.qnpa.OtherUserProfileScreen
import co.edu.unal.qnpa.RegisterScreen
import co.edu.unal.qnpa.SearchScreen
import co.edu.unal.qnpa.SessionManager
import co.edu.unal.qnpa.UserProfileScreen

@Composable
fun NavigationWrapper(isLogged: Boolean = false) {
    val navController = rememberNavController()
    val sessionManager = SessionManager(LocalContext.current) // Inicializar el SessionManager

    NavHost(navController = navController, startDestination = if (isLogged) Home else Login) {
        composable<Login> {
            LoginScreen(
                navigateToHome = { navController.navigate(Home) },
                navigateToRegister = { navController.navigate(Register) },
                sessionManager = sessionManager // Pasar el SessionManager
            )
        }
        composable<Register> {
            RegisterScreen(
                navigateToLogin = { navController.popBackStack() },
                navigateToCategories = { navController.navigate(Categories) },
                sessionManager = sessionManager // Pasar el SessionManager
            )
        }
        composable<Categories> {
            CategoriesScreen(
                navigateToHome = { navController.navigate(Home) },
                sessionManager = sessionManager // Pasar el SessionManager
            )
        }
        composable<Home> {
            HomeScreen(
                navigateToSearch = { navController.navigate(Search) },
                navigateToProfile = { navController.navigate(UserProfile) },
                navigateToCategories = { navController.navigate(Categories) },
                navigateToEditProfile = { navController.navigate(EditUserProfile) },
                navigateToLogin = {
                    navController.navigate(Login) {
                        popUpTo(navController.graph.startDestinationId) {
                            inclusive = true
                        }
                    }
                },
                navigateToActivityDetails = { activityId -> // Nuevo parámetro para navegar a ActivityScreen
                    navController.navigate(ActivityScreenRoute(activityId))
                },
                sessionManager = sessionManager // Pasar el SessionManager
            )
        }
        composable<Search> {
            SearchScreen(
                goBack = { navController.popBackStack() },
                navigateToActivityDetails = { activityId -> // Nuevo parámetro para navegar a ActivityScreen
                    navController.navigate(ActivityScreenRoute(activityId))
                }
            )
        }
        composable<UserProfile> {
            UserProfileScreen(
                navigateToEditProfile = { navController.navigate(EditUserProfile) },
                goBack = { navController.popBackStack() },
                sessionManager = sessionManager // Pasar el SessionManager
            )
        }
        composable<ActivityScreenRoute> { backStackEntry ->
            val activity = backStackEntry.toRoute<ActivityScreenRoute>()
            ActivityScreen(
                activityId = activity.activityId,
                onBackClick = { navController.popBackStack() },
                navigateToOtherUserProfile = { userId -> // Nueva función de navegación
                    navController.navigate(OtherUserProfileRoute(userId))
                }
            )
        }
        composable<EditUserProfile> {
            EditUserProfileScreen(
                goBack = { navController.popBackStack() },
                sessionManager = sessionManager // Pasar el SessionManager
            )
        }
        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            DetailScreen(detail.name) {
                navController.navigate(Login) {
                    popUpTo(Login) { inclusive = false }
                }
            }
        }
        // Nueva ruta para OtherUserProfileScreen
        composable<OtherUserProfileRoute> { backStackEntry ->
            val otherUserProfile = backStackEntry.toRoute<OtherUserProfileRoute>()
            OtherUserProfileScreen(
                userId = otherUserProfile.userId,
                goBack = { navController.popBackStack() }
            )
        }
    }
}