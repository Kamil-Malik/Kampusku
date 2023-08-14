package com.lelestacia.kampusku.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.domain.repository.StudentRepository
import com.lelestacia.kampusku.ui.screen.student.list.ListStudentScreenEvent
import com.lelestacia.kampusku.ui.screen.student.list.ListStudentScreenState
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
class ListStudentViewModel @Inject constructor(
    private val repository: StudentRepository
) : ViewModel() {

    private val _studentsResult: MutableStateFlow<DataState<List<StudentFirebaseModel>>> =
        MutableStateFlow(DataState.None)
    val studentsResult: StateFlow<DataState<List<StudentFirebaseModel>>> =
        _studentsResult.asStateFlow()

    private val _listStudentScreenState: MutableStateFlow<ListStudentScreenState> =
        MutableStateFlow(ListStudentScreenState())
    val listStudentScreenState: StateFlow<ListStudentScreenState> =
        _listStudentScreenState.asStateFlow()

    val eventChannel: Channel<UiText> = Channel()

    fun onEvent(event: ListStudentScreenEvent) {
        when (event) {
            is ListStudentScreenEvent.OnStudentClicked -> {
                _listStudentScreenState.update { currentState ->
                    currentState.copy(
                        selectedStudent = event.student
                    )
                }
                onEvent(ListStudentScreenEvent.OnAlertDialogToggled)
            }

            ListStudentScreenEvent.OnAlertDialogToggled -> {
                _listStudentScreenState.update { currentState ->
                    currentState.copy(
                        isDialogOpened = !currentState.isDialogOpened
                    )
                }
            }

            ListStudentScreenEvent.OnDeleteStudentClicked -> {
                deleteStudent()
                onEvent(ListStudentScreenEvent.OnAlertDialogToggled)
            }
        }
    }

    private fun deleteStudent() = viewModelScope.launch {
        listStudentScreenState.value.selectedStudent?.let { student ->
            repository.deleteStudent(student).collectLatest { result ->

                _listStudentScreenState.update { currentState ->
                    currentState.copy(
                        isLoading = result is DataState.Loading
                    )
                }

                when (result) {
                    is DataState.Error -> eventChannel.send(result.errorMessage)
                    is DataState.Success -> {
                        eventChannel.send(UiText.StringResource(R.string.message_student_deleted, emptyList()))
                    }
                    else -> Unit
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            repository.getStudent().collectLatest { result ->
                _studentsResult.update { result }
            }
        }
    }
}