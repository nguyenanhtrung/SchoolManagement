package com.nguyenanhtrung.schoolmanagement.data.local.datasource.schoolroom

import androidx.collection.ArrayMap
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UpdateSchoolRoomParams
import com.xwray.groupie.kotlinandroidextensions.Item

interface SchoolRoomLocalDataSource {

    suspend fun getSchoolRooms(offset: Int): Resource<MutableList<out GenericItem>>

    suspend fun saveSchoolRooms(schoolRoomItems: List<GenericItem>)

    suspend fun saveSchoolRoom(schoolRoom: SchoolRoom)

    suspend fun isSchoolRoomsSaved(offset: Int): Boolean

    suspend fun saveUpdateSchoolRoom(updateSchoolRoomParams: UpdateSchoolRoomParams)
}