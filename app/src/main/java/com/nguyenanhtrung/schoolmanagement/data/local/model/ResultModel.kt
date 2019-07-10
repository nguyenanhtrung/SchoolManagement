package com.nguyenanhtrung.schoolmanagement.data.local.model

open class ResultModel <out T : Any> {
    object Loading : ResultModel<Nothing>()
    class Success<out T : Any>(@JvmField val value: T) : ResultModel<T>()
    class Failure(@JvmField val error: String) : ResultModel<Nothing>()
}
