package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    val viewStateLiveData by lazy {
        MediatorLiveData<ResultModel<Any>>()
    }

    private val _loadingLiveData by lazy {
        MutableLiveData<ResultModel<Any>>()
    }
    internal val loadingLiveData: LiveData<ResultModel<Any>>
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

    internal fun setLoading(apiResult: ResultModel<Any>) {
        _loadingLiveData.value = apiResult
    }

    internal fun showError(errorState: ErrorState) {
        _errorLiveData.value = errorState
    }

    protected fun createApiResultLiveData(): MutableLiveData<ResultModel<Any>> {
        val apiResultLiveData = MutableLiveData<ResultModel<Any>>()
        viewStateLiveData.addSource(apiResultLiveData) {
            viewStateLiveData.value = it
        }
        return apiResultLiveData
    }

    internal fun handleViewState(resultModel: ResultModel<Any>) {
        when(resultModel) {
            is ResultModel.Loading -> _loadingLiveData.value = resultModel
            is ResultModel.Failure -> _errorLiveData.value = ErrorState.NoAction(resultModel.error)
        }
    }

}