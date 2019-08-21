package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val fireBaseUserId: String,
    val userId: Long,
    val name: String,
    var isProfileUpdated: Boolean = false,
    val userTypeName: String,
    val avatarPath: String = "",
    val profileImagePath: String = ""
): Parcelable