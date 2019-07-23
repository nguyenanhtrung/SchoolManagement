package com.nguyenanhtrung.schoolmanagement.data.repository.userid

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource

interface UserIdRepository {

    suspend fun getMaxUserId(result: MutableLiveData<Resource<Long>>)
}