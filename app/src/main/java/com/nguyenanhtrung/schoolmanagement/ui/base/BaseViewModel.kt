package com.nguyenanhtrung.schoolmanagement.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status

abstract class BaseViewModel : ViewModel() {

    val viewStateLiveData by lazy {
        MediatorLiveData<Resource<*>>()
    }

    protected val _loadingLiveData by lazy {
        MutableLiveData<Boolean>()
    }
    internal val loadingLiveData: LiveData<Boolean>
        get() = _loadingLiveData

    protected val _errorLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val errorLiveData: LiveData<ErrorState>
        get() = _errorLiveData


    internal fun setLoading(isLoading: Boolean) {
        _loadingLiveData.value = isLoading
    }

    internal fun showError(errorState: ErrorState) {
        _errorLiveData.value = errorState
    }

    protected fun<T> createApiResultLiveData(): MutableLiveData<Resource<T>> where T: Any{
        val apiResultLiveData = MutableLiveData<Resource<T>>()
        viewStateLiveData.addSource(apiResultLiveData) {
            viewStateLiveData.value = it
        }
        return apiResultLiveData
    }

    internal fun handleViewState(resource: Resource<*>) {
        when(resource.status) {
            Status.EMPTY -> _loadingLiveData.value = false
            Status.SUCCESS -> _loadingLiveData.value = false
            Status.LOADING -> _loadingLiveData.value = true
            Status.FAILURE -> {
                _loadingLiveData.value = false
                handleErrorState(resource)
            }
            Status.EXCEPTION -> {
                _loadingLiveData.value = false
            }
            else -> _loadingLiveData.value = false
        }
    }

    protected open fun handleErrorState(resource: Resource<*>) {
        _errorLiveData.value = ErrorState.NoAction(resource.error)
    }

}