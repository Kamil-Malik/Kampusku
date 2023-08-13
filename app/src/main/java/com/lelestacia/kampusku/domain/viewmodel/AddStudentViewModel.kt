package com.lelestacia.kampusku.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.lelestacia.kampusku.domain.repository.StudentRepository
import com.lelestacia.kampusku.ui.screen.add.AddStudentScreenEvent
import com.lelestacia.kampusku.ui.screen.add.AddStudentScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
data class AddStudentViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _addStudentScreenState: MutableStateFlow<AddStudentScreenState> =
        MutableStateFlow(AddStudentScreenState())
    val addStudentScreenState: StateFlow<AddStudentScreenState> =
        _addStudentScreenState.asStateFlow()

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
        }
    }
}