package com.nguyenanhtrung.schoolmanagement.data.local.datasource.schoolroom

import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.xwray.groupie.kotlinandroidextensions.Item

interface SchoolRoomLocalDataSource {

    suspend fun getSchoolRooms(offset: Int): Resource<MutableList<out Item>>

    suspend fun saveSchoolRooms(schoolRoomItems: List<Item>)

    suspend fun saveSchoolRoom(schoolRoom: SchoolRoom)

    suspend fun isSchoolRoomsSaved(offset: Int): Boolean
}