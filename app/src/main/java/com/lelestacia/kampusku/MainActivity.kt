package com.lelestacia.kampusku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.domain.viewmodel.AddStudentViewModel
import com.lelestacia.kampusku.domain.viewmodel.ListStudentViewModel
import com.lelestacia.kampusku.domain.viewmodel.SignInViewModel
import com.lelestacia.kampusku.domain.viewmodel.UpdateStudentViewModel
import com.lelestacia.kampusku.ui.navigation.StudentParcelableNavType
import com.lelestacia.kampusku.ui.route.Screen
import com.lelestacia.kampusku.ui.screen.dashboard.DashboardScreen
import com.lelestacia.kampusku.ui.screen.dashboard.DashboardScreenEvent
import com.lelestacia.kampusku.ui.screen.information.InformationScreen
import com.lelestacia.kampusku.ui.screen.signin.SignInScreen
import com.lelestacia.kampusku.ui.screen.splash.SplashScreen
import com.lelestacia.kampusku.ui.screen.student.add.AddStudentScreen
import com.lelestacia.kampusku.ui.screen.student.detail.DetailStudentScreen
import com.lelestacia.kampusku.ui.screen.student.list.ListStudentScreen
import com.lelestacia.kampusku.ui.screen.student.update.UpdateStudentScreen
import com.lelestacia.kampusku.ui.screen.student.update.UpdateStudentScreenEvent
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
                            val vm = hiltViewModel<ListStudentViewModel>()
                            val studentsResult by vm.studentsResult.collectAsState()
                            val state by vm.listStudentScreenState.collectAsState()
                            ListStudentScreen(
                                state = state,
                                studentsResult = studentsResult,
                                onEvent = vm::onEvent,
                                onNavigateToDetail = {
                                    val student = vm.encodeJson()
                                    val route = Screen.DetailStudent
                                        .createRoute(student)
                                    navController.navigate(route)
                                },
                                onNavigateToUpdate = {
                                    val student = vm.encodeJson()
                                    val route = Screen.UpdateStudent
                                        .createRoute(student)
                                    navController.navigate(route)
                                },
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                eventChannel = vm.eventChannel
                            )
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
                            DetailStudentScreen(
                                student = student as StudentFirebaseModel,
                                onNavigateBack = {
                                    navController.popBackStack()
                                })
                        }

                        composable(
                            route = Screen.UpdateStudent.route,
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
                            val vm = hiltViewModel<UpdateStudentViewModel>()

                            LaunchedEffect(key1 = Unit) {
                                vm.onEvent(UpdateStudentScreenEvent.OnSelectedStudentChanged(student as StudentFirebaseModel))
                            }

                            val state by vm.updateStudentScreenState.collectAsState()
                            UpdateStudentScreen(
                                state = state,
                                onEvent = vm::onEvent,
                                onNavigateBack = {
                                    navController.popBackStack()
                                },
                                eventChannel = vm.eventChannel
                            )
                        }
                    }
                }
            }
        }
    }
}