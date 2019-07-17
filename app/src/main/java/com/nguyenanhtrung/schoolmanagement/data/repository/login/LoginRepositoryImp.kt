package com.nguyenanhtrung.schoolmanagement.data.repository.login

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoginState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.login.LoginRemoteDataSource
import javax.inject.Inject

class LoginRepositoryImp @Inject constructor(private val loginRemoteDataSource: LoginRemoteDataSource): LoginRepository {


    override suspend fun loginAsync(
        loginPair: Pair<String, String>,
        result: MutableLiveData<Resource<Boolean>>
    ) {
        loginRemoteDataSource.loginAsync(loginPair, result)
    }

    override suspend fun checkAlreadyLoginAsync(result: MutableLiveData<Resource<LoginState>>) {
        loginRemoteDataSource.checkAlreadyLoginAsync(result)
    }
}