package com.lelestacia.kampusku.ui.screen.student.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.lelestacia.kampusku.R
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.ui.component.icon.GenderIcon
import com.lelestacia.kampusku.ui.component.topbar.KampuskuTopBar
import com.parassidhu.simpledate.toDateStandard
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailStudentScreen(
    student: StudentFirebaseModel,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            KampuskuTopBar(
                title = stringResource(id = R.string.topbar_title_detail_student),
                onNavigateBack = onNavigateBack,
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(
                    horizontal = 16.dp
                )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Box(
                    contentAlignment = Alignment.BottomEnd
                ) {
                    AsyncImage(
                        model = student.photoUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(125.dp)
                            .clip(CircleShape)
                    )
                    GenderIcon(isWoman = student.isWoman)
                }
            }

            OutlinedTextField(
                value = student.identificationNumber,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(text = stringResource(id = R.string.label_identification_number))
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = student.name,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(text = stringResource(id = R.string.label_name))
                },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = Date(student.studentBirthDate).toDateStandard(),
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(text = stringResource(id = R.string.label_birth_date))
                },
            )

            OutlinedTextField(
                value = student.address,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(text = stringResource(id = R.string.label_address))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}