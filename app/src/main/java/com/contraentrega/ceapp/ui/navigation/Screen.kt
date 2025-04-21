package com.contraentrega.ceapp.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Menu : Screen("menu/{user}") {
        fun withUser(user: String) = "menu/$user"
    }


    // Ejemplo para futuro uso:
    // object Detail : Screen("detail/{itemId}") {
    //     fun passId(itemId: String) = "detail/$itemId"
    // }
}
