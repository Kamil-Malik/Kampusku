package com.lelestacia.kampusku.ui.screen.student.update

import android.net.Uri
import com.lelestacia.kampusku.data.model.StudentFirebaseModel

data class UpdateStudentScreenState(
    val currentStudentData: StudentFirebaseModel? = null,
    val identificationNumber: String = "",
    val name: String = "",
    val studentBirthDate: Long = 0,
    val isWoman: Boolean = false,
    val address: String = "",
    val photoUrl: Uri = Uri.EMPTY,
    val isDatePickerOpened: Boolean = false,
    val isLoading: Boolean = false
)