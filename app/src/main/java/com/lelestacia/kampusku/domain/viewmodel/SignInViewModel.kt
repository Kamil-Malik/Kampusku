package com.lelestacia.kampusku.domain.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.lelestacia.kampusku.domain.repository.AuthRepository
import com.lelestacia.kampusku.ui.screen.login.SignInScreenEvent
import com.lelestacia.kampusku.ui.screen.login.SignInScreenState
import com.lelestacia.kampusku.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _signInScreenState: MutableStateFlow<SignInScreenState> =
        MutableStateFlow(SignInScreenState())
    val signInScreenState: StateFlow<SignInScreenState> =
        _signInScreenState.asStateFlow()

    private val _signInScreenResult: MutableStateFlow<DataState<Boolean>> =
        MutableStateFlow(DataState.None)
    val signInScreenResult: StateFlow<DataState<Boolean>> =
        _signInScreenResult.asStateFlow()

    val userEvent: Channel<FirebaseUser?> = Channel()
    val event: Channel<String> = Channel()

    fun onEvent(event: SignInScreenEvent) {
        when (event) {
            is SignInScreenEvent.OnEmailChanged -> {
                _signInScreenState.update {
                    it.copy(
                        email = event.newEmail
                    )
                }
            }

            is SignInScreenEvent.OnPasswordChanged -> {
                _signInScreenState.update {
                    it.copy(
                        password = event.newPassword
                    )
                }
            }

            SignInScreenEvent.OnPasswordVisibilityToggled -> {
                _signInScreenState.update { currentState ->
                    currentState.copy(
                        shouldPasswordBeVisible = !currentState.shouldPasswordBeVisible
                    )
                }
            }

            SignInScreenEvent.OnLogin -> signInWithEmailAndPassword()
        }
    }

    private fun signInWithEmailAndPassword() = viewModelScope.launch {
        viewModelScope.launch {
            repository.signInWithEmailAndPassword(
                email = signInScreenState.value.email,
                password = signInScreenState.value.password
            ).collectLatest { result ->
                Log.d(TAG, "signInWithEmailAndPassword: Result is ${result.javaClass.simpleName}")

                _signInScreenState.update {
                    it.copy(
                        isNowLoading = result is DataState.Loading
                    )
                }

                when (result) {
                    is DataState.Error -> event.send(result.errorMessage)
                    DataState.Loading -> _signInScreenResult.update { result }
                    DataState.None -> _signInScreenResult.update { result }
                    is DataState.Success -> _signInScreenResult.update { result }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        event.close()
        userEvent.close()
    }

    init {
        viewModelScope.launch {
            userEvent.send(repository.getCurrentUser())
        }
    }
}