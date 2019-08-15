package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.CreateAccountParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class CreateUserUseCase @Inject constructor(private val userRepository: UserRepository) :
    BaseUseCase<CreateAccountParam, String>() {

    override suspend fun execute(
        params: CreateAccountParam,
        resultLiveData: MutableLiveData<Resource<String>>
    ) {
        userRepository.createUser(params, resultLiveData)
    }
}