package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import com.nguyenanhtrung.schoolmanagement.data.local.model.CreateAccountParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserItem

interface UserRemoteDataSource {
    suspend fun loadUserInfoAsync(): Resource<User>

    suspend fun getUserId(): String

    suspend fun sendResetPassword(email: String): Resource<Int>

    suspend fun createNewUser(createAccountParam: CreateAccountParam): Resource<Unit>

    suspend fun getUsers(userTypes: Map<String, String>): Resource<MutableList<UserItem>>

    suspend fun getPagingUsers(
        lastUserId: String,
        userTypes: Map<String, String>
    ): Resource<MutableList<UserItem>>
}