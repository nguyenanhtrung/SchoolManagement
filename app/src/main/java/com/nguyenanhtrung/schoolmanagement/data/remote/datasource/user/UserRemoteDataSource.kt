package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User

interface UserRemoteDataSource {
    suspend fun loadUserInfoAsync(result: MutableLiveData<Resource<User>>)
}