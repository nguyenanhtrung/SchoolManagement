package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val fireBaseUserId: String,
    val userId: Long,
    val name: String,
    val isProfileUpdated: Boolean,
    val userTypeName: String,
    val avatarPath: String
): Parcelable