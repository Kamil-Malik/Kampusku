package com.lelestacia.kampusku.ui.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import com.lelestacia.kampusku.data.model.StudentFirebaseModel
import com.lelestacia.kampusku.util.parcelable

class StudentParcelableNavType : NavType<StudentFirebaseModel>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): StudentFirebaseModel? {
        return bundle.parcelable(key)
    }

    override fun parseValue(value: String): StudentFirebaseModel {
        return Gson().fromJson(value, StudentFirebaseModel::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: StudentFirebaseModel) {
        bundle.putParcelable(key, value)
    }
}