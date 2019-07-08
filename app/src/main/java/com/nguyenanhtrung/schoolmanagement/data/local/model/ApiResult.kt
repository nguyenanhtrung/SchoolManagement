package com.nguyenanhtrung.schoolmanagement.data.local.model

sealed class ApiResult {
    object Loading : ApiResult()
    data class Success<T>(val value: T) : ApiResult()
    data class Failure<E>(val error: E) : ApiResult()
}