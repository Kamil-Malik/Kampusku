package com.lelestacia.kampusku.ui.screen.student.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.ui.component.topbar.KampuskuTopBar
import com.lelestacia.kampusku.ui.screen.util.ErrorScreen
import com.lelestacia.kampusku.ui.screen.util.LoadingScreen
import com.lelestacia.kampusku.util.DataState
import com.lelestacia.kampusku.util.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListStudentScreen(
    state: ListStudentScreenState,
    studentsResult: DataState<List<StudentFirebaseModel>>,
    onEvent: (ListStudentScreenEvent) -> Unit,
    onNavigateToDetail: () -> Unit,
    onNavigateToUpdate: () -> Unit,
    onNavigateBack: () -> Unit,
    eventChannel: Channel<UiText>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val snackbarHostState by remember {
        mutableStateOf(SnackbarHostState())
    }

    LaunchedEffect(key1 = Unit) {
        scope.launch {
            eventChannel.receiveAsFlow().collectLatest { uiText ->
                snackbarHostState.showSnackbar(
                    message = uiText.asString(context),
                    duration = SnackbarDuration.Short
                )
            }
        }
    }

    Scaffold(
        topBar = {
            KampuskuTopBar(
                onNavigateBack = onNavigateBack,
                title = stringResource(id = R.string.topbar_title_list_student),
                scrollBehavior = scrollBehavior,
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            )
        },
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->
        when (studentsResult) {
            is DataState.Error -> {
                ErrorScreen(
                    errorMessage = studentsResult.errorMessage.asString(),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            DataState.Loading -> {
                LoadingScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }

            DataState.None -> Unit
            is DataState.Success -> {
                ListStudentScreenContent(
                    students = studentsResult.data,
                    onEvent = onEvent,
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }

        if (state.isDialogOpened) {
            AlertDialog(
                onDismissRequest = {
                    onEvent(ListStudentScreenEvent.OnAlertDialogToggled)
                },
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .clip(RoundedCornerShape(percent = 15))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Options",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        TextButton(
                            onClick = {
                                onNavigateToDetail()
                                onEvent(ListStudentScreenEvent.OnAlertDialogToggled)
                            },
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.dialog_view_data))
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(
                            onClick = {
                                onNavigateToUpdate()
                                onEvent(ListStudentScreenEvent.OnAlertDialogToggled)
                            },
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.dialog_edit_data))
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        TextButton(
                            onClick = {
                                onEvent(ListStudentScreenEvent.OnDeleteStudentClicked)
                            },
                            shape = RoundedCornerShape(percent = 15),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.dialog_delete_data))
                        }
                    }
                }
            }
        }
    }
}