package com.nguyenanhtrung.schoolmanagement.data.local.model

sealed class ModificationState {
    object Edit : ModificationState()
    object Save : ModificationState()
}