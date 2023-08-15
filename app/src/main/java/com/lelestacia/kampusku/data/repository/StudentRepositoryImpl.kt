package com.lelestacia.kampusku.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.domain.repository.StudentRepository
import com.lelestacia.kampusku.util.DataState
import com.lelestacia.kampusku.util.UiText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.net.UnknownHostException
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class StudentRepositoryImpl @Inject constructor(
    private val docsService: FirebaseFirestore,
    private val imageService: FirebaseStorage,
    private val ioDispatcher: CoroutineContext
) : StudentRepository {

    override fun addStudent(student: StudentFirebaseModel): Flow<DataState<Boolean>> {
        return flow<DataState<Boolean>> {
            val storageReference: StorageReference = imageService
                .reference
                .child("$STUDENT_FOLDER/${student.name} - ${student.identificationNumber}.jpg")

            if (student.photoUrl.isNotBlank()) {
                storageReference.putFile(Uri.parse(student.photoUrl)).await()
            }

            val uri: Uri = if (student.photoUrl.isNotBlank()) {
                storageReference.downloadUrl.await()
            } else {
                Uri.EMPTY
            }

            val newID: String = docsService.collection(STUDENT_FOLDER).document().id
            val newStudent: StudentFirebaseModel = student.copy(
                id = newID,
                photoUrl = uri.toString()
            )
            docsService
                .collection(STUDENT_FOLDER)
                .document(newID)
                .set(newStudent)
                .await()
            emit(DataState.Success(true))
        }.onStart {
            emit(DataState.Loading)
        }.catch { t ->
            if (t is UnknownHostException || t is IOException) {
                emit(
                    DataState.Error(
                        errorMessage = UiText.StringResource(
                            R.string.no_internet,
                            emptyList()
                        )
                    )
                )
            } else {
                emit(DataState.Error(errorMessage = UiText.DynamicString(t.message.orEmpty())))
            }
        }.flowOn(ioDispatcher)
    }

    override fun getStudent(): Flow<DataState<List<StudentFirebaseModel>>> {
        return callbackFlow<DataState<List<StudentFirebaseModel>>> {
            val listener =
                EventListener<QuerySnapshot> { value, error ->
                    if (error != null) {
                        close(error)
                    } else {
                        val data: MutableList<StudentFirebaseModel>? =
                            value?.toObjects(StudentFirebaseModel::class.java)
                        trySend(DataState.Success((data ?: emptyList()).toList()))
                    }
                }
            docsService
                .collection(STUDENT_FOLDER)
                .addSnapshotListener(listener)
            awaitClose()
        }.onStart {
            emit(DataState.Loading)
        }.catch { t ->
            if (t is UnknownHostException || t is IOException) {
                emit(
                    DataState.Error(
                        errorMessage = UiText.StringResource(
                            R.string.no_internet,
                            listOf()
                        )
                    )
                )
            } else {
                emit(DataState.Error(errorMessage = UiText.DynamicString(t.message.orEmpty())))
            }
        }.flowOn(ioDispatcher)
    }

    override fun updateStudent(
        oldStudentData: StudentFirebaseModel,
        newStudentData: StudentFirebaseModel
    ): Flow<DataState<Boolean>> {
        return flow<DataState<Boolean>> {

            val newStorageReference: StorageReference = imageService
                .reference
                .child("$STUDENT_FOLDER/${newStudentData.name} - ${newStudentData.identificationNumber}.jpg")

            if (newStudentData.photoUrl.isNotBlank()) {
                newStorageReference.putFile(Uri.parse(newStudentData.photoUrl)).await()
            }

            //  Get new reference url
            val uri: Uri = if (newStudentData.photoUrl.isNotBlank()) {
                newStorageReference.downloadUrl.await()
            } else {
                Uri.EMPTY
            }

            //  Replace with NewData
            docsService
                .collection(STUDENT_FOLDER)
                .document(oldStudentData.id)
                .set(
                    newStudentData.copy(
                        photoUrl = uri.toString()
                    )
                ).await()
            emit(DataState.Success(true))
        }.onStart {
            emit(DataState.Loading)
        }.catch { t ->
            if (t is UnknownHostException || t is IOException) {
                emit(
                    DataState.Error(
                        errorMessage = UiText.StringResource(
                            R.string.no_internet,
                            emptyList()
                        )
                    )
                )
            } else {
                emit(DataState.Error(errorMessage = UiText.DynamicString(t.message.orEmpty())))
            }
        }.flowOn(ioDispatcher)
    }

    override fun deleteStudent(student: StudentFirebaseModel): Flow<DataState<Boolean>> {
        return flow<DataState<Boolean>> {
            val storageReference: StorageReference = imageService
                .reference
                .child("$STUDENT_FOLDER/${student.name} - ${student.identificationNumber}.jpg")
            val listOfJobs: MutableList<Deferred<Task<Void>>> = mutableListOf()
            listOfJobs.add(
                CoroutineScope(ioDispatcher).async {
                    docsService.collection(STUDENT_FOLDER).document(student.id).delete()
                }
            )
            listOfJobs.add(
                CoroutineScope(ioDispatcher).async {
                    storageReference.delete()
                }
            )
            listOfJobs.awaitAll()
            emit(DataState.Success(true))
        }.onStart {
            emit(DataState.Loading)
        }.catch { t ->
            if (t is UnknownHostException || t is IOException) {
                emit(
                    DataState.Error(
                        errorMessage = UiText.StringResource(
                            R.string.no_internet,
                            emptyList()
                        )
                    )
                )
            } else {
                emit(DataState.Error(errorMessage = UiText.DynamicString(t.message.orEmpty())))
            }
        }.flowOn(ioDispatcher)
    }

    companion object {
        private const val STUDENT_FOLDER = "student"
    }
}