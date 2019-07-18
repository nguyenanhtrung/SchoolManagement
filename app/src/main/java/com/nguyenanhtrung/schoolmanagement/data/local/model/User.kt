package com.nguyenanhtrung.schoolmanagement.data.local.model

data class User(
    val name: String,
    val status: UserStatus,
    val typeName: String,
    val typeId: String,
    val avatarPath: String
)