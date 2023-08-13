package com.lelestacia.kampusku.domain.repository

import com.google.firebase.auth.FirebaseUser
import com.lelestacia.kampusku.util.DataState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signInWithEmailAndPassword(email: String, password: String): Flow<DataState<Boolean>>
    fun getCurrentUser(): FirebaseUser?
}