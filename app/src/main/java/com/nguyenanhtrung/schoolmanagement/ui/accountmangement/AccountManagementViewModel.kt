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

    private val _emptyUsersLiveData by lazy {
        MutableLiveData<EmptyItem>()
    }
    internal val emptyUsersLiveData: LiveData<EmptyItem>
        get() = _emptyUsersLiveData

    private val _errorUsersLiveData by lazy {
        MutableLiveData<Int>()
    }
    internal val errorUsersLiveData: LiveData<Int>
        get() = _errorUsersLiveData

    private val _usersLiveData by lazy {
        MutableLiveData<MutableList<UserItem>>()
    }
    internal val usersLiveData: LiveData<MutableList<UserItem>>
        get() = _usersLiveData

    private val _stateLoadMoreUsersLiveData by lazy {
        MutableLiveData<Status>()
    }
    internal val stateLoadMoreUsersLiveData: LiveData<Status>
        get() = _stateLoadMoreUsersLiveData

    internal fun loadUsers() {
        val getUsersResult = _getUsersLiveData.value
        if (getUsersResult != null) {
            return
        }
        getUsersUseCase.invoke(viewModelScope, -1, _getUsersLiveData)
    }

    internal fun handleStatusGetUsers(resource: Resource<MutableList<UserItem>>) {
        when (resource.status) {
            Status.EMPTY -> {
                _emptyUsersLiveData.value = EmptyItem(resource.error)
            }
            Status.FAILURE, Status.EXCEPTION -> {
                if (_errorUsersLiveData.value == null) {
                    _errorUsersLiveData.value = resource.error
                }
            }
            Status.SUCCESS -> {
                if (_stateLoadMoreUsersLiveData.value == Status.LOADING) {
                    _stateLoadMoreUsersLiveData.value = Status.COMPLETE
                }



                val users = resource.data
                users?.let {
                    _usersLiveData.value = it
                }
            }
            Status.COMPLETE -> {
                if (_stateLoadMoreUsersLiveData.value == Status.LOADING) {
                    _stateLoadMoreUsersLiveData.value = Status.COMPLETE
                }
            }
            else -> Unit
        }
    }

    internal fun onLoadMoreUsers(lastUserId: Long) {
        if (_stateLoadMoreUsersLiveData.value == Status.LOADING) {
            return
        }
        _stateLoadMoreUsersLiveData.value = Status.LOADING
        getUsersUseCase.invoke(viewModelScope, lastUserId, _getUsersLiveData)

    }

    internal fun onClickButtonRetry() {
        getUsersUseCase.invoke(viewModelScope, -1, _getUsersLiveData)
    }

    internal fun onClickButtonCreateAccount() {
        getMaxUserIdUseCase.invoke(viewModelScope, Unit, _maxUserIdLiveData)
    }

    internal fun onSuccessGetMaxUserId(id: Long) {
        _navToCreateAccountFragment.value = Event(id)
    }
}