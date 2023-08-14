package com.lelestacia.kampusku.ui.screen.student.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.ui.component.student.StudentCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListStudentScreen(
    students: List<StudentFirebaseModel>,
    onEvent: (ListStudentScreenEvent) -> Unit,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = students,
            key = { student -> student.id }
        ) { student ->
            StudentCard(
                student = student,
                onClicked = { selectedStudent ->
                    onEvent(ListStudentScreenEvent.OnStudentClicked(selectedStudent))
                },
                modifier = Modifier.animateItemPlacement()
            )
            HorizontalDivider()
        }
    }
}