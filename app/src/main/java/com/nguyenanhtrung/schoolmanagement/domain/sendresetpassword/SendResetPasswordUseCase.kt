package com.nguyenanhtrung.schoolmanagement.domain.sendresetpassword

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class SendResetPasswordUseCase @Inject constructor(private val userRepository: UserRepository) : BaseUseCase<String, Int>() {


    override suspend fun execute(
        params: String,
        resultLiveData: MutableLiveData<Resource<Int>>
    ) {
        userRepository.sendResetPasswordToEmail(params, resultLiveData)
    }
}