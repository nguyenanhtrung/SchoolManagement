package com.nguyenanhtrung.schoolmanagement.data.repository.user

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.user.UserRemoteDataSource
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(private val userRemoteDataSource: UserRemoteDataSource): UserRepository {


    override suspend fun loadUserInfo(result: MutableLiveData<Resource<User>>) {
        result.value = Resource.loading()
        val userInfoResult = userRemoteDataSource.getUserInfo()
        result.value = userInfoResult
    }


}