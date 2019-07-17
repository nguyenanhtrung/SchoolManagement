package com.nguyenanhtrung.schoolmanagement.data.local.model

data class User(
    val name: String,
    val status: UserStatus,
    val type: String,
    val avatarPath: String
)