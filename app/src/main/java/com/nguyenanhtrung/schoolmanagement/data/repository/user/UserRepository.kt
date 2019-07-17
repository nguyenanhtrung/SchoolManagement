package com.nguyenanhtrung.schoolmanagement.data.repository.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User

interface UserRepository{
    suspend fun loadUserInfo(result: MutableLiveData<Resource<User>>)
}