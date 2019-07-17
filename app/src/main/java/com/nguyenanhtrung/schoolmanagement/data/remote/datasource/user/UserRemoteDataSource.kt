package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User

interface UserRemoteDataSource {
    suspend fun getUserInfo(): Resource<User>
}