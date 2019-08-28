package com.nguyenanhtrung.schoolmanagement.data.remote.model

import androidx.collection.ArrayMap


class UpdateSchoolRoomParams(
    val fireBaseId: String,
    val fields: ArrayMap<String, Any>
)