package com.nguyenanhtrung.schoolmanagement.data.remote.model

data class UserCloudStore constructor(
    val accountName: String = "",
    val avatarPath: String = "",
    val id: Long = -1,
    val name: String = "",
    val typeId: String = ""
)