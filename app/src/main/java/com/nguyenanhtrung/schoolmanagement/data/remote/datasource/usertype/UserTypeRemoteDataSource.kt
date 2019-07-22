package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.usertype

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType

interface UserTypeRemoteDataSource {

    suspend fun getUserTypes(): Resource<List<UserType>>
}