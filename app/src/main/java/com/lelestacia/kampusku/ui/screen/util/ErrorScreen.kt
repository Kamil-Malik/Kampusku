package com.lelestacia.kampusku.ui.screen.util

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ErrorScreen(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}