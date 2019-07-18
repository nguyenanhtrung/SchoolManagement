package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem

interface UserTaskRemoteDataSource {

    suspend fun loadUserTasksAsync(
        userTypeId: String,
        result: MutableLiveData<Resource<MutableList<UserTaskItem>>>
    )
}