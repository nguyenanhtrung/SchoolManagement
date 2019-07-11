package com.nguyenanhtrung.schoolmanagement.data.remote.datasource

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel

interface LoginRemoteDataSource {

    suspend fun loginAsync(loginPair: Pair<String,String>, result: MutableLiveData<ResultModel<Boolean>>)
}