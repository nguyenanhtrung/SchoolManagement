package com.nguyenanhtrung.schoolmanagement.domain.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ChangePasswordParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.user.UserRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class ChangeUserPassUseCase @Inject constructor(private val userRepository: UserRepository) :
    BaseUseCase<ChangePasswordParam, Unit>() {

    override suspend fun execute(
        params: ChangePasswordParam,
        resultLiveData: MutableLiveData<Resource<Unit>>
    ) {
        userRepository.changeUserPassword(params, resultLiveData)
    }
}