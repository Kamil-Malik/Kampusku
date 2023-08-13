package com.lelestacia.kampusku.ui.route

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Information : Screen("information")
    object AddStudent : Screen("add_student")
}

