package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SchoolRoom(
    val fireBaseId: String,
    val roomId: Long,
    val roomNumber: String,
    val roomName: String,
    val isOfficeRoom: Boolean
): Parcelable