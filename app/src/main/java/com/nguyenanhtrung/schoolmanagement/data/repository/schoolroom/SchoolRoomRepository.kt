package com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom

import androidx.lifecycle.MutableLiveData
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.CreateSchoolRoomParams
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UpdateSchoolRoomParams
import com.xwray.groupie.kotlinandroidextensions.Item

interface SchoolRoomRepository {

    suspend fun getSchoolRoomsAsync(result: MutableLiveData<Resource<MutableList<out GenericItem>>>)
    suspend fun getPagingSchoolRoomsAsync(
        lastRoomId: Long,
        result: MutableLiveData<Resource<MutableList<out GenericItem>>>
    )

    suspend fun createSchoolRoom(
        schoolRoomParams: CreateSchoolRoomParams,
        result: MutableLiveData<Resource<SchoolRoom>>
    )

    suspend fun updateSchoolRoom(
        params: UpdateSchoolRoomParams,
        result: MutableLiveData<Resource<Unit>>
    )
}