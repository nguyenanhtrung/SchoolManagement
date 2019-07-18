package com.nguyenanhtrung.schoolmanagement.data.repository.task

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask

interface UserTaskRepository {

    suspend fun loadUserTasks(taskId: String, result: MutableLiveData<Resource<MutableList<UserTaskItem>>>)
}