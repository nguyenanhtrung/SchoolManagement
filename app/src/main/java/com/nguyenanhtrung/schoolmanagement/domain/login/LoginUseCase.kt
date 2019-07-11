package com.nguyenanhtrung.schoolmanagement.domain.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val loginRepository: LoginRepository) : BaseUseCase<Pair<String, String>, Boolean>(){

    override suspend fun execute(
        params: Pair<String, String>,
        resultLiveData: MutableLiveData<ResultModel<Boolean>>
    ) {
        loginRepository.loginAsync(params, resultLiveData)
    }
}