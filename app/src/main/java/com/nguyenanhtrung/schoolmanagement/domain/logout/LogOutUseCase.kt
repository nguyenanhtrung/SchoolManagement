package com.nguyenanhtrung.schoolmanagement.domain.logout

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.login.LoginRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class LogOutUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    BaseUseCase<Unit, Unit>() {

    override suspend fun execute(params: Unit, resultLiveData: MutableLiveData<Resource<Unit>>) {
        loginRepository.logOut(resultLiveData)
    }
}