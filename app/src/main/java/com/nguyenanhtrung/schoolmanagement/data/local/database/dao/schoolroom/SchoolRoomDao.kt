package com.nguyenanhtrung.schoolmanagement.data.local.database.dao.schoolroom

import androidx.room.Dao
import androidx.room.Query
import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.base.BaseDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.SchoolRoomEntity

@Dao
interface SchoolRoomDao : BaseDao<SchoolRoomEntity> {

    @Query("SELECT * FROM SchoolRoom LIMIT 15 OFFSET :offset")
    suspend fun getSchoolRooms(offset: Int): List<SchoolRoomEntity>
}