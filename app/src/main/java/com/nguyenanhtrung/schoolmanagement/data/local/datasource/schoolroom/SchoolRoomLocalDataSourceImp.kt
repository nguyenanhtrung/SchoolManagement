package com.nguyenanhtrung.schoolmanagement.data.local.datasource.schoolroom

import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.schoolroom.SchoolRoomDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.SchoolRoomEntity
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class SchoolRoomLocalDataSourceImp @Inject constructor(private val schoolRoomDao: SchoolRoomDao) :
    SchoolRoomLocalDataSource {


    override suspend fun getSchoolRooms(offset: Int): Resource<MutableList<out Item>> {
        val schoolRoomEntities = schoolRoomDao.getSchoolRooms(offset)
        val schoolRoomItems =  schoolRoomEntities.map {
            SchoolRoomItem(
                SchoolRoom(it.fireBaseId, it.id, it.roomNumber, it.roomName, it.isOfficeRoom)
            )
        }.toMutableList()
        return Resource.success(schoolRoomItems)
    }

    override suspend fun saveSchoolRooms(schoolRoomItems: List<SchoolRoomItem>) {
        val schoolRoomEntities = schoolRoomItems.map {
            val schoolRoom = it.schoolRoom
            SchoolRoomEntity(
                schoolRoom.roomId,
                schoolRoom.fireBaseId,
                schoolRoom.roomNumber,
                schoolRoom.roomName,
                schoolRoom.isOfficeRoom
            )
        }
        schoolRoomDao.insertListData(schoolRoomEntities)
    }
}