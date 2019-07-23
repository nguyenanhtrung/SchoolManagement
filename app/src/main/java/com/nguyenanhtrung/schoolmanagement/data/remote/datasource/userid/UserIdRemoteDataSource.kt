package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.userid

import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource

interface UserIdRemoteDataSource {

    suspend fun getMaxUserId(): Resource<Long>
}