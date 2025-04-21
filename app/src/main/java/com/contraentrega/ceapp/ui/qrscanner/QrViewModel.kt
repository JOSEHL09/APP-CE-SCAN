package com.contraentrega.ceapp.ui.qrscanner

import android.app.Application
import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import android.util.Log
import com.contraentrega.ceapp.model.CountTodayResponse
import com.contraentrega.ceapp.model.UpdateOrderRequest
import com.contraentrega.ceapp.network.RetrofitInstance
import com.contraentrega.ceapp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ScannedBarcode(val code: String, val responseCode: Int)
data class Client(val name: String, val id: Int)

class QrViewModel(application: Application) : AndroidViewModel(application) {
    var showScanner = mutableStateOf(false)
        private set

    var isScanning = mutableStateOf(false)
        private set

    var selectedClient = mutableStateOf<Client?>(null)

    // Nuevo estado para almacenar el total de órdenes del cliente seleccionado
    private val _totalOrders = mutableStateOf(0)
    val totalOrders: State<Int> = _totalOrders

    private var lastScannedTime = 0L
    private val minDelayBetweenScans = 3000L

    private val _scannedBarcode = mutableStateOf<String?>(null)
    val scannedBarcode: State<String?> = _scannedBarcode

    private val _scannedBarcodes = mutableStateListOf<ScannedBarcode>()
    val scannedBarcodes: List<ScannedBarcode> = _scannedBarcodes

    // Estado para manejar la carga de datos
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // Set para rastrear los clientes cuyos datos ya han sido obtenidos
    private val fetchedClients = mutableSetOf<Int>()

    val isScannerOpen: Boolean
        get() = showScanner.value

    val isScannerActive: MutableState<Boolean>
        get() = isScanning

    val clients = listOf(
        Client("PERU FIT", 42),
    )

    init {
        // Solo intentar obtener datos si hay un cliente seleccionado
        selectedClient.value?.let { client ->
            fetchTotalOrders(client.id)
        }
    }

    fun toggleScanner() {
        showScanner.value = !showScanner.value
        isScanning.value = showScanner.value
    }

    // Nueva función para actualizar el cliente seleccionado
    fun updateSelectedClient(client: Client) {
        if (selectedClient.value?.id != client.id) {
            selectedClient.value = client
            // Si cambia el cliente, necesitamos obtener datos nuevos
            fetchTotalOrders(client.id)
        }
    }

