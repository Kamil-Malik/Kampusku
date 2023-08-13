package com.lelestacia.kampusku.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lelestacia.kampusku.domain.repository.AuthRepository
import com.lelestacia.kampusku.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class AuthRepositoryImpl @Inject constructor(
    private val authService: FirebaseAuth,
    private val ioDispatcher: CoroutineContext
) : AuthRepository {

    override fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Flow<DataState<Boolean>> {
        return flow<DataState<Boolean>> {
            authService.signInWithEmailAndPassword(
                email, password
            ).await()
            emit(DataState.Success(true))
        }.onStart {
            emit(DataState.Loading)
        }.catch { t ->
            emit(DataState.Error(t.message.orEmpty()))
        }.flowOn(ioDispatcher)
    }

    override fun getCurrentUser(): FirebaseUser? {
        return authService.currentUser
    }
}