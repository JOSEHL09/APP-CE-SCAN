package com.contraentrega.ceapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RoundedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true // Agregar el parámetro enabled con un valor predeterminado
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.Red),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        enabled = enabled // Usar el parámetro enabled aquí
    ) {
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}



