package com.contraentrega.ceapp.ui.qrscanner

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.contraentrega.ceapp.ui.components.RoundedButton
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun QrScreen(viewModel: QrViewModel = viewModel()) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Usar accompanist para manejar permisos
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Cliente dropdown
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            ) {
                ClientSelector(
                    selectedClient = viewModel.selectedClient.value,
                    clients = viewModel.clients,
                    onClientSelected = { client ->
                        viewModel.updateSelectedClient(client)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.isScannerOpen) {
                if (cameraPermissionState.status.isGranted) {
                    // Si tenemos permiso, mostrar la cámara
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .aspectRatio(3f / 4f)
                            .background(MaterialTheme.colorScheme.surface)
                    ) {
                        BarcodeScannerView(
                            modifier = Modifier.fillMaxSize(),
                            lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current,
                            isScannerActive = viewModel.isScannerActive.value,
                            onBarcodeDetected = { code -> viewModel.onBarcodeScanned(code) }
                        )
                    }
                } else {
                    // Si no tenemos permiso, mostrar mensaje y botón para solicitarlo
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .aspectRatio(3f / 4f)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Se requiere permiso de cámara para escanear")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                                Text("Permitir Cámara")
                            }
                        }
                    }

                    // Cerrar el scanner ya que no se puede usar sin permisos
                    LaunchedEffect(Unit) {
                        viewModel.toggleScanner()
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

// Modificar en QrScreen.kt - Dentro del componente RoundedButton
            RoundedButton(
                text = if (viewModel.isScannerOpen) "CERRAR SCANNER" else "INICIAR SCANNER",
                onClick = {
                    if (viewModel.selectedClient.value == null) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Por favor, seleccione un cliente")
                        }
                    } else if (viewModel.totalOrders.value == 0) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("No hay órdenes pendientes para este cliente")
                        }
                        // Mostrar mensaje y reproducir sonido de alarma
                        viewModel.showNoOrdersAlert()
                    } else {
                        if (!viewModel.isScannerOpen && !cameraPermissionState.status.isGranted) {
                            cameraPermissionState.launchPermissionRequest()
                        } else {
                            viewModel.toggleScanner()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                enabled = viewModel.selectedClient.value != null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClientSelector(
    selectedClient: Client?,
    clients: List<Client>,
    onClientSelected: (Client) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedClient?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Seleccionar cliente") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            clients.forEach { client ->
                DropdownMenuItem(
                    text = { Text(client.name) },
                    onClick = {
                        onClientSelected(client)
                        expanded = false
                    }
                )
            }
        }
    }
}