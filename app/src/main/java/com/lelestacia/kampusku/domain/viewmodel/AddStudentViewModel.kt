package com.lelestacia.kampusku.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.domain.repository.StudentRepository
import com.lelestacia.kampusku.ui.screen.student.add.AddStudentScreenEvent
import com.lelestacia.kampusku.ui.screen.student.add.AddStudentScreenState
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
data class AddStudentViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _addStudentScreenState: MutableStateFlow<AddStudentScreenState> =
        MutableStateFlow(AddStudentScreenState())
    val addStudentScreenState: StateFlow<AddStudentScreenState> =
        _addStudentScreenState.asStateFlow()

    val eventMessage: Channel<UiText> = Channel()

    fun onEvent(event: AddStudentScreenEvent) {
        when (event) {
            is AddStudentScreenEvent.OnIdentificationNumberChanged -> {
                _addStudentScreenState.update { currentState ->
                    currentState.copy(
                        identificationNumber = event.newIdentificationNumber
                    )
                }
            }

            is AddStudentScreenEvent.OnStudentNameChanged -> {
                _addStudentScreenState.update { currentState ->
                    currentState.copy(
                        name = event.newStudentName
                    )
                }
            }

            is AddStudentScreenEvent.OnStudentBirthDateChanged -> {
                _addStudentScreenState.update { currentState ->
                    currentState.copy(
                        studentBirthDate = event.newBirthDate
                    )
                }
            }

            is AddStudentScreenEvent.OnStudentAddressChanged -> {
                _addStudentScreenState.update { currentState ->
                    currentState.copy(
                        address = event.newStudentAddress
                    )
                }
            }

            is AddStudentScreenEvent.OnStudentPhotoUrlChanged -> {
                _addStudentScreenState.update { currentState ->
                    currentState.copy(
                        photoUrl = event.newStudentPhotoUrl
                    )
                }
            }

            is AddStudentScreenEvent.OnGenderChanged -> {
                _addStudentScreenState.update { currentState ->
                    currentState.copy(
                        isWoman = event.newGender
                    )
                }
            }

            AddStudentScreenEvent.OnDatePickerToggled -> {
                _addStudentScreenState.update { currentState ->
                    currentState.copy(
                        isDatePickerOpened = !currentState.isDatePickerOpened
                    )
                }
            }

            AddStudentScreenEvent.OnSaveButtonPressed -> {
                addStudent()
            }
        }
    }

    private fun addStudent() = viewModelScope.launch {
        val student = StudentFirebaseModel(
            identificationNumber = _addStudentScreenState.value.identificationNumber,
            name = _addStudentScreenState.value.name,
            studentBirthDate = _addStudentScreenState.value.studentBirthDate,
            address = _addStudentScreenState.value.address,
            photoUrl = _addStudentScreenState.value.photoUrl.toString(),
            isWoman = _addStudentScreenState.value.isWoman
        )

        repository.addStudent(student).collectLatest { result ->

            _addStudentScreenState.update { currentState ->
                currentState.copy(
                    isLoading = result is DataState.Loading
                )
            }

            when (result) {
                is DataState.Error -> {
                    eventMessage.send(result.errorMessage)
                }

                is DataState.Success -> {
                    eventMessage.send(UiText.StringResource(R.string.student_added, listOf()))
                }

                else -> Unit
            }
        }
    }
}