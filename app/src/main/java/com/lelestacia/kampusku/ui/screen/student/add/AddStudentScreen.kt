package com.lelestacia.kampusku.ui.screen.student.add

import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.ui.component.button.DeleteImageCircleButton
import com.lelestacia.kampusku.ui.component.topbar.KampuskuTopBar
import com.lelestacia.kampusku.ui.theme.KampuskuTheme
import com.lelestacia.kampusku.util.UiText
import com.parassidhu.simpledate.toDateStandard
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun AddStudentScreen(
    state: AddStudentScreenState,
    event: Channel<UiText>,
    onEvent: (AddStudentScreenEvent) -> Unit,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }

    val galleryContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { newImageUri ->
            if (newImageUri != null) {
                onEvent(AddStudentScreenEvent.OnStudentPhotoUrlChanged(newImageUri))
            }
        })

    val galleryPermission = rememberPermissionState(
        permission =
        if (Build.VERSION.SDK_INT > 32) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    LaunchedEffect(key1 = Unit) {
        event.receiveAsFlow().collectLatest { uiText ->
            snackbarHostState.showSnackbar(
                message = when (uiText) {
                    is UiText.DynamicString -> uiText.value
                    is UiText.StringResource -> context.getString(uiText.id)
                },
                duration = SnackbarDuration.Short
            )
        }
    }

    Surface {
        Scaffold(
            topBar = {
                KampuskuTopBar(
                    title = "Add Student",
                    onNavigateBack = onNavigateBack
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = snackbarHostState
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    onEvent(AddStudentScreenEvent.OnSaveButtonPressed)
                }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null
                    )
                }
            }
        ) { paddingValues ->
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(
                        horizontal = 16.dp
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    Crossfade(
                        targetState = state.photoUrl.toString().isEmpty(),
                        label = "Photo"
                    ) { isPhotoUrlEmpty ->
                        if (isPhotoUrlEmpty) {
                            Image(
                                painter = painterResource(id = R.drawable.add_image),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp)
                                    .clickable {
                                        if (galleryPermission.status.isGranted) {
                                            galleryContract.launch("image/*")
                                        } else {
                                            galleryPermission.launchPermissionRequest()
                                        }
                                    }
                            )
                        } else {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Box(
                                    contentAlignment = Alignment.BottomEnd
                                ) {
                                    AsyncImage(
                                        model = state.photoUrl,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        alignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(175.dp)
                                            .clip(CircleShape)
                                    )
                                    DeleteImageCircleButton(
                                        onClicked = {
                                            onEvent(
                                                AddStudentScreenEvent.OnStudentPhotoUrlChanged(
                                                    Uri.EMPTY
                                                )
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = state.identificationNumber,
                    onValueChange = { newIdentificationNumber ->
                        onEvent(
                            AddStudentScreenEvent.OnIdentificationNumberChanged(
                                newIdentificationNumber
                            )
                        )
                    },
                    label = {
                        Text(text = "Identification Number")
                    },
                    placeholder = {
                        Text(text = "example: 8020190088")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.name,
                    onValueChange = { newStudentName ->
                        onEvent(AddStudentScreenEvent.OnStudentNameChanged(newStudentName))
                    },
                    label = {
                        Text(text = "Name")
                    },
                    placeholder = {
                        Text(text = "example: John Doe")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = if (state.studentBirthDate == 0.toLong()) {
                            ""
                        } else {
                            Date(state.studentBirthDate).toDateStandard()
                        },
                        readOnly = true,
                        onValueChange = {},
                        label = {
                            Text(text = "Birth Date")
                        },
                        placeholder = {
                            Text(text = "example: 22 April 2002")
                        },
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { onEvent(AddStudentScreenEvent.OnDatePickerToggled) }) {
                        Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                    }
                }
                OutlinedTextField(
                    value = state.address,
                    onValueChange = { newAddress ->
                        onEvent(AddStudentScreenEvent.OnStudentAddressChanged(newAddress))
                    },
                    label = {
                        Text(text = "Address")
                    },
                    placeholder = {
                        Text(text = "example: Jl. Sudirman no.13")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 8.dp
                        )
                ) {
                    Text(
                        text = "Gender",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(
                            start = 8.dp,
                        )
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = !state.isWoman,
                                onClick = { onEvent(AddStudentScreenEvent.OnGenderChanged(false)) })
                            Text(text = "Man")
                        }
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.isWoman,
                                onClick = { onEvent(AddStudentScreenEvent.OnGenderChanged(true)) })
                            Text(text = "Woman")
                        }
                    }


                    val datePickerState = rememberDatePickerState()
                    AnimatedVisibility(visible = state.isDatePickerOpened) {
                        DatePickerDialog(
                            onDismissRequest = {
                                onEvent(AddStudentScreenEvent.OnDatePickerToggled)
                            },
                            confirmButton = {
                                TextButton(onClick = {
                                    datePickerState.selectedDateMillis?.let {
                                        onEvent(AddStudentScreenEvent.OnStudentBirthDateChanged(it))
                                        onEvent(AddStudentScreenEvent.OnDatePickerToggled)
                                    }
                                }) {
                                    Text(text = "Confirm Date")
                                }
                            },
                            dismissButton = {},
                            content = {
                                DatePicker(state = datePickerState)
                            })
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Preview Add Student Screen Day Mode",
    device = "id:pixel_7",
    showBackground = true
)
@Preview(
    name = "Preview Add Student Screen Dark Mode",
    device = "id:pixel_7",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewDashboardScreen() {
    KampuskuTheme {
        AddStudentScreen(
            state = AddStudentScreenState(),
            event = Channel(),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}