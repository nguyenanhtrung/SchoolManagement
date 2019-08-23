package com.nguyenanhtrung.schoolmanagement.data.local.model

import androidx.collection.ArrayMap

class ProfileModificationParams(
    val firebaseUserId: String,
    val imagePath: String,
    val modifiedFields: ArrayMap<String, Any>
)