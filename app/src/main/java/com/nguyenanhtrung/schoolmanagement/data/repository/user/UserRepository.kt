package com.nguyenanhtrung.schoolmanagement.data.repository.user

import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.xwray.groupie.kotlinandroidextensions.Item

interface UserRepository {
    suspend fun loadUserInfo(result: MutableLiveData<Resource<User>>)

    suspend fun sendResetPasswordToEmail(email: String, result: MutableLiveData<Resource<Int>>)

    suspend fun createUser(
        createAccountParam: CreateAccountParam,
        result: MutableLiveData<Resource<String>>
    )

    suspend fun getUsers(
        userTypes: Map<String, String>,
        result: MutableLiveData<Resource<MutableList<out Item>>>
    )

    suspend fun getUsersByProfileFilter(
        params: Pair<ProfileFilter, Map<String, String>>,
        result: MutableLiveData<Resource<MutableList<ProfileItem>>>
    )

    suspend fun getUsersByLimit(
        lastUserId: Long,
        userTypes: Map<String, String>,
        result: MutableLiveData<Resource<MutableList<out Item>>>
    )

    suspend fun getPagingUserByProfileFilter(
        lastUserId: Long,
        params: Pair<ProfileFilter, Map<String, String>>,
        result: MutableLiveData<Resource<MutableList<ProfileItem>>>
    )

    suspend fun updateUserInfo(
        result: MutableLiveData<Resource<Unit>>,
        userInfos: Pair<String, ArrayMap<String, String>>
    )

    suspend fun changeUserPassword(
        changePassParam: ChangePasswordParam,
        result: MutableLiveData<Resource<Unit>>
    )
}