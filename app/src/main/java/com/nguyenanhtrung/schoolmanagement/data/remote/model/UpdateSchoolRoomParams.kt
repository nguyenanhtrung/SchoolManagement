package com.nguyenanhtrung.schoolmanagement.data.remote.model

import android.util.ArrayMap

class UpdateSchoolRoomParams(
    val fireBaseId: String,
    val fields: ArrayMap<String, Any>
)