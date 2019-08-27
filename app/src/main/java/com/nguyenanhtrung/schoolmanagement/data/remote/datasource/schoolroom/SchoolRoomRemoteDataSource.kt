package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.schoolroom

import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.CreateSchoolRoomParams
import com.xwray.groupie.kotlinandroidextensions.Item

interface SchoolRoomRemoteDataSource {

    suspend fun getSchoolRoomsAsync(): Resource<MutableList<out Item>>

    suspend fun getPagingSchoolRoomsAsync(lastRoomId: Long): Resource<MutableList<out Item>>

    suspend fun createSchoolRoom(schoolRoomParams: CreateSchoolRoomParams): Resource<SchoolRoom>
}