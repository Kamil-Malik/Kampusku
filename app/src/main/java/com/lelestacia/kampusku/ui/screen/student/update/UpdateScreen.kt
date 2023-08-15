package com.lelestacia.kampusku.ui.screen.student.update

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.ui.component.button.DeleteImageCircleButton
import com.lelestacia.kampusku.ui.component.topbar.KampuskuTopBar
import com.lelestacia.kampusku.ui.screen.util.LoadingScreen
import com.lelestacia.kampusku.util.UiText
import com.parassidhu.simpledate.toDateStandard
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import java.util.Date

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun UpdateStudentScreen(
    state: UpdateStudentScreenState,
    onEvent: (UpdateStudentScreenEvent) -> Unit,
    onNavigateBack: () -> Unit,
    eventChannel: Channel<UiText>
) {
    val context = LocalContext.current
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }

    val galleryContract = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { newImageUri ->
            if (newImageUri != null) {
                onEvent(UpdateStudentScreenEvent.OnStudentPhotoUrlChanged(newImageUri))
            }
        })

    val galleryPermission = rememberPermissionState(
        permission =
        if (Build.VERSION.SDK_INT > 32) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    LaunchedEffect(key1 = Unit) {
        eventChannel.receiveAsFlow().collectLatest { uiText ->
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
                    title = stringResource(R.string.topbar_title_add_student),
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
                    onEvent(UpdateStudentScreenEvent.OnSaveButtonPressed)
                }) {
                    Icon(
                        imageVector = Icons.Default.Save,
                        contentDescription = null
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .matchParentSize()
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
                            targetState = state.photoUrl.toString().isBlank(),
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
                                                    UpdateStudentScreenEvent.OnStudentPhotoUrlChanged(
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
                                UpdateStudentScreenEvent.OnIdentificationNumberChanged(
                                    newIdentificationNumber
                                )
                            )
                        },
                        label = {
                            Text(text = stringResource(id = R.string.label_identification_number))
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
                            onEvent(UpdateStudentScreenEvent.OnStudentNameChanged(newStudentName))
                        },
                        label = {
                            Text(text = stringResource(id = R.string.label_name))
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
                                Text(text = stringResource(id = R.string.label_birth_date))
                            },
                            placeholder = {
                                Text(text = "example: 22 April 2002")
                            },
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { onEvent(UpdateStudentScreenEvent.OnDatePickerToggled) }) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                        }
                    }
                    OutlinedTextField(
                        value = state.address,
                        onValueChange = { newAddress ->
                            onEvent(UpdateStudentScreenEvent.OnStudentAddressChanged(newAddress))
                        },
                        label = {
                            Text(text = stringResource(id = R.string.label_address))
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
                            text = stringResource(id = R.string.label_gender),
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
                                    onClick = {
                                        onEvent(
                                            UpdateStudentScreenEvent.OnGenderChanged(
                                                false
                                            )
                                        )
                                    })
                                Text(text = stringResource(id = R.string.gender_man))
                            }
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = state.isWoman,
                                    onClick = {
                                        onEvent(
                                            UpdateStudentScreenEvent.OnGenderChanged(
                                                true
                                            )
                                        )
                                    })
                                Text(text = stringResource(id = R.string.gender_woman))
                            }
                        }


                        val datePickerState = rememberDatePickerState()
                        AnimatedVisibility(visible = state.isDatePickerOpened) {
                            DatePickerDialog(
                                onDismissRequest = {
                                    onEvent(UpdateStudentScreenEvent.OnDatePickerToggled)
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        datePickerState.selectedDateMillis?.let {
                                            onEvent(
                                                UpdateStudentScreenEvent.OnStudentBirthDateChanged(
                                                    it
                                                )
                                            )
                                            onEvent(UpdateStudentScreenEvent.OnDatePickerToggled)
                                        }
                                    }) {
                                        Text(text = stringResource(id = R.string.button_confirm_date))
                                    }
                                },
                                dismissButton = {},
                                content = {
                                    DatePicker(state = datePickerState)
                                })
                        }
                    }
                }
                AnimatedVisibility(visible = state.isLoading) {
                    LoadingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(0.5F))
                    )
                }
            }
        }
    }
}