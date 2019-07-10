package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState

abstract class BaseActivityViewModel() : BaseViewModel() {

    internal fun onNetworkStatusChanged(isConnected: Boolean) {
        if (isConnected) {
            val errorState = ErrorState.NoAction("Kết nối mạng đã ngắt! Vui lòng kiểm tra kết nối.")
            showError(errorState)
        }
    }
}