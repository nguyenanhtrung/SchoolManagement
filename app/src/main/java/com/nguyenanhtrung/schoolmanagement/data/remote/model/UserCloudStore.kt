package com.nguyenanhtrung.schoolmanagement.data.remote.model

import com.google.firebase.firestore.PropertyName

data class UserCloudStore constructor(
    @set:PropertyName("avatar_path")
    @get:PropertyName("avatar_path")
    var avatarPath: String = "",

    val id: Long = -1,
    val name: String = "",

    @set:PropertyName("type_id")
    @get:PropertyName("type_id")
    var typeId: String = ""
)