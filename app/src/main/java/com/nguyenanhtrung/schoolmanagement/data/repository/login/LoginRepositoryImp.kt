package com.nguyenanhtrung.schoolmanagement.data.repository.login

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.LoginRemoteDataSource
import javax.inject.Inject

class LoginRepositoryImp @Inject constructor(private val loginRemoteDataSource: LoginRemoteDataSource): LoginRepository {


    override suspend fun loginAsync(
        loginPair: Pair<String, String>,
        result: MutableLiveData<ResultModel<Boolean>>
    ) {
        loginRemoteDataSource.loginAsync(loginPair, result)
    }
}