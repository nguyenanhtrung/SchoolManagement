package com.nguyenanhtrung.schoolmanagement.data.repository.user

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.user.UserLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user.UserRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    @ApplicationContext private val context: Context
) : UserRepository {


    override suspend fun loadUserInfo(result: MutableLiveData<Resource<User>>) {
        object : NetworkBoundResources<Unit, User>(context, Unit, result) {

            override suspend fun shouldFetchFromServer(params: Unit): Boolean {
                val userId = userRemoteDataSource.getUserId()
                val isUserInfoExist = userLocalDataSource.checkExistUserInfo(userId)
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


}