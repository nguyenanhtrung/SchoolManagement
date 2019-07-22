package com.nguyenanhtrung.schoolmanagement.ui.accountmangement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class AccountManagementViewModel @Inject constructor() : BaseViewModel() {

    private val _navToCreateAccountFragment by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val navToCreateAccountFragment: LiveData<Event<Boolean>>
        get() = _navToCreateAccountFragment

    internal fun onClickButtonCreateAccount() {
        _navToCreateAccountFragment.value = Event(true)
    }
}