package com.contraentrega.ceapp.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.contraentrega.ceapp.network.RetrofitInstance
import java.lang.Exception
import android.util.Log
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var snackbarMessage = mutableStateOf<String?>(null)


    fun login(email: String, password: String, onSuccess: () -> Unit) {
        if (email.isBlank() || password.isBlank()) {
            snackbarMessage.value = "Por favor, completa todos los campos."
            return
        }

        isLoading = true

        viewModelScope.launch {
            try {

                val response = RetrofitInstance.authApi.login(
                    username = email,
                    password = password
                )

                isLoading = false

                Log.d("LoginSuccess", "Token recibido: ${response.accessToken}")

                onSuccess()

            } catch (e: Exception) {
                isLoading = false
                snackbarMessage.value = when (e) {
                    is IOException -> "Verifica tu señal de internet."
                    is HttpException -> "Error al iniciar sesión. Verifica tus datos."
                    else -> "Error desconocido. Inténtalo de nuevo."
                }
                Log.e("LoginError", "Fallo en login", e)
            }
        }
    }

    fun dismissSnackbar() {
        snackbarMessage.value = null
    }
}


