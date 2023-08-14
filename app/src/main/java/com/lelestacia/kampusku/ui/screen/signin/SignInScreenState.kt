package com.lelestacia.kampusku.ui.screen.signin

data class SignInScreenState(
    val email: String = "",
    val password: String = "",
    val shouldPasswordBeVisible: Boolean = false,
    val isNowLoading: Boolean = false
)