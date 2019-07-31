package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class FilterState : Parcelable {
    @Parcelize class Profile(val status: ProfileFilter) : FilterState()
}