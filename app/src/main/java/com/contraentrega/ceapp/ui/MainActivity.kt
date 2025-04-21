package com.contraentrega.ceapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.contraentrega.ceapp.ui.navigation.AppNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContent {
                val navController = rememberNavController()
                AppNavigation(navController)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error initializing app", e)
            // Mostrar un mensaje de error o tomar alguna acción apropiada
        }
    }
}






