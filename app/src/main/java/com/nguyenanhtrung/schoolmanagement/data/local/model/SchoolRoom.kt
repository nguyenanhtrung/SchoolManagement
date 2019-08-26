package com.nguyenanhtrung.schoolmanagement.data.local.model

data class SchoolRoom(
    val fireBaseId: String,
    val roomId: Long,
    val roomNumber: String,
    val roomName: String,
    val isOfficeRoom: Boolean
)