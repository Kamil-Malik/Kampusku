package com.lelestacia.kampusku.ui.screen.login

data class SignInScreenState(
    val email: String = "",
    val password: String = "",
    val shouldPasswordBeVisible: Boolean = false,
    val isNowLoading: Boolean = false
)