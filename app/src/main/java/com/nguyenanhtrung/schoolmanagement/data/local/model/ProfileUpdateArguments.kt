package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class ProfileUpdateArguments(
    val firebaseUserId: String,
    val userId: Long,
    val userType: UserType,
    val accountName: String
): Parcelable