    // Modificada para verificar si ya tenemos los datos del cliente
    fun fetchTotalOrders(clientId: Int) {
        // Evitar llamadas repetidas a la API para el mismo cliente
        if (clientId in fetchedClients) {
            Log.d("API_COUNT", "Omitiendo consulta para cliente $clientId - datos ya obtenidos")
            return
        }

        _isLoading.value = true

        Log.d("API_COUNT", "Solicitando conteo para cliente: $clientId")

        val request = UpdateOrderRequest(
            idClient = clientId,
            orderNumber = 0,
            idStatus = 1,
            serviceType = 3
        )

        Log.d("API_COUNT", "Request body: ${request.idClient}, ${request.idStatus}, ${request.serviceType}")

        RetrofitInstance.orderApi.countTodayOrders(request).enqueue(object : Callback<CountTodayResponse> {
            override fun onResponse(call: Call<CountTodayResponse>, response: Response<CountTodayResponse>) {
                _isLoading.value = false

                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    Log.d("API_COUNT", "Respuesta exitosa: code=${body.code}, message=${body.message}, totalorders=${body.totalOrders}")
                    _totalOrders.value = body.totalOrders

                    // Marcar este cliente como ya consultado
                    fetchedClients.add(clientId)
                } else {
                    val errorMsg = "Error: ${response.code()} - ${response.message()}"
                    Log.e("API_COUNT", errorMsg)
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.e("API_COUNT", "Error body: $errorBody")
                    } catch (e: Exception) {
                        Log.e("API_COUNT", "No se pudo leer el cuerpo del error", e)
                    }
                    showToastAndPlaySound(errorMsg, R.raw.error_sound)
                }
            }

            override fun onFailure(call: Call<CountTodayResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e("API_COUNT", "Error en la petición", t)
                showToastAndPlaySound(
                    "Error de conexión: ${t.message}",
                    R.raw.error_sound
                )
            }
        })
    }

    fun onBarcodeScanned(code: String) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastScannedTime > minDelayBetweenScans) {
            lastScannedTime = currentTime

            if (_scannedBarcode.value != code) {
                _scannedBarcode.value = code
                isScanning.value = false

                // Verifica si el código ya ha sido escaneado antes de agregarlo
                val existingBarcode = _scannedBarcodes.find { it.code == code }

                val clientId = selectedClient.value?.id ?: return

                if (existingBarcode == null) {
                    // Crear la solicitud
                    val updateRequest = UpdateOrderRequest(
                        idClient = clientId,
                        orderNumber = code.toInt(),
                        idStatus = 26,
                        serviceType = 3
                    )

                    // Enviar la solicitud
                    RetrofitInstance.orderApi.updateOrderStatus(updateRequest).enqueue(object : Callback<UpdateOrderResponse> {
                        override fun onResponse(call: Call<UpdateOrderResponse>, response: Response<UpdateOrderResponse>) {
                            if (response.isSuccessful && response.body() != null) {
                                val updateResponse = response.body()!!

                                // Usamos el código de respuesta de la API
                                val updateCode = updateResponse.code
                                val responseCode = if (updateCode == "1") 200 else 400

                                // Agregamos el código escaneado con su estado
                                _scannedBarcodes.add(ScannedBarcode(code, responseCode))

                                // Determinamos el mensaje y el sonido según el resultado
                                val message = if (updateCode == "1") {
                                    "Orden $code actualizada correctamente"
                                } else {
                                    "Orden $code leída pero no actualizada"
                                }

                                val soundResId = when (updateCode) {
                                    "1" -> R.raw.success_sound
                                    "0" -> R.raw.alarm_sound  // Nuevo sonido de alarma
                                    else -> R.raw.error_sound
                                }

                                showToastAndPlaySound(message, soundResId)
                                Log.d("API_UPDATE", "Respuesta: ${updateResponse.code} - ${updateResponse.message} - Orden: $code")
                            } else {
                                _scannedBarcodes.add(ScannedBarcode(code, response.code()))
                                showToastAndPlaySound(
                                    "Error al actualizar orden $code: ${response.code()}",
                                    R.raw.error_sound
                                )
                            }
                        }

                        override fun onFailure(call: Call<UpdateOrderResponse>, t: Throwable) {
                            _scannedBarcodes.add(ScannedBarcode(code, 500))
                            showToastAndPlaySound("Error de conexión: ${t.message}", R.raw.error_sound)
                            Log.e("API_UPDATE", "Error al llamar API", t)
                        }
                    })
                } else {
                    // Mostrar que ya fue escaneado
                    showToastAndPlaySound("Código $code ya fue escaneado", R.raw.error_sound)
                }
            }
        }
    }

    // Añadir en QrViewModel.kt
    fun showNoOrdersAlert() {
        showToastAndPlaySound(
            "No hay órdenes pendientes para ${selectedClient.value?.name}",
            R.raw.alarm_sound
        )
    }

    // Añadir en QrViewModel.kt
    fun clearAppData() {
        // Limpiar cliente seleccionado
        selectedClient.value = null

        // Limpiar órdenes escaneadas
        _scannedBarcodes.clear()

        // Reiniciar contador de órdenes
        _totalOrders.value = 0

        // Limpiar caché de clientes consultados
        fetchedClients.clear()

        // Cerrar scanner si está abierto
        if (showScanner.value) {
            toggleScanner()
        }

        Log.d("APP_CLEAR", "Datos de la aplicación limpiados")
    }

    data class UpdateOrderResponse(
        val code: String,
        val message: String
    )

    private fun showToastAndPlaySound(message: String, soundResId: Int) {
        Toast.makeText(getApplication(), message, Toast.LENGTH_SHORT).show()
        val mediaPlayer = MediaPlayer.create(getApplication(), soundResId)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener { it.release() }
    }
}