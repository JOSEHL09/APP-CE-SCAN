package com.contraentrega.ceapp.ui.login

import androidx.compose.ui.draw.clip
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material3.OutlinedTextField
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.contraentrega.ceapp.R
import com.contraentrega.ceapp.ui.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val email by remember { viewModel::email }
    val password by remember { viewModel::password }
    val isLoading by remember { viewModel::isLoading }
    var passwordVisible by remember { mutableStateOf(false) }

    val offsetXEmail = remember { Animatable(0f) }
    val offsetXPassword = remember { Animatable(0f) }

    // Mostrar mensaje con Toast
    LaunchedEffect(viewModel.snackbarMessage.value) {
        viewModel.snackbarMessage.value?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.dismissSnackbar()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo ondulado superior
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
        ) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(0f, height * 0.6f)
                quadraticTo(width * 0.5f, height, width, height * 0.6f)
                lineTo(width, 0f)
                lineTo(0f, 0f)
                close()
            }

            drawPath(
                path = path,
                color = Color(0xFFB71C1C) // Rojo oscuro
            )
        }

        // Logo centrado en la parte ondulada
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logologin2),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp),
                contentScale = ContentScale.Fit
            )
        }

        // Contenido principal
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 280.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido a CE",
                fontSize = 20.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email
            Box(
                modifier = Modifier
                    .offset(x = offsetXEmail.value.dp)
                    .fillMaxWidth()
                    .height(62.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Transparent)
            ) {
                OutlinedTextField(
                    value = email,
                    onValueChange = { viewModel.email = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent
                    ),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Password con ícono de visibilidad
            Box(
                modifier = Modifier
                    .offset(x = offsetXPassword.value.dp)
                    .fillMaxWidth()
                    .height(62.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.Transparent)
            ) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { viewModel.password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxSize(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor = Color.Transparent
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                contentDescription = "Toggle password visibility",
                                tint = Color.Gray
                            )
                        }
                    },
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botón de iniciar sesión
            Button(
                onClick = {
                    var shouldShake = false

                    scope.launch {
                        if (email.isBlank()) {
                            shouldShake = true
                            offsetXEmail.animateTo(
                                targetValue = 15f,
                                animationSpec = repeatable(
                                    iterations = 3,
                                    animation = tween(50),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )
                            offsetXEmail.snapTo(0f)
                        }

                        if (password.isBlank()) {
                            shouldShake = true
                            offsetXPassword.animateTo(
                                targetValue = 15f,
                                animationSpec = repeatable(
                                    iterations = 3,
                                    animation = tween(50),
                                    repeatMode = RepeatMode.Reverse
                                )
                            )
                            offsetXPassword.snapTo(0f)
                        }

                        if (shouldShake) {
                            viewModel.snackbarMessage.value = "Por favor, completa todos los campos."
                            return@launch
                        }

                        // Si todo está correcto, procede al login
                        viewModel.login(email, password) {
                            navController.navigate(Screen.Menu.withUser(email)) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("INICIAR SESIÓN", fontSize = 16.sp)
                }
            }
        }
    }
}



















