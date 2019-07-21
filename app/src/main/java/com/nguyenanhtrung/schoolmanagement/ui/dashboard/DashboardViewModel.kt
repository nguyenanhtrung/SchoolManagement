package com.nguyenanhtrung.schoolmanagement.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserTaskItem
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUserInfoUseCase
import com.nguyenanhtrung.schoolmanagement.domain.usertask.GetUserTasksUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserTasksUseCase: GetUserTasksUseCase
) : BaseViewModel() {

    private val _userInfoLiveData by lazy {
        createApiResultLiveData<User>()
    }
    internal val userInfoLiveData: LiveData<Resource<User>>
        get() = _userInfoLiveData

    private val _userTasksLiveData by lazy {
        createApiResultLiveData<List<UserTaskItem>>()
    }
    internal val userTaskLiveData: LiveData<Resource<List<UserTaskItem>>>
        get() = _userTasksLiveData


    internal fun loadUserInfo() {
        if (_userInfoLiveData.value != null) {
            return
        }
        getUserInfoUseCase.invoke(viewModelScope, Unit, _userInfoLiveData)
    }

    internal fun loadUserTasks() {
        if (_userTasksLiveData.value?.data != null) {
            return
        }
        val userTypeId = getUserTypeId()
        getUserTasksUseCase.invoke(viewModelScope, userTypeId, _userTasksLiveData)
    }

    private fun getUserTypeId(): String {
        val userInfoResult = _userInfoLiveData.value
        val userInfo = _userInfoLiveData.value?.data
        if (userInfoResult != null && userInfo != null) {
            return userInfo.typeId
        }
        return ""
    }

    internal fun onClickTaskItem(userTask: UserTask) {

    }
}