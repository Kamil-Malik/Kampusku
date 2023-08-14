package com.lelestacia.kampusku.ui.screen.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.ui.route.Screen
import com.lelestacia.kampusku.ui.theme.KampuskuTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigate: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    Surface {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.graduating_student),
                contentDescription = null,
                modifier = Modifier
                    .size(125.dp)
            )
        }
    }
    LaunchedEffect(key1 = Unit) {
        scope.launch {
            delay(5000)
            onNavigate(Screen.SignIn.route)
        }
    }
}

@Preview(
    name = "Preview Splash Screen Day Mode",
    device = "id:pixel_7",
    showBackground = true
)
@Preview(
    name = "Preview Splash Screen Dark Mode",
    device = "id:pixel_7",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewSplashScreen() {
    KampuskuTheme {
        SplashScreen(onNavigate = {})
    }
}