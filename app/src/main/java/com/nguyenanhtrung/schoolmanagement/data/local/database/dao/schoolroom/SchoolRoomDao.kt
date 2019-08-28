package com.nguyenanhtrung.schoolmanagement.data.local.database.dao.schoolroom

import androidx.collection.ArrayMap
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.base.BaseDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.SchoolRoomEntity
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UpdateSchoolRoomParams
import com.nguyenanhtrung.schoolmanagement.util.AppKey

@Dao
interface SchoolRoomDao : BaseDao<SchoolRoomEntity> {

    @Query("SELECT * FROM SchoolRoom LIMIT 15 OFFSET :offset")
    suspend fun getSchoolRooms(offset: Int): List<SchoolRoomEntity>

    @Query(value = "SELECT EXISTS(SELECT 1 FROM SchoolRoom LIMIT 15 OFFSET :offset)")
    suspend fun checkSchoolRoomsSaved(offset: Int): Int

    @Query("SELECT * FROM SchoolRoom WHERE firebase_id LIKE :fireBaseId")
    suspend fun getSingleSchoolRoom(fireBaseId: String): SchoolRoomEntity

    @Transaction
    suspend fun updateSchoolRoom(updateSchoolRoomParams: UpdateSchoolRoomParams) {
        val schoolRoomEntity = getSingleSchoolRoom(updateSchoolRoomParams.fireBaseId)
        val fields = updateSchoolRoomParams.fields
        if (fields[AppKey.ROOM_NUMBER_FIELD_SCHOOL_ROOMS] != null) {
            schoolRoomEntity.roomNumber = fields[AppKey.ROOM_NUMBER_FIELD_SCHOOL_ROOMS] as String
        }
        if (fields[AppKey.NAME_FIELD_SCHOOL_ROOMS] != null) {
            schoolRoomEntity.roomName = fields[AppKey.NAME_FIELD_SCHOOL_ROOMS] as String
        }
        if (fields[AppKey.IS_OFFICE_ROOM_FIELD_SCHOOL_ROOMS] != null) {
            schoolRoomEntity.isOfficeRoom = fields[AppKey.IS_OFFICE_ROOM_FIELD_SCHOOL_ROOMS] as Boolean
        }
        updateData(schoolRoomEntity)
    }
}