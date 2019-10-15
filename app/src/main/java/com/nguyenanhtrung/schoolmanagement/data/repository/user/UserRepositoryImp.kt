package com.nguyenanhtrung.schoolmanagement.data.repository.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.user.UserLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user.UserRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserDetail
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    @ApplicationContext private val context: Context
) : UserRepository {




    override suspend fun changeUserPassword(
        changePassParam: ChangePasswordParam,
        result: MutableLiveData<Resource<Unit>>
    ) {
        object :
            NetworkBoundResources<ChangePasswordParam, Unit>(context, changePassParam, result) {

            override fun shouldSaveToLocal(params: ChangePasswordParam): Boolean = false
            override fun shouldLoadFromLocal(params: ChangePasswordParam): Boolean = false

            override suspend fun callApi(): Resource<Unit> {
                return userRemoteDataSource.changeUserPassword(
                    changePassParam.fireBaseUserId,
                    changePassParam.accountName,
                    changePassParam.newPassword
                )
            }

        }.createCall()
    }

    override suspend fun getUserPassword(
        fireBaseUserId: String,
        result: MutableLiveData<Resource<String>>
    ) {
        object : NetworkBoundResources<String, String>(context, fireBaseUserId, result) {

            override fun shouldSaveToLocal(params: String): Boolean = false
            override fun shouldLoadFromLocal(params: String): Boolean = false

            override suspend fun callApi(): Resource<String> {
                return userRemoteDataSource.getUserPassword(params)
            }
        }.createCall()
    }

    override suspend fun getUserDetail(
        fireBaseUserId: String,
        result: MutableLiveData<Resource<UserDetail>>
    ) {
        object : NetworkBoundResources<String, UserDetail>(context, fireBaseUserId, result) {
            override fun shouldSaveToLocal(params: String): Boolean = false
            override fun shouldLoadFromLocal(params: String): Boolean = false

            override suspend fun callApi(): Resource<UserDetail> {
                return userRemoteDataSource.getUserDetail(fireBaseUserId)
            }
        }.createCall()
    }

    override suspend fun updateUserInfo(
        result: MutableLiveData<Resource<Unit>>,
        updateInfoParams: UpdateAccountInfoParams
    ) {
        object :
            NetworkBoundResources<UpdateAccountInfoParams, Unit>(
                context,
                updateInfoParams,
                result
            ) {

            override fun shouldLoadFromLocal(params: UpdateAccountInfoParams): Boolean =
                false

            override fun shouldSaveToLocal(params: UpdateAccountInfoParams): Boolean =
                false

            override suspend fun callApi(): Resource<Unit> {
                return userRemoteDataSource.updateUserInfo(params)
            }

        }.createCall()
    }

    override suspend fun sendResetPasswordToEmail(
        email: String,
        result: MutableLiveData<Resource<Int>>
    ) {
        object : NetworkBoundResources<String, Int>(context, email, result) {
            override suspend fun callApi(): Resource<Int> {
                return userRemoteDataSource.sendResetPassword(email)
            }
        }.createCall()
    }

    override suspend fun loadUserInfo(result: MutableLiveData<Resource<User>>) {
        object : NetworkBoundResources<Unit, User>(context, Unit, result) {

            override suspend fun shouldFetchFromServer(params: Unit): Boolean {
                val firebaseUserId = userRemoteDataSource.getUserId()
                val isUserInfoExist = userLocalDataSource.checkExistUserInfo(firebaseUserId)
                return !isUserInfoExist
            }

            override suspend fun callApi(): Resource<User> =
                userRemoteDataSource.loadUserInfoAsync()

            override fun shouldSaveToLocal(params: Unit) = true

            override suspend fun saveToLocal(output: User) =
                userLocalDataSource.saveUserInfo(output)

            override suspend fun loadFromLocal(params: Unit): Resource<User> {
                val userId = userRemoteDataSource.getUserId()
                return userLocalDataSource.getUserInfo(userId)
            }
        }.createCall()
    }

    override suspend fun createUser(
        createAccountParam: CreateAccountParam,
        result: MutableLiveData<Resource<String>>
    ) {
        object :
            NetworkBoundResources<CreateAccountParam, String>(context, createAccountParam, result) {

            override fun shouldLoadFromLocal(params: CreateAccountParam): Boolean = false
            override fun shouldSaveToLocal(params: CreateAccountParam): Boolean = false

            override suspend fun callApi(): Resource<String> {
                return userRemoteDataSource.createNewUser(createAccountParam)
            }

        }.createCall()
    }

    override suspend fun getUsers(
        userTypes: Map<String, String>,
        result: MutableLiveData<Resource<MutableList<out GenericItem>>>
    ) {

        object : NetworkBoundResources<Map<String, String>, MutableList<out GenericItem>>(
            context,
            userTypes,
            result
        ) {

            override fun shouldLoadFromLocal(params: Map<String, String>): Boolean = false
            override fun shouldSaveToLocal(params: Map<String, String>): Boolean = false

            override suspend fun callApi(): Resource<MutableList<out GenericItem>> {
                return userRemoteDataSource.getUsers(userTypes)
            }
        }.createCall()
    }

    override suspend fun getUsersByLimit(
        lastUserId: Long,
        userTypes: Map<String, String>,
        result: MutableLiveData<Resource<MutableList<out GenericItem>>>
    ) {
        object : NetworkBoundResources<Pair<Long, Map<String, String>>, MutableList<out GenericItem>>(
            context,
            Pair(lastUserId, userTypes),
            result
        ) {

            override fun shouldLoadFromLocal(params: Pair<Long, Map<String, String>>): Boolean =
                false

            override fun shouldSaveToLocal(params: Pair<Long, Map<String, String>>): Boolean = false
            override fun shouldShowLoading(): Boolean = false

            override suspend fun callApi(): Resource<MutableList<out GenericItem>> {
                return userRemoteDataSource.getPagingUsers(params.first, params.second)
            }
        }.createCall()
    }
}