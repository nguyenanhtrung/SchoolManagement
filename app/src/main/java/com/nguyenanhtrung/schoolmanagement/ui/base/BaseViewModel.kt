package com.nguyenanhtrung.schoolmanagement.ui.base

import android.app.Application
import androidx.lifecycle.*
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState

abstract class BaseViewModel() : ViewModel() {

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