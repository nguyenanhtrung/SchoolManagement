package com.nguyenanhtrung.schoolmanagement.data.local.model

data class Profile(
    val fireBaseUserId: String,
    val userId: Long,
    val name: String,
    val isProfileUpdated: Boolean,
    val userTypeName: String,
    val avatarPath: String
)