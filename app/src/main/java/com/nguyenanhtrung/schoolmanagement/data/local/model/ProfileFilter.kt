package com.nguyenanhtrung.schoolmanagement.data.local.model

sealed class ProfileFilter {
    object All : ProfileFilter()
    object Updated : ProfileFilter()
    object NoUpdate : ProfileFilter()
}