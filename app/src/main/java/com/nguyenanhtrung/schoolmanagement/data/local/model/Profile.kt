package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Profile(
    val fireBaseUserId: String,
    val userId: Long,
    val name: String,
    val phoneNumber: String,
    val gender: Gender,
    val userType: UserType,
    var profileImagePath: String = ""
): Parcelable