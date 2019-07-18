package com.nguyenanhtrung.schoolmanagement.domain.usertask

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.nguyenanhtrung.schoolmanagement.data.repository.task.UserTaskRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetUserTasksUseCase @Inject constructor(private val userTaskRepository: UserTaskRepository) :
    BaseUseCase<String, MutableList<UserTaskItem>>() {


    override suspend fun execute(
        params: String,
        resultLiveData: MutableLiveData<Resource<MutableList<UserTaskItem>>>
    ) {
        userTaskRepository.loadUserTasks(params, resultLiveData)
    }
}