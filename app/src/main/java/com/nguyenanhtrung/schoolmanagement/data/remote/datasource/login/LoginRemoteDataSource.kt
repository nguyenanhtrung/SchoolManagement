package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.login

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoginState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel

interface LoginRemoteDataSource {

    suspend fun loginAsync(loginPair: Pair<String,String>, result: MutableLiveData<Resource<Boolean>>)
    suspend fun checkAlreadyLoginAsync(result: MutableLiveData<Resource<LoginState>>)
}