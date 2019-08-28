package com.nguyenanhtrung.schoolmanagement.domain.schoolroomdetail

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UpdateSchoolRoomParams
import com.nguyenanhtrung.schoolmanagement.data.repository.schoolroom.SchoolRoomRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class UpdateSchoolRoomUseCase @Inject constructor(private val schoolRoomRepository: SchoolRoomRepository) :
    BaseUseCase<UpdateSchoolRoomParams, Unit>() {


    override suspend fun execute(
        params: UpdateSchoolRoomParams,
        resultLiveData: MutableLiveData<Resource<Unit>>
    ) {
        schoolRoomRepository.updateSchoolRoom(params, resultLiveData)
    }
}