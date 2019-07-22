package com.nguyenanhtrung.schoolmanagement.data.repository.usertype

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType

interface UserTypeRepository {

    suspend fun loadUserTypes(shouldLoadLocal: Boolean, result: MutableLiveData<Resource<List<UserType>>>)
}