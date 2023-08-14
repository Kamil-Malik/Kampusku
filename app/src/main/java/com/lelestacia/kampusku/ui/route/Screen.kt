package com.lelestacia.kampusku.ui.route

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object SignIn : Screen("login")
    object Dashboard : Screen("dashboard")
    object Information : Screen("information")
    object DetailStudent : Screen("detail/{student}") {
        fun createRoute(student: String) : String {
            return this.route.replace(
                oldValue = "{student}",
                newValue = student
            )
        }
    }
    object AddStudent : Screen("add")
    object ListStudent : Screen("list")
}

