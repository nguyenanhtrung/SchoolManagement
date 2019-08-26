package com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel

abstract class BaseSchoolRoomViewModel : BaseViewModel() {

    private val _stateErrorInputRoomId by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val stateErrorInputRoomId: LiveData<ErrorState>
        get() = _stateErrorInputRoomId

    private val _stateErrorInputRoomName by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val stateErrorInputRoomName: LiveData<ErrorState>
        get() = _stateErrorInputRoomName


}