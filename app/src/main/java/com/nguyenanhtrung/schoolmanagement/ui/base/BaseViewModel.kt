package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ApiResult

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val _loadingLiveData by lazy {
        MutableLiveData<ApiResult>()
    }

    internal val loadingLiveData: LiveData<ApiResult>
        get() = _loadingLiveData

}