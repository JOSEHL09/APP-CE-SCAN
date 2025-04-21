package com.contraentrega.ceapp.ui.menu

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.contraentrega.ceapp.R
import com.contraentrega.ceapp.ui.components.LogoutDialogStyled
import com.contraentrega.ceapp.ui.navigation.Screen
import com.contraentrega.ceapp.ui.qrscanner.QrScreen
import com.contraentrega.ceapp.ui.qrscanner.QrViewModel

enum class MenuTab(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    QR("Lector QR", Icons.Default.QrCode),
    Reportes("Reportes", Icons.Default.Assessment)
}

@Composable
fun MenuScreen(navController: NavController, user: String, qrViewModel: QrViewModel = viewModel()) {
    var selectedTab by remember { mutableStateOf(MenuTab.QR) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
    }

    LaunchedEffect(selectedTab) {
        if (selectedTab != MenuTab.QR && qrViewModel.isScannerOpen) {
            qrViewModel.toggleScanner()
        }
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.contraentrega1),
                    contentDescription = "Logo Contraentrega",
                    modifier = Modifier.size(40.dp)
                )

                IconButton(onClick = { showLogoutDialog = true }) {
                    Icon(Icons.Default.PowerSettingsNew, contentDescription = "Cerrar sesión")
                }
            }
        },
        bottomBar = {
            NavigationBar {
                MenuTab.values().forEach { tab ->
                    NavigationBarItem(
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) },
                        selected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                MenuTab.QR -> QrScreen(viewModel = qrViewModel)
                MenuTab.Reportes -> ReportesScreen()
            }
        }

        if (showLogoutDialog) {
            LogoutDialogStyled(
                user = user,
                onDismiss = { showLogoutDialog = false },
                onLogout = {
                    showLogoutDialog = false
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Menu.route) { inclusive = true }
                    }
                }
            )
        }

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = { Text("Salir de la aplicación") },
                text = { Text("¿Estás seguro que quieres salir de la aplicación?") },
                confirmButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Cancelar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text("Salir")
                    }
                }
            )
        }
    }
}