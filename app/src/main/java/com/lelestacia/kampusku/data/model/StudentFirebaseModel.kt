package com.lelestacia.kampusku.data.model

import android.os.Parcelable
import com.google.firebase.firestore.PropertyName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentFirebaseModel(

    @PropertyName("student_docs_id")
    var id: String = "",

    @PropertyName("student_id")
    var identificationNumber: String = "",

    @PropertyName("student_name")
    var name: String = "",

    @PropertyName("student_birth")
    var studentBirthDate: Long = 0,

    @PropertyName("student_gender")
    var isWoman: Boolean = false,

    @PropertyName("student_address")
    var address: String = "",

    @PropertyName("student_photo")
    var photoUrl: String = ""
): Parcelable