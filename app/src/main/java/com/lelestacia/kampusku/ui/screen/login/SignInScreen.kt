package com.lelestacia.kampusku.ui.screen.login

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.ui.theme.KampuskuTheme
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun SignInScreen(
    state: SignInScreenState,
    onEvent: (SignInScreenEvent) -> Unit,
    eventMessage: Channel<String>
) {
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }
    LaunchedEffect(Unit) {
        eventMessage.receiveAsFlow().collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }
    Surface {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState
                )
            }
        ) { paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        horizontal = 16.dp
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.graduating_student),
                    contentDescription = null,
                    modifier = Modifier.size(75.dp)
                )
                Text(
                    text = "Welcome Back",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                OutlinedTextField(
                    value = state.email,
                    onValueChange = { newEmail ->
                        onEvent(SignInScreenEvent.OnEmailChanged(newEmail))
                    },
                    label = {
                        Text(text = "Email")
                    },
                    placeholder = {
                        Text(text = "example@mail.com")
                    },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    readOnly = state.isNowLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.password,
                    onValueChange = { newPassword ->
                        onEvent(SignInScreenEvent.OnPasswordChanged(newPassword))
                    },
                    label = {
                        Text(text = "Password")
                    },
                    placeholder = {
                        Text(text = "Your Password Here")
                    },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    visualTransformation =
                    if (state.shouldPasswordBeVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { onEvent(SignInScreenEvent.OnPasswordVisibilityToggled) }) {
                            if (state.shouldPasswordBeVisible) {
                                Icon(
                                    imageVector = Icons.Default.LockOpen,
                                    contentDescription = null
                                )
                            } else {
                                Icon(imageVector = Icons.Default.Lock, contentDescription = null)
                            }
                        }
                    },
                    readOnly = state.isNowLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                Crossfade(
                    targetState = state.isNowLoading,
                    label = "Button transition"
                ) { nowLoading ->
                    if (nowLoading) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        Button(
                            onClick = { onEvent(SignInScreenEvent.OnLogin) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp
                                )
                        ) {
                            Text(text = "SignIn")
                            LocalSoftwareKeyboardController.current?.hide()
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Preview Sign In Screen Day Mode",
    device = "id:pixel_7",
    showBackground = true
)
@Preview(
    name = "Preview Sign In Screen Dark Mode",
    device = "id:pixel_7",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewSplashScreen() {
    KampuskuTheme {
        SignInScreen(
            state = SignInScreenState(),
            onEvent = {},
            eventMessage = Channel()
        )
    }
}