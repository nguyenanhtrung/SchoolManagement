package com.nguyenanhtrung.schoolmanagement.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.nguyenanhtrung.schoolmanagement.domain.navigation.GetDestinationIdUseCase
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUserInfoUseCase
import com.nguyenanhtrung.schoolmanagement.domain.usertask.GetUserTasksUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import timber.log.Timber
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val getUserTasksUseCase: GetUserTasksUseCase,
    private val getDestinationIdUseCase: GetDestinationIdUseCase
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

    private val _navigationLiveData by lazy {
        MutableLiveData<Resource<Event<NavDirections>>>()
    }
    internal val navigationLiveData: LiveData<Resource<Event<NavDirections>>>
        get() = _navigationLiveData


    internal fun loadUserInfo() {
        val userInfoResource = _userInfoLiveData.value
        if (userInfoResource != null && userInfoResource.status == Status.SUCCESS) {
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
        Timber.d("UserTypeId = ${userInfo?.typeId}")
        return ""
    }

    internal fun onClickTaskItem(userTask: UserTask) {
        getDestinationIdUseCase.invoke(viewModelScope, userTask.id.toInt(), _navigationLiveData)
    }
}