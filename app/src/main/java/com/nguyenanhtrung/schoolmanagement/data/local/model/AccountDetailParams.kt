package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountDetailParams(
    val user: User,
    val password: String
): Parcelable