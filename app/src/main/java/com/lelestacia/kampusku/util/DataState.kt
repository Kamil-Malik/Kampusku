package com.lelestacia.kampusku.util

sealed class DataState<out T> {
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val errorMessage: UiText) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    object None : DataState<Nothing>()
}
