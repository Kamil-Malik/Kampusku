package com.lelestacia.kampusku.domain.repository

import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.util.DataState
import kotlinx.coroutines.flow.Flow

interface StudentRepository {
    fun addStudent(student: StudentFirebaseModel): Flow<DataState<Boolean>>
    fun getStudent(): Flow<DataState<List<StudentFirebaseModel>>>
    fun updateStudent(oldStudentData: StudentFirebaseModel, newStudentData: StudentFirebaseModel): Flow<DataState<Boolean>>
    fun deleteStudent(student: StudentFirebaseModel): Flow<DataState<Boolean>>
}