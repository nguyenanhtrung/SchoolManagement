package com.nguyenanhtrung.schoolmanagement.data.local.model

import androidx.annotation.NonNull

class Resource<T> private constructor(
    val status: Status,
    val data: T?,
    val error: Int = -1,
    val exception: Throwable? = null
){
    companion object {

        fun <T> completed(): Resource<T> {
            return Resource(Status.COMPLETE, data = null)
        }

        fun <T> success(@NonNull data: T): Resource<T> {
            return Resource(Status.SUCCESS, data)
        }

        fun <T> failure(error: Int): Resource<T> {
            return Resource(Status.FAILURE, data = null, error = error)
        }

        fun <T> exception(throwable: Throwable): Resource<T> {
            return Resource(Status.EXCEPTION, data = null, exception =  throwable)
        }

        fun <T> loading(): Resource<T> = Resource(Status.LOADING, null)
    }
}