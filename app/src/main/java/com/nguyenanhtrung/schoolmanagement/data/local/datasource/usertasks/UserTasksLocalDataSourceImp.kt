package com.nguyenanhtrung.schoolmanagement.data.local.datasource.usertasks

import com.nguyenanhtrung.schoolmanagement.data.local.database.dao.usertasks.UserTaskDao
import com.nguyenanhtrung.schoolmanagement.data.local.entity.UserTaskEntity
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import javax.inject.Inject

class UserTasksLocalDataSourceImp @Inject constructor(private val userTaskDao: UserTaskDao) :
    UserTasksLocalDataSource {


    override suspend fun saveUserTasks(userTypeId: String, userTasks: List<UserTask>) {
        val userTaskEntities = userTasks.map {
            UserTaskEntity(it.id, it.name, it.iconPathId, userTypeId)
        }
        userTaskDao.insertListData(userTaskEntities)
    }

    override suspend fun checkUserTaskSaved(userTypeId: String): Boolean {
        return userTaskDao.checkUserTasksSaved(userTypeId) == 1
    }

    override suspend fun getUserTasks(userTypeId: String): Resource<List<UserTaskItem>> {
        val userTaskEntities = userTaskDao.getUserTasks(userTypeId)
        val mappedUserTasks = userTaskEntities.map {
            UserTaskItem(
                UserTask(it.id, it.name, it.iconId)
            )
        }
        return Resource.success(mappedUserTasks)
    }
}