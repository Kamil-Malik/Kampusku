package com.lelestacia.kampusku.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.domain.repository.StudentRepository
import com.lelestacia.kampusku.util.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class StudentRepositoryImpl @Inject constructor(
    private val docsService: FirebaseFirestore,
    private val imageService: FirebaseStorage,
    private val ioDispatcher: CoroutineContext
) : StudentRepository {

    override fun addStudent(student: StudentFirebaseModel): Flow<DataState<Boolean>> {
        return flow {

        }
    }
}