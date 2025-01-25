package co.edu.unal.qnpa.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import co.edu.unal.qnpa.CategoriesScreen
import co.edu.unal.qnpa.DetailScreen
import co.edu.unal.qnpa.HomeScreen
import co.edu.unal.qnpa.LoginScreen
import co.edu.unal.qnpa.RegisterScreen
import co.edu.unal.qnpa.SearchScreen
import co.edu.unal.qnpa.UserProfileScreen

@Composable
fun NavigationWrapper (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Login){
        composable<Login> {
            LoginScreen(
                navigateToHome = {navController.navigate(Home)},
                navigateToRegister = {navController.navigate(Register)}
                )
        }
        composable<Register> {
            RegisterScreen (
                navigateToLogin = { navController.popBackStack() },
                navigateToCategories = { navController.navigate(Categories) }
            )
        }
        composable<Categories> {
            CategoriesScreen()
        }
        composable<Home> {
            HomeScreen(
                navigateToSearch = { navController.navigate(Search) },
                navigateToProfile = { navController.navigate(UserProfile) }
            )
        }
        composable<Search> {
            SearchScreen( goBack = { navController.popBackStack() })
        }
        composable<UserProfile> {
            UserProfileScreen( goBack = { navController.popBackStack() })
        }
        composable<Detail> { backStackEntry ->
            val detail = backStackEntry.toRoute<Detail>()
            DetailScreen(detail.name){ navController.navigate(Login) {
                popUpTo(Login) { inclusive = false }
            } }
        }
    }
}