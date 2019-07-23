package com.nguyenanhtrung.schoolmanagement.data.repository.userid

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.userid.UserIdRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import javax.inject.Inject

class UserIdRepositoryImp @Inject constructor(
    private val userIdRemoteDataSource: UserIdRemoteDataSource,
    @ApplicationContext private val context: Context
) :
    UserIdRepository {

    override suspend fun getMaxUserId(result: MutableLiveData<Resource<Long>>) {
        object : NetworkBoundResources<Unit, Long>(context, Unit, result) {
            override fun shouldLoadFromLocal(params: Unit): Boolean = false
            override suspend fun callApi(): Resource<Long> {
                return userIdRemoteDataSource.getMaxUserId()
            }
        }.createCall()
    }
}