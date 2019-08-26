package com.nguyenanhtrung.schoolmanagement.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SchoolRoom")
class SchoolRoomEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "firebase_id")
    val fireBaseId: String,

    @ColumnInfo(name = "room_number")
    val roomNumber: String,

    @ColumnInfo(name = "room_name")
    val roomName: String,

    @ColumnInfo(name = "is_office_room")
    val isOfficeRoom: Boolean

)