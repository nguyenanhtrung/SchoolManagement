package com.nguyenanhtrung.schoolmanagement.domain.usertype

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import com.nguyenanhtrung.schoolmanagement.data.repository.usertype.UserTypeRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetUserTypesUseCase @Inject constructor(private val userTypeRepository: UserTypeRepository) :
    BaseUseCase<Boolean, List<UserType>>() {

    override suspend fun execute(
        params: Boolean,
        resultLiveData: MutableLiveData<Resource<List<UserType>>>
    ) {
        userTypeRepository.loadUserTypes(params, resultLiveData)
    }
}