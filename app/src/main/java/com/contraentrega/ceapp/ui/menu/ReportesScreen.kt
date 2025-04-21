package com.contraentrega.ceapp.ui.menu

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contraentrega.ceapp.ui.components.RoundedButton
import com.contraentrega.ceapp.ui.qrscanner.QrViewModel

@Composable
fun ReportesScreen(viewModel: QrViewModel = viewModel()) {
    // Actualizar contador al entrar a la pantalla si hay un cliente seleccionado
    LaunchedEffect(Unit) {
        viewModel.selectedClient.value?.let { client ->
            Log.d("REPORTES", "Actualizando contador para cliente: ${client.name}")
            viewModel.fetchTotalOrders(client.id)
        }
    }
    val context = LocalContext.current
    val scannedBarcodes by remember { mutableStateOf(viewModel.scannedBarcodes) }
    val successfulCodes = scannedBarcodes.filter { it.responseCode == 200 }

    // Usar el valor dinámico de totalOrders
    val totalOrders by viewModel.totalOrders
    val scannedProducts = successfulCodes.size

    // Estado para mostrar el indicador de carga
    val isLoading by viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Productos Escaneados",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar indicador de carga si es necesario
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(32.dp),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            // Gráfico de progreso circular con el valor dinámico
            CircularProgressChart(totalOrders, scannedProducts)
        }

        // Estado para controlar la visibilidad del diálogo de confirmación
        var showClearDataDialog by remember { mutableStateOf(false) }

        Spacer(modifier = Modifier.height(16.dp))

        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = MaterialTheme.shapes.medium,
            shadowElevation = 4.dp
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(successfulCodes) { barcode ->
                    ListItem(
                        headlineContent = { Text(barcode.code) },
                        leadingContent = {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Green
                            )
                        },
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón LIMPIAR APP - Ahora muestra el diálogo de confirmación
        RoundedButton(
            text = "FINALIZAR RECOJO",
            onClick = {
                showClearDataDialog = true // Mostrar diálogo de confirmación
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        // Diálogo de confirmación para limpiar datos
        if (showClearDataDialog) {
            AlertDialog(
                onDismissRequest = { showClearDataDialog = false },
                title = { Text("Limpiar Datos") },
                text = { Text("¿Estás seguro de que quieres limpiar todos los datos actuales?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.clearAppData()
                            Toast.makeText(context, "Datos limpiados", Toast.LENGTH_SHORT).show()
                            showClearDataDialog = false
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showClearDataDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun CircularProgressChart(total: Int, scanned: Int) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp)
    ) {
        CircularProgressIndicator(
            progress = if (total > 0) scanned.toFloat() / total.toFloat() else 0f,
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primary,
            strokeWidth = 12.dp
        )
        Text(
            text = "$scanned / $total",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}