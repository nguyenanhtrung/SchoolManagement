package com.nguyenanhtrung.schoolmanagement.data.local.model

sealed class ErrorState {
    class NoAction(val messageId: Int) : ErrorState()
    class WithAction(val messageId: Int, val actionNameId: Int,  val action: () -> Unit) : ErrorState()
    object Empty : ErrorState()
}