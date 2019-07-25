package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import com.nguyenanhtrung.schoolmanagement.data.local.model.*

interface UserRemoteDataSource {
    suspend fun loadUserInfoAsync(): Resource<User>

    suspend fun getUserId(): String

    suspend fun sendResetPassword(email: String): Resource<Int>

    suspend fun createNewUser(createAccountParam: CreateAccountParam): Resource<Unit>

    suspend fun getUsers(): Resource<List<UserItem>>

}