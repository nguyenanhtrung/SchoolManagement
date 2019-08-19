package com.nguyenanhtrung.schoolmanagement.data.local.model

sealed class ListEmptyState {
    object CLEAR : ListEmptyState()
    class EMPTY(val emptyView: EmptyItem) : ListEmptyState()
}