package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: Long,
    val firebaseUserId: String,
    val name: String,
    val typeName: String,
    val typeId: String,
    val avatarPath: String,
    val accountName: String
) : Parcelable