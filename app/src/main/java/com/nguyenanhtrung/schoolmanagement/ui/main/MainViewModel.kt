package com.nguyenanhtrung.schoolmanagement.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData
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
        MutableLiveData<Int>()
    }
    internal val profileUpdated: LiveData<Int>
        get() = mutableProfileUpdated

    val mutableAccountEvent by lazy {
        MutableLiveData<User>()
    }
    val accountEvent: LiveData<User>
        get() = mutableAccountEvent




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