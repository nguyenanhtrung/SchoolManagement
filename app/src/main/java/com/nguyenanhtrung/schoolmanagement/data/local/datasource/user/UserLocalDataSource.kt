package com.nguyenanhtrung.schoolmanagement.data.local.datasource.user

import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User

interface UserLocalDataSource {

    suspend fun saveUserInfo(user: User)
    suspend fun getUserInfo(userId: String): Resource<User>
    suspend fun checkExistUserInfo(userId: String): Boolean
}