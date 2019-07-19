package com.nguyenanhtrung.schoolmanagement.data.repository.task

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertasks.UserTasksLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.task.UserTaskRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import javax.inject.Inject

class UserTaskRepositoryImp @Inject constructor(
    private val userTaskRemoteDataSource: UserTaskRemoteDataSource,
    private val userTasksLocalDataSource: UserTasksLocalDataSource,
    @ApplicationContext private val context: Context
) :
    UserTaskRepository {

    override suspend fun loadUserTasks(
        taskId: String,
        result: MutableLiveData<Resource<List<UserTaskItem>>>
    ) {
        object : NetworkBoundResources<String, List<UserTaskItem>>(context, taskId, result) {
            override suspend fun callApi(): Resource<List<UserTaskItem>> =
                userTaskRemoteDataSource.loadUserTasksAsync(taskId)

            override suspend fun shouldFetchFromServer(params: String): Boolean {
                return !userTasksLocalDataSource.checkUserTaskSaved(params)
            }

            override fun shouldSaveToLocal(params: String): Boolean = true

            override suspend fun loadFromLocal(params: String): Resource<List<UserTaskItem>> {
                return userTasksLocalDataSource.getUserTasks(params)
            }

            override suspend fun saveToLocal(output: List<UserTaskItem>) {
                userTasksLocalDataSource.saveUserTasks(taskId, output.map {
                    it.userTask
                })
            }

        }.createCall()
    }
}