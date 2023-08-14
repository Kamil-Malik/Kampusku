package com.lelestacia.kampusku.ui.screen.student.list

import com.lelestacia.kampusku.data.model.StudentFirebaseModel

sealed class ListStudentScreenEvent {
    data class OnStudentClicked(val student: StudentFirebaseModel) : ListStudentScreenEvent()
    object OnAlertDialogToggled : ListStudentScreenEvent()
    object OnDeleteStudentClicked: ListStudentScreenEvent()
}