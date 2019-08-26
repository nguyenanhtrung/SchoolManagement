package com.nguyenanhtrung.schoolmanagement.domain.schoolrooms

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom.SchoolRoomRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class GetSchoolRoomsUseCase @Inject constructor(private val schoolRoomRepository: SchoolRoomRepository) :
    BaseUseCase<Int, MutableList<out Item>>() {


    override suspend fun execute(
        params: Int,
        resultLiveData: MutableLiveData<Resource<MutableList<out Item>>>
    ) {
        val offset = params
        if (offset < 0) {
            schoolRoomRepository.getSchoolRoomsAsync(resultLiveData)
        } else {
           //load more
        }
    }
}