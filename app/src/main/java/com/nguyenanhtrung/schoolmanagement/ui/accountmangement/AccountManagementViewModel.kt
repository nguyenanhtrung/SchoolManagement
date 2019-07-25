package com.nguyenanhtrung.schoolmanagement.ui.accountmangement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUsersUseCase
import com.nguyenanhtrung.schoolmanagement.domain.userid.GetMaxUserIdUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class AccountManagementViewModel @Inject constructor(
    private val getMaxUserIdUseCase: GetMaxUserIdUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : BaseViewModel() {

    private val _navToCreateAccountFragment by lazy {
        MutableLiveData<Event<Long>>()
    }
    internal val navToCreateAccountFragment: LiveData<Event<Long>>
        get() = _navToCreateAccountFragment

    private val _maxUserIdLiveData by lazy {
        createApiResultLiveData<Long>()
    }
    internal val maxUserIdLiveData: LiveData<Resource<Long>>
        get() = _maxUserIdLiveData

    private val _getUsersLiveData by lazy {
        createApiResultLiveData<MutableList<UserItem>>()
    }
    internal val getUsersLiveData: LiveData<Resource<MutableList<UserItem>>>
        get() = _getUsersLiveData

    private val _stateUsersLiveData by lazy {
        MutableLiveData<MutableList<Item>>()
    }
    internal val stateUsersLiveData: LiveData<MutableList<Item>>
        get() = _stateUsersLiveData

    private val _errorUsersLiveData by lazy {
        MutableLiveData<Int>()
    }
    internal val errorUsersLiveData: LiveData<Int>
        get() = _errorUsersLiveData

    internal fun loadUsers() {
        val getUsersResult = _getUsersLiveData.value
        if (getUsersResult != null) {
            return
        }
        getUsersUseCase.invoke(viewModelScope, null, _getUsersLiveData)
    }

    internal fun handleStatusGetUsers(resource: Resource<MutableList<UserItem>>) {
        when (resource.status) {
            Status.EMPTY -> {
                _stateUsersLiveData.value = mutableListOf(EmptyItem(resource.error))
            }
            Status.FAILURE, Status.EXCEPTION -> {
                _errorUsersLiveData.value = resource.error
            }
            Status.SUCCESS -> {
                val users = resource.data
                users?.let {
                    _stateUsersLiveData.value?.addAll(users)
                }
            }
            else -> Unit
        }
    }

    internal fun onClickButtonCreateAccount() {
        getMaxUserIdUseCase.invoke(viewModelScope, Unit, _maxUserIdLiveData)
    }

    internal fun onSuccessGetMaxUserId(id: Long) {
        _navToCreateAccountFragment.value = Event(id)
    }
}