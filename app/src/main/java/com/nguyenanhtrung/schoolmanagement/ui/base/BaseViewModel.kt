package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ApiResult
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val _loadingLiveData by lazy {
        MutableLiveData<ApiResult>()
    }
    internal val loadingLiveData: LiveData<ApiResult>
        get() = _loadingLiveData

    private val _errorLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val errorLiveData: LiveData<ErrorState>
        get() = _errorLiveData


    internal fun onNetworkStatusChanged(isConnected: Boolean) {
        if (isConnected) {
            _errorLiveData.value = ErrorState.NoAction("Kết nối mạng đã ngắt! Vui lòng kiểm tra kết nối.")
        }
    }
}