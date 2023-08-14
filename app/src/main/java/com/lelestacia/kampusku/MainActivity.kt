package com.lelestacia.kampusku

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.gson.Gson
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.domain.viewmodel.AddStudentViewModel
import com.lelestacia.kampusku.domain.viewmodel.ListStudentViewModel
import com.lelestacia.kampusku.domain.viewmodel.SignInViewModel
import com.lelestacia.kampusku.ui.component.topbar.KampuskuTopBar
import com.lelestacia.kampusku.ui.navigation.StudentParcelableNavType
import com.lelestacia.kampusku.ui.route.Screen
import com.lelestacia.kampusku.ui.screen.student.add.AddStudentScreen
import com.lelestacia.kampusku.ui.screen.dashboard.DashboardScreen
import com.lelestacia.kampusku.ui.screen.dashboard.DashboardScreenEvent
import com.lelestacia.kampusku.ui.screen.information.InformationScreen
import com.lelestacia.kampusku.ui.screen.student.list.ListStudentScreen
import com.lelestacia.kampusku.ui.screen.student.list.ListStudentScreenEvent
import com.lelestacia.kampusku.ui.screen.signin.SignInScreen
import com.lelestacia.kampusku.ui.screen.splash.SplashScreen
import com.lelestacia.kampusku.ui.screen.student.detail.DetailStudentScreen
import com.lelestacia.kampusku.ui.theme.KampuskuTheme
import com.lelestacia.kampusku.util.DataState
import com.lelestacia.kampusku.util.parcelable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KampuskuTheme {
                val navController: NavHostController = rememberNavController()
                val uiController = rememberSystemUiController()
                val backgroundColor = MaterialTheme.colorScheme.background
                val useDarkIcons = !isSystemInDarkTheme()

                LaunchedEffect(uiController, useDarkIcons) {
                    uiController.setSystemBarsColor(
                        color = backgroundColor,
                        darkIcons = useDarkIcons
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route
                    ) {
                        composable(Screen.Splash.route) {
                            SplashScreen(onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo(Screen.Splash.route) {
                                        inclusive = true
                                    }
                                }
                            })
                        }

                        composable(Screen.SignIn.route) {
                            val vm = hiltViewModel<SignInViewModel>()
                            val state by vm.signInScreenState.collectAsState()
                            val result by vm.signInScreenResult.collectAsState()

                            LaunchedEffect(Unit) {
                                vm.userEvent.receiveAsFlow().collectLatest { user ->
                                    if (user != null) {
                                        navController.navigate(Screen.Dashboard.route) {
                                            popUpTo(Screen.SignIn.route) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                }
                            }

                            SignInScreen(
                                state = state,
                                onEvent = vm::onEvent,
                                eventMessage = vm.event
                            )

                            when (result) {
                                is DataState.Success -> navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.SignIn.route) {
                                        inclusive = true
                                    }
                                }

                                else -> return@composable
                            }
                        }

                        composable(Screen.Dashboard.route) {
                            DashboardScreen(onEvent = { event ->
                                when (event) {
                                    DashboardScreenEvent.OnInformationClicked -> {
                                        navController.navigate(Screen.Information.route)
                                    }

                                    DashboardScreenEvent.OnInputDataClicked -> {
                                        navController.navigate(Screen.AddStudent.route)
                                    }

                                    DashboardScreenEvent.OnShowDataClicked -> {
                                        navController.navigate(Screen.ListStudent.route)
                                    }
                                }
                            })
                        }

                        composable(
                            route = Screen.Information.route,
                            enterTransition = {
                                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                            },
                            popExitTransition = {
                                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                            }
                        ) {
                            InformationScreen {
                                navController.popBackStack()
                            }
                        }

                        composable(
                            route = Screen.AddStudent.route,
                            enterTransition = {
                                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                            },
                            popExitTransition = {
                                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                            }
                        ) {
                            val vm = hiltViewModel<AddStudentViewModel>()
                            val state by vm.addStudentScreenState.collectAsState()
                            AddStudentScreen(
                                state = state,
                                event = vm.eventMessage,
                                onEvent = vm::onEvent,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.ListStudent.route,
                            enterTransition = {
                                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                            },
                            popEnterTransition = {
                                fadeIn()
                            },
                            popExitTransition = {
                                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                            },
                            exitTransition = {
                                fadeOut()
                            }
                        ) {
                            val context = LocalContext.current
                            val vm = hiltViewModel<ListStudentViewModel>()
                            val studentsResult by vm.studentsResult.collectAsState()
                            val state by vm.listStudentScreenState.collectAsState()
                            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

                            val snackbarHostState by remember {
                                mutableStateOf(SnackbarHostState())
                            }

                            LaunchedEffect(key1 = Unit) {
                                vm.eventChannel.receiveAsFlow().collectLatest { uiText ->
                                    snackbarHostState.showSnackbar(
                                        message = uiText.asString(context),
                                        duration = SnackbarDuration.Short
                                    )
                                }
                            }

                            Scaffold(
                                topBar = {
                                    KampuskuTopBar(
                                        onNavigateBack = {
                                            navController.popBackStack()
                                        },
                                        title = "Students Data",
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
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(paddingValues)
                                        ) {
                                            Text(
                                                text = (studentsResult as DataState.Error).errorMessage.asString(),
                                                style = MaterialTheme.typography.titleLarge.copy(
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }

                                    DataState.Loading -> {
                                        Box(
                                            contentAlignment = Alignment.Center,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(paddingValues)
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                    DataState.None -> Unit
                                    is DataState.Success -> {
                                        ListStudentScreen(
                                            students = (studentsResult as DataState.Success<List<StudentFirebaseModel>>).data,
                                            onEvent = vm::onEvent,
                                            modifier = Modifier.padding(paddingValues)
                                        )
                                    }
                                }

                                if (state.isDialogOpened) {
                                    AlertDialog(
                                        onDismissRequest = {
                                            vm.onEvent(ListStudentScreenEvent.OnAlertDialogToggled)
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
                                                        val json =
                                                            Uri.encode(Gson().toJson(state.selectedStudent))
                                                        navController.navigate(
                                                            Screen.DetailStudent.createRoute(
                                                                json
                                                            )
                                                        )
                                                        vm.onEvent(ListStudentScreenEvent.OnAlertDialogToggled)
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
                                                    onClick = { /*TODO*/ },
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
                                                        vm.onEvent(ListStudentScreenEvent.OnDeleteStudentClicked)
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

                        composable(
                            route = Screen.DetailStudent.route,
                            enterTransition = {
                                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start)
                            },
                            popExitTransition = {
                                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End)
                            },
                            arguments = listOf(
                                navArgument("student") {
                                    type = StudentParcelableNavType()
                                }
                            )
                        ) {
                            val student = it.arguments
                                ?.parcelable<StudentFirebaseModel>("student")
                            student?.let { existStudent ->
                                DetailStudentScreen(
                                    student = existStudent,
                                    onNavigateBack = {
                                        navController.popBackStack()
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}