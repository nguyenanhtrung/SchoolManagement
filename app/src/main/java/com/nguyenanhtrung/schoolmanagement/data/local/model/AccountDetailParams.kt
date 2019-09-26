package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserDetail
import kotlinx.android.parcel.Parcelize

@Parcelize
class AccountDetailParams(
    val user: User,
    val userDetail: UserDetail
): Parcelable