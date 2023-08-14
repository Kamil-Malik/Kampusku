package com.lelestacia.kampusku.ui.screen.student.add

import android.net.Uri

data class AddStudentScreenState(
    val identificationNumber: String = "",
    val name: String = "",
    val studentBirthDate: Long = 0,
    val isWoman: Boolean = false,
    val address: String = "",
    val photoUrl: Uri = Uri.EMPTY,
    val isDatePickerOpened: Boolean = false,
    val isLoading: Boolean = false
)