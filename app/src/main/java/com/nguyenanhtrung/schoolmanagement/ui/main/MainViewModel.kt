package com.nguyenanhtrung.schoolmanagement.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor() : BaseActivityViewModel() {

    private val _toolbarVisibility by lazy {
        MutableLiveData<Boolean>()
    }
    internal val toolbarVisibility: LiveData<Boolean>
        get() = _toolbarVisibility

    val mutableFilterItem by lazy {
        MutableLiveData<FilterData>()
    }
    internal val filterItemLiveData: LiveData<FilterData>
        get() = mutableFilterItem

    val mutableProfileUpdated by lazy {
        MutableLiveData<Event<String>>()
    }
    internal val profileUpdated: LiveData<Event<String>>
        get() = mutableProfileUpdated

    val mutableAccountEvent by lazy {
        MutableLiveData<Event<User>>()
    }
    val accountEvent: LiveData<Event<User>>
        get() = mutableAccountEvent

    val mutableSupportMainToolbar by lazy {
        MutableLiveData<Boolean>()
    }

    val supportMainToolbar: LiveData<Boolean>
        get() = mutableSupportMainToolbar

    val updateAccountInfo by lazy {
        MutableLiveData<Event<User>>()
    }
    val stateModifyAccInfo: LiveData<Event<User>>
        get() = updateAccountInfo

    val eventClickUpdateProfile by lazy {
        MutableLiveData<Event<Unit>>()
    }
    val observableClickUpdateProfile: LiveData<Event<Unit>>
        get() = eventClickUpdateProfile


    val stateAddSchoolRoom by lazy {
        MutableLiveData<Event<SchoolRoom>>()
    }


    fun notifySuccessAddSchoolRoom(schoolRoom: SchoolRoom) {
        stateAddSchoolRoom.value = Event(schoolRoom)
    }


    fun hideToolbar() {
        if (_toolbarVisibility.value == true || _toolbarVisibility.value == null) {
            _toolbarVisibility.value = false
        }
    }

    internal fun showToolbar() {
        if (_toolbarVisibility.value == null || _toolbarVisibility.value == false) {
            _toolbarVisibility.value = true
        }
    }

}