package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FlowStatusInfo(
    val status: Status,
    val titleId: Int,
    val contentId: Int,
    val buttonNameId: Int,
    val destinationId: Int = -1
) : Parcelable
