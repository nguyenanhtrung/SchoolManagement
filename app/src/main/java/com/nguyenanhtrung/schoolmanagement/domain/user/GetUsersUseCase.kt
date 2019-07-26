package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserItem
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.data.repository.usertype.UserTypeRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val userTypeRepository: UserTypeRepository
) : BaseUseCase<Long, MutableList<UserItem>>() {

    private val userTypes by lazy {
        userTypeRepository.getUserTypes()
    }

    override suspend fun execute(
        params: Long,
        resultLiveData: MutableLiveData<Resource<MutableList<UserItem>>>
    ) {
        userTypes
        userRepository.getUsers(params,userTypes, resultLiveData)
    }
}