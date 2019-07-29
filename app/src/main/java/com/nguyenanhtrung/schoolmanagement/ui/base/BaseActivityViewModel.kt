package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState

abstract class BaseActivityViewModel() : BaseViewModel() {

    internal fun onNetworkStatusChanged(isConnected: Boolean) {
        if (!isConnected) {
            val errorState = ErrorState.NoAction(R.string.error_network)
            showError(errorState)
        }
    }

    fun showMessage(messageId: Int) {
        _messageLiveData.value = messageId
    }

}