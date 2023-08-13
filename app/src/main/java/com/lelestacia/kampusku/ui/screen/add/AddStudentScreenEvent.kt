package com.lelestacia.kampusku.ui.screen.add

import android.net.Uri

sealed class AddStudentScreenEvent {
    data class OnIdentificationNumberChanged(val newIdentificationNumber: String) :
        AddStudentScreenEvent()

    data class OnStudentNameChanged(val newStudentName: String) : AddStudentScreenEvent()
    data class OnStudentBirthDateChanged(val newBirthDate: Long) : AddStudentScreenEvent()
    data class OnStudentAddressChanged(val newStudentAddress: String) : AddStudentScreenEvent()
    data class OnStudentPhotoUrlChanged(val newStudentPhotoUrl: Uri) : AddStudentScreenEvent()
    data class OnGenderChanged(val newGender: Boolean) : AddStudentScreenEvent()
    object OnDatePickerToggled: AddStudentScreenEvent()
}