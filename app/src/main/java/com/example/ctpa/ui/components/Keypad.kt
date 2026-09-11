package com.example.ctpa.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ctpa.ui.theme.*

@Composable
fun Keypad(
    onDigitClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Filas 1-3: números 1-9
        listOf(
            listOf("1" to "", "2" to "ABC", "3" to "DEF"),
            listOf("4" to "GHI", "5" to "JKL", "6" to "MNO"),
            listOf("7" to "PQRS", "8" to "TUV", "9" to "WXYZ")
        ).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { (key, letters) ->
                    KeypadButton(
                        key = key,
                        letters = letters,
                        onClick = { onDigitClick(key) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Fila 4: NFC | 0 | CLEAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            KeypadButton(
                key = "NFC",
                letters = "RFID",
                onClick = { /* NFC */ },
                modifier = Modifier.weight(1f),
                keyColor = Gray500
            )
            KeypadButton(
                key = "0",
                letters = "...",
                onClick = { onDigitClick("0") },
                modifier = Modifier.weight(1f)
            )
            KeypadButton(
                key = "",
                letters = "CLEAR",
                onClick = onDeleteClick,
                modifier = Modifier.weight(1f),
                showBackspace = true
            )
        }
    }
}

@Composable
private fun KeypadButton(
    key: String,
    letters: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    keyColor: Color = Gray900,
    showBackspace: Boolean = false
) {
    Column(
        modifier = modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(Gray100)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showBackspace) {
            Icon(
                imageVector = Icons.Filled.Backspace,
                contentDescription = "Borrar",
                tint = Gray500,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = key,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = keyColor,
                fontSize = 28.sp
            )
        }
        Text(
            text = letters,
            style = MaterialTheme.typography.labelSmall,
            color = Gray500,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )
    }
}