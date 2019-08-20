package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetUserPasswordUseCase @Inject constructor(private val userRepository: UserRepository) :
    BaseUseCase<String, String>() {


    override suspend fun execute(
        params: String,
        resultLiveData: MutableLiveData<Resource<String>>
    ) {
        userRepository.getUserPassword(params, resultLiveData)
    }
}