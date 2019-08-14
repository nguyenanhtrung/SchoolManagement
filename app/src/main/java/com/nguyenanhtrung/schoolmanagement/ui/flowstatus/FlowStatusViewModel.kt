package com.nguyenanhtrung.schoolmanagement.ui.flowstatus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.FlowStatusInfo
import com.nguyenanhtrung.schoolmanagement.data.local.model.FlowStatusViewState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class FlowStatusViewModel @Inject constructor() : BaseViewModel() {
    lateinit var flowStatusInfo: FlowStatusInfo

    private val _viewDatas by lazy {
        MutableLiveData<FlowStatusViewState>()
    }
    internal val viewDatas: LiveData<FlowStatusViewState>
        get() = _viewDatas

    internal fun showViewDatas() {
        val messageId = flowStatusInfo.messageId
        var buttonNameId = -1
        val status = flowStatusInfo.status
        if (status == Status.SUCCESS) {
            buttonNameId = flowStatusInfo.buttonNameId
        }
        val flowStatusViewState = FlowStatusViewState(messageId, buttonNameId)
        _viewDatas.value = flowStatusViewState
    }

}