package com.nguyenanhtrung.schoolmanagement.data.repository.user

import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.*

interface UserRepository {
    suspend fun loadUserInfo(result: MutableLiveData<Resource<User>>)

    suspend fun sendResetPasswordToEmail(email: String, result: MutableLiveData<Resource<Int>>)

    suspend fun createUser(
        createAccountParam: CreateAccountParam,
        result: MutableLiveData<Resource<Unit>>
    )

    suspend fun getUsers(
        userTypes: Map<String, String>,
        result: MutableLiveData<Resource<MutableList<UserItem>>>
    )

    suspend fun getUsersByProfileStatus(
        params: Pair<ProfileStatus, Map<String, String>>,
        result: MutableLiveData<Resource<MutableList<ProfileItem>>>
    )

    suspend fun getUsersByLimit(
        lastUserId: Long,
        userTypes: Map<String, String>,
        result: MutableLiveData<Resource<MutableList<UserItem>>>
    )

    suspend fun getPagingUserByProfileStatus(
        lastUserId: Long,
        params: Pair<ProfileStatus, Map<String, String>>,
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