package com.nguyenanhtrung.schoolmanagement.data.local.model

sealed class ErrorState {
    class NoAction(val message: String) : ErrorState()
    class WithAction(val message: String, val actionName: String,  val action: () -> Unit) : ErrorState()
}