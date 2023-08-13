package com.lelestacia.kampusku.ui.screen.login

sealed class SignInScreenEvent {
    data class OnEmailChanged(val newEmail: String) : SignInScreenEvent()
    data class OnPasswordChanged(val newPassword: String) : SignInScreenEvent()
    object OnPasswordVisibilityToggled: SignInScreenEvent()
    object OnLogin : SignInScreenEvent()
}