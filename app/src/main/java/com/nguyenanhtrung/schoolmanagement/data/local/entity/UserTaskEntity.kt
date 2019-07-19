package com.nguyenanhtrung.schoolmanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTask")
data class UserTaskEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "icon_id")
    val iconId: Int,

    @ColumnInfo(name = "user_type_id")
    val userTypeId: String
)