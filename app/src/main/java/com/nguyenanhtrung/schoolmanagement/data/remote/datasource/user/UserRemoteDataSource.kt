package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import androidx.collection.ArrayMap
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.xwray.groupie.kotlinandroidextensions.Item

interface UserRemoteDataSource {
    suspend fun loadUserInfoAsync(): Resource<User>

    suspend fun getUserId(): String

    suspend fun sendResetPassword(email: String): Resource<Int>

    suspend fun createNewUser(createAccountParam: CreateAccountParam): Resource<String>

    suspend fun getUsers(userTypes: Map<String, String>): Resource<MutableList<out Item>>

    suspend fun getPagingUsers(
        lastUserId: Long,
        userTypes: Map<String, String>
    ): Resource<MutableList<out Item>>

    suspend fun updateUserInfo(updateInfoParams: UpdateAccountInfoParams): Resource<Unit>

    suspend fun changeUserPassword(
        fireBaseUserId: String,
        accountName: String,
        newPassword: String
    ): Resource<Unit>

    suspend fun getUserPassword(fireBaseUserId: String): Resource<String>

    suspend fun getUserByProfileStatus(
        userTypes: Map<String, String>,
        profileFilter: ProfileFilter
    ): Resource<MutableList<out Item>>

    suspend fun getPagingUsesByProfileStatus(
        lastUserId: Long,
        userTypes: Map<String, String>,
        profileFilter: ProfileFilter
    ): Resource<MutableList<out Item>>
}