package com.nguyenanhtrung.schoolmanagement.data.repository.user

import androidx.collection.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserDetail
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
        result: MutableLiveData<Resource<MutableList<out GenericItem>>>
    )

    suspend fun getUsersByLimit(
        lastUserId: Long,
        userTypes: Map<String, String>,
        result: MutableLiveData<Resource<MutableList<out GenericItem>>>
    )

    suspend fun updateUserInfo(
        result: MutableLiveData<Resource<Unit>>,
        updateInfoParams: UpdateAccountInfoParams
    )

    suspend fun changeUserPassword(
        changePassParam: ChangePasswordParam,
        result: MutableLiveData<Resource<Unit>>
    )

    suspend fun getUserPassword(
        fireBaseUserId: String,
        result: MutableLiveData<Resource<String>>
    )

    suspend fun getUserDetail(
        fireBaseUserId: String,
        result: MutableLiveData<Resource<UserDetail>>
    )
}