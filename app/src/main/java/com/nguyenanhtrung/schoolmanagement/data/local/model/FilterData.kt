package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FilterData(val titleId: Int, val state: FilterState, var isSelected: Boolean = false) :
    Parcelable