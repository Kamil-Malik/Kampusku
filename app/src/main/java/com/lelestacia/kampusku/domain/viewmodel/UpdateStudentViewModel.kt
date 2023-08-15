package com.lelestacia.kampusku.domain.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.domain.repository.StudentRepository
import com.lelestacia.kampusku.ui.screen.student.update.UpdateStudentScreenEvent
import com.lelestacia.kampusku.ui.screen.student.update.UpdateStudentScreenState
import com.lelestacia.kampusku.util.DataState
import com.lelestacia.kampusku.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateStudentViewModel @Inject constructor(
    private val studentRepository: StudentRepository
) : ViewModel() {

    private val _updateStudentScreenState: MutableStateFlow<UpdateStudentScreenState> =
        MutableStateFlow(UpdateStudentScreenState())
    val updateStudentScreenState: StateFlow<UpdateStudentScreenState> =
        _updateStudentScreenState.asStateFlow()

    val eventChannel: Channel<UiText> = Channel()

    fun onEvent(event: UpdateStudentScreenEvent) {
        when (event) {
            is UpdateStudentScreenEvent.OnSelectedStudentChanged -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        currentStudentData = event.student,
                        identificationNumber = event.student.identificationNumber,
                        name = event.student.name,
                        studentBirthDate = event.student.studentBirthDate,
                        isWoman = event.student.isWoman,
                        address = event.student.address,
                        photoUrl = Uri.parse(event.student.photoUrl),
                    )
                }
            }

            is UpdateStudentScreenEvent.OnIdentificationNumberChanged -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        identificationNumber = event.newIdentificationNumber
                    )
                }
            }

            is UpdateStudentScreenEvent.OnStudentNameChanged -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        name = event.newStudentName
                    )
                }
            }

            is UpdateStudentScreenEvent.OnStudentBirthDateChanged -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        studentBirthDate = event.newBirthDate
                    )
                }
            }

            is UpdateStudentScreenEvent.OnStudentAddressChanged -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        address = event.newStudentAddress
                    )
                }
            }

            is UpdateStudentScreenEvent.OnStudentPhotoUrlChanged -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        photoUrl = event.newStudentPhotoUrl
                    )
                }
            }

            is UpdateStudentScreenEvent.OnGenderChanged -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        isWoman = event.newGender
                    )
                }
            }

            UpdateStudentScreenEvent.OnDatePickerToggled -> {
                _updateStudentScreenState.update { currentState ->
                    currentState.copy(
                        isDatePickerOpened = !currentState.isDatePickerOpened
                    )
                }
            }

            UpdateStudentScreenEvent.OnSaveButtonPressed -> {
                updateStudent()
            }
        }
    }

    private fun updateStudent() = viewModelScope.launch {
        val newStudentData = StudentFirebaseModel(
            identificationNumber = updateStudentScreenState.value.identificationNumber,
            name = updateStudentScreenState.value.name,
            studentBirthDate = updateStudentScreenState.value.studentBirthDate,
            address = updateStudentScreenState.value.address,
            photoUrl = updateStudentScreenState.value.photoUrl.toString(),
            isWoman = updateStudentScreenState.value.isWoman
        )

        studentRepository.updateStudent(
            oldStudentData = updateStudentScreenState.value.currentStudentData as StudentFirebaseModel,
            newStudentData = newStudentData
        ).collectLatest { updateResult ->

            _updateStudentScreenState.update { currentState ->
                currentState.copy(
                    isLoading = updateResult is DataState.Loading
                )
            }

            when (updateResult) {
                is DataState.Error -> eventChannel.send(updateResult.errorMessage)
                is DataState.Success -> eventChannel.send(
                    UiText.StringResource(
                        id = R.string.student_updated,
                        args = emptyList()
                    )
                )

                else -> Unit
            }
        }
    }
}