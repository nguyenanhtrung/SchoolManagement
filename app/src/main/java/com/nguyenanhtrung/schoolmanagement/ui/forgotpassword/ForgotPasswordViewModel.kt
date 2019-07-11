package com.nguyenanhtrung.schoolmanagement.ui.forgotpassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class ForgotPasswordViewModel @Inject constructor() : BaseViewModel() {

    private val _dismissDialogLiveData by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val dismissDialogLiveData: LiveData<Event<Boolean>>
        get() = _dismissDialogLiveData

    internal fun onClickButtonClose() {
        _dismissDialogLiveData.value = Event(true)
    }
}