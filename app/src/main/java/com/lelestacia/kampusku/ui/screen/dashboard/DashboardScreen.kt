package com.lelestacia.kampusku.ui.screen.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.ui.theme.KampuskuTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onEvent: (DashboardScreenEvent) -> Unit
) {
    Surface {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(
                        text = "Dashboard",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                })
            }
        ) { paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.graduating_student),
                    contentDescription = null,
                    modifier = Modifier.size(125.dp)
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier
                        .padding(
                            horizontal = 24.dp
                        )
                ) {
                    Button(
                        onClick = { onEvent(DashboardScreenEvent.OnShowDataClicked) },
                        shape = RoundedCornerShape(percent = 15),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.List, contentDescription = null)
                            Text(text = "Show Data")
                        }
                    }
                    Button(
                        onClick = { onEvent(DashboardScreenEvent.OnInputDataClicked) },
                        shape = RoundedCornerShape(percent = 15),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null)
                            Text(text = "Input Data")
                        }
                    }
                    Button(
                        onClick = { onEvent(DashboardScreenEvent.OnInformationClicked) },
                        shape = RoundedCornerShape(percent = 15),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(imageVector = Icons.Default.Info, contentDescription = null)
                            Text(text = "Information")
                        }
                    }
                }
            }
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
fun PreviewDashboardScreen() {
    KampuskuTheme {
        DashboardScreen(onEvent = {})
    }
}