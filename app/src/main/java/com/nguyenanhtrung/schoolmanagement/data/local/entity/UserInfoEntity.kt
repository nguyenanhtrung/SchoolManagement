package com.nguyenanhtrung.schoolmanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class UserInfoEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "firebase_user_id")
    val firebaseUserId: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "avatar_path")
    val avatarPath: String,

    @ColumnInfo(name = "type_id")
    val typeId: String,

    @ColumnInfo(name = "type_name")
    val typeName: String

)