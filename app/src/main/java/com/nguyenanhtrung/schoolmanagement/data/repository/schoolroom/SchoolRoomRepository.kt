package com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.xwray.groupie.kotlinandroidextensions.Item

interface SchoolRoomRepository {

    suspend fun getSchoolRoomsAsync(result: MutableLiveData<Resource<MutableList<out Item>>>)
}