package com.example.myapplication.data.api.model

sealed class DataState<out T>

data object Loading : DataState<Nothing>()
data object NoDataFound : DataState<Nothing>()

sealed class Result<out T> : DataState<T>() {

    data class Success<out T : Any>(val data: T) : Result<T>()

    data class Error(val error: Throwable, val cachedData: Any? = null) : Result<Nothing>()
}
