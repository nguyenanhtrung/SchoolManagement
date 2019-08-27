package com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.remote.model.CreateSchoolRoomParams
import com.xwray.groupie.kotlinandroidextensions.Item

interface SchoolRoomRepository {

    suspend fun getSchoolRoomsAsync(result: MutableLiveData<Resource<MutableList<out Item>>>)
    suspend fun getPagingSchoolRoomsAsync(
        lastRoomId: Long,
        result: MutableLiveData<Resource<MutableList<out Item>>>
    )

    suspend fun createSchoolRoom(
        schoolRoomParams: CreateSchoolRoomParams,
        result: MutableLiveData<Resource<SchoolRoom>>
    )
}