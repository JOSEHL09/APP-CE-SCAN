package com.contraentrega.ceapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.contraentrega.ceapp.ui.splash.SplashScreen
import com.contraentrega.ceapp.ui.login.LoginScreen
import com.contraentrega.ceapp.ui.menu.MenuScreen

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
        composable(Screen.Login.route) {
            LoginScreen(navController)
        }
        composable(Screen.Menu.route) { backStackEntry ->
            val user = backStackEntry.arguments?.getString("user") ?: "Usuario"
            MenuScreen(navController, user)
        }
    }
}



