package com.lelestacia.kampusku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import com.lelestacia.kampusku.domain.viewmodel.AddStudentViewModel
import com.lelestacia.kampusku.domain.viewmodel.SignInViewModel
import com.lelestacia.kampusku.ui.route.Screen
import com.lelestacia.kampusku.ui.screen.add.AddStudentScreen
import com.lelestacia.kampusku.ui.screen.dashboard.DashboardScreen
import com.lelestacia.kampusku.ui.screen.dashboard.DashboardScreenEvent
import com.lelestacia.kampusku.ui.screen.information.InformationScreen
import com.lelestacia.kampusku.ui.screen.login.SignInScreen
import com.lelestacia.kampusku.ui.screen.splash.SplashScreen
import com.lelestacia.kampusku.ui.theme.KampuskuTheme
import com.lelestacia.kampusku.util.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KampuskuTheme {
                val navController: NavHostController = rememberNavController()
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

                        composable(Screen.Login.route) {
                            val vm = hiltViewModel<SignInViewModel>()
                            val state by vm.signInScreenState.collectAsState()
                            val result by vm.signInScreenResult.collectAsState()

                            LaunchedEffect(Unit) {
                                vm.userEvent.receiveAsFlow().collectLatest { user ->
                                    if (user != null) {
                                        navController.navigate(Screen.Dashboard.route) {
                                            popUpTo(Screen.Login.route) {
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
                                    popUpTo(Screen.Login.route) {
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
                                    DashboardScreenEvent.OnShowDataClicked -> Unit
                                }
                            })
                        }

                        composable(Screen.Information.route) {
                            InformationScreen {
                                navController.popBackStack()
                            }
                        }

                        composable(Screen.AddStudent.route) {
                            val vm = hiltViewModel<AddStudentViewModel>()
                            val state by vm.addStudentScreenState.collectAsState()
                            AddStudentScreen(
                                state = state,
                                onEvent = vm::onEvent,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}