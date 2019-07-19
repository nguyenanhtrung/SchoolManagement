package com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertasks

import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask

interface UserTasksLocalDataSource {

    suspend fun saveUserTasks(userTypeId: String, userTasks: List<UserTask>)

    suspend fun getUserTasks(userTypeId: String): Resource<List<UserTaskItem>>

    suspend fun checkUserTaskSaved(userTypeId: String): Boolean
}