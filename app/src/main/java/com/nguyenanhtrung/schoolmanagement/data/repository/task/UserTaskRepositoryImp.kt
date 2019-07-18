package com.nguyenanhtrung.schoolmanagement.data.repository.task

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task.UserTaskRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import javax.inject.Inject

class UserTaskRepositoryImp @Inject constructor(private val userTaskRemoteDataSource: UserTaskRemoteDataSource) :
    UserTaskRepository {

    override suspend fun loadUserTasks(
        taskId: String,
        result: MutableLiveData<Resource<MutableList<UserTaskItem>>>
    ) {
        userTaskRemoteDataSource.loadUserTasksAsync(taskId, result)
    }
}