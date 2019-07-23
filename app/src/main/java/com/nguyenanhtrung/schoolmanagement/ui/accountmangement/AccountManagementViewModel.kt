package com.nguyenanhtrung.schoolmanagement.ui.accountmangement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.domain.userid.GetMaxUserIdUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class AccountManagementViewModel @Inject constructor(private val getMaxUserIdUseCase: GetMaxUserIdUseCase) : BaseViewModel() {

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

    internal fun onClickButtonCreateAccount() {
        getMaxUserIdUseCase.invoke(viewModelScope, Unit, _maxUserIdLiveData)
    }

    internal fun onSuccessGetMaxUserId(id: Long) {
        _navToCreateAccountFragment.value = Event(id)
    }
}