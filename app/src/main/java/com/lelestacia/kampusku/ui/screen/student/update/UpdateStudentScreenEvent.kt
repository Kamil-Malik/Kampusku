package com.lelestacia.kampusku.ui.screen.student.update

import android.net.Uri
import com.lelestacia.kampusku.data.model.StudentFirebaseModel

sealed class UpdateStudentScreenEvent {
    data class OnSelectedStudentChanged(val student: StudentFirebaseModel) :
        UpdateStudentScreenEvent()

    data class OnIdentificationNumberChanged(val newIdentificationNumber: String) :
        UpdateStudentScreenEvent()

    data class OnStudentNameChanged(val newStudentName: String) : UpdateStudentScreenEvent()
    data class OnStudentBirthDateChanged(val newBirthDate: Long) : UpdateStudentScreenEvent()
    data class OnStudentAddressChanged(val newStudentAddress: String) : UpdateStudentScreenEvent()
    data class OnStudentPhotoUrlChanged(val newStudentPhotoUrl: Uri) : UpdateStudentScreenEvent()
    data class OnGenderChanged(val newGender: Boolean) : UpdateStudentScreenEvent()
    object OnDatePickerToggled : UpdateStudentScreenEvent()
    object OnSaveButtonPressed : UpdateStudentScreenEvent()
}
