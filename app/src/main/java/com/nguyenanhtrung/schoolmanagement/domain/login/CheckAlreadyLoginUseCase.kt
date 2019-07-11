package com.nguyenanhtrung.schoolmanagement.domain.login

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoginState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class CheckAlreadyLoginUseCase @Inject constructor(private val loginRepository: LoginRepository) : BaseUseCase<Unit, LoginState>() {


    override suspend fun execute(
        params: Unit,
        resultLiveData: MutableLiveData<Resource<LoginState>>
    ) {
        loginRepository.checkAlreadyLoginAsync(resultLiveData)
    }
}