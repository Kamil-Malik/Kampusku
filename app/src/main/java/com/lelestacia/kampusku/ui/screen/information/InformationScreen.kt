package com.lelestacia.kampusku.ui.screen.information

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.kampusku.ui.theme.KampuskuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(
    onNavigateBack: () -> Unit
) {
    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Information",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { onNavigateBack() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }
        ) { paddingValues ->
            Text(
                text = "Aplikasi Kampusku merupakan aplikasi yang akan digunakan untuk pendataan mahasiswa. User nantinya bisa menambahkan, update, dan hapus data mahasiswa.",
                textAlign = TextAlign.Justify,
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(
    name = "Preview Dashboard Screen Day Mode",
    device = "id:pixel_7",
    showBackground = true
)
@Preview(
    name = "Preview Dashboard Screen Dark Mode",
    device = "id:pixel_7",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewInformationScreen() {
    KampuskuTheme {
        InformationScreen {

        }
    }
}