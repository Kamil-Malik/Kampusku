package com.lelestacia.kampusku.ui.screen.student.list

import com.lelestacia.kampusku.data.model.StudentFirebaseModel

data class ListStudentScreenState(
    val selectedStudent: StudentFirebaseModel? = null,
    val isDialogOpened: Boolean = false,
    val isLoading: Boolean = false
)