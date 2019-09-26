package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserDetail
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(private val userRepository: UserRepository) :
    BaseUseCase<String, UserDetail>() {


    override suspend fun execute(
        params: String,
        resultLiveData: MutableLiveData<Resource<UserDetail>>
    ) {
        userRepository.getUserDetail(params, resultLiveData)
    }
}