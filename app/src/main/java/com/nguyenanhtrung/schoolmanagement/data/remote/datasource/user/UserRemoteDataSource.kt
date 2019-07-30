package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user

import androidx.collection.ArrayMap
import com.nguyenanhtrung.schoolmanagement.data.local.model.*

interface UserRemoteDataSource {
    suspend fun loadUserInfoAsync(): Resource<User>

    suspend fun getUserId(): String

    suspend fun sendResetPassword(email: String): Resource<Int>

    suspend fun createNewUser(createAccountParam: CreateAccountParam): Resource<Unit>

    suspend fun getUsers(userTypes: Map<String, String>): Resource<MutableList<UserItem>>

    suspend fun getPagingUsers(
        lastUserId: Long,
        userTypes: Map<String, String>
    ): Resource<MutableList<UserItem>>

    suspend fun updateUserInfo(userInfos: Pair<String, ArrayMap<String, String>>): Resource<Unit>

    suspend fun changeUserPassword(
        fireBaseUserId: String,
        accountName: String,
        newPassword: String
    ): Resource<Unit>

    suspend fun getUserByProfileStatus(
        userTypes: Map<String, String>,
        profileFilter: ProfileFilter
    ): Resource<MutableList<ProfileItem>>

    suspend fun getPagingUsesByProfileStatus(
        lastUserId: Long,
        userTypes: Map<String, String>,
        profileFilter: ProfileFilter
    ): Resource<MutableList<ProfileItem>>
}