package com.lelestacia.kampusku.ui.component.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.lelestacia.kampusku.ui.theme.Blue
import com.lelestacia.kampusku.ui.theme.Pink

@Composable
fun GenderIcon(
    isWoman: Boolean
) {
    Icon(
        imageVector =
        if (isWoman) {
            Icons.Default.Female
        } else {
            Icons.Default.Male
        },
        contentDescription = null,
        tint =
        if (isWoman) {
            Pink
        } else {
            Blue
        }
    )
}