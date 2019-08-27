package com.nguyenanhtrung.schoolmanagement.domain.schoolrooms

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.remote.model.CreateSchoolRoomParams
import com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom.SchoolRoomRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class AddSchoolRoomUseCase @Inject constructor(private val schoolRoomRepository: SchoolRoomRepository) :
    BaseUseCase<CreateSchoolRoomParams, SchoolRoom>() {
    override suspend fun execute(
        params: CreateSchoolRoomParams,
        resultLiveData: MutableLiveData<Resource<SchoolRoom>>
    ) {
        schoolRoomRepository.createSchoolRoom(params, resultLiveData)
    }
}