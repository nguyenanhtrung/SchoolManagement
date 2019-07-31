package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ProfileFilter : Parcelable {
    @Parcelize object All : ProfileFilter()
    @Parcelize object Updated : ProfileFilter()
    @Parcelize object NoUpdate : ProfileFilter()
}