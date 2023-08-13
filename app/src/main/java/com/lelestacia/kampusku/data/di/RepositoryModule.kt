package com.lelestacia.kampusku.data.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.lelestacia.kampusku.data.repository.AuthRepositoryImpl
import com.lelestacia.kampusku.data.repository.StudentRepositoryImpl
import com.lelestacia.kampusku.domain.repository.AuthRepository
import com.lelestacia.kampusku.domain.repository.StudentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideAuthService(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideDocsService(): FirebaseFirestore {
        return Firebase.firestore
    }

    @Provides
    @Singleton
    fun provideImageService(): FirebaseStorage {
        return Firebase.storage
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        authService: FirebaseAuth,
        ioDispatcher: CoroutineContext
    ): AuthRepository {
        return AuthRepositoryImpl(
            authService = authService,
            ioDispatcher = ioDispatcher
        )
    }

    @Provides
    @Singleton
    fun provideStudentRepository(
        docsService: FirebaseFirestore,
        imageService: FirebaseStorage,
        ioDispatcher: CoroutineContext
    ): StudentRepository {
        return StudentRepositoryImpl(
            docsService = docsService,
            imageService = imageService,
            ioDispatcher = ioDispatcher
        )
    }
}