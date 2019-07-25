package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlowStatusInfo(
    private val status: Status,
    private val message: String,
    private val destinationId: Int = -1
) : Parcelable
