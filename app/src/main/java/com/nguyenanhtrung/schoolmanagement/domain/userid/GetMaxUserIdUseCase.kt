package com.nguyenanhtrung.schoolmanagement.domain.userid

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.userid.UserIdRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetMaxUserIdUseCase @Inject constructor(private val userIdRepository: UserIdRepository) :
    BaseUseCase<Unit, Long>() {

    override suspend fun execute(params: Unit, resultLiveData: MutableLiveData<Resource<Long>>) {
        userIdRepository.getMaxUserId(resultLiveData)
    }
}