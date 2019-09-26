package com.nguyenanhtrung.schoolmanagement.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class UserDetail(
    val account: String,
    val password: String
): Parcelable