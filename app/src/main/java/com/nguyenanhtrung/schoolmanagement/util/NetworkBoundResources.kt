package com.nguyenanhtrung.schoolmanagement.util

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResources<Params, Output>
constructor(
    private val context: Context,
    protected val params: Params,
    private val result: MutableLiveData<Resource<Output>>
) where Output : Any {


    internal suspend fun createCall() {
        if (!NetworkUtils.isNetworkConnected(context)) {
            result.value = Resource.failure(R.string.error_network)
            return
        }


        if (shouldFetchFromServer(params)) {
            result.value = Resource.loading()
            //call api
            val response: Resource<Output> = callApi()
            if (response.status == Status.SUCCESS && shouldSaveToLocal(params)) {
                val data = response.data
                if (data != null) {
                    saveToLocal(response.data)
                }
            }
            result.value = response
        } else {
            if (!shouldLoadFromLocal(params)) {
                result.value = Resource.completed()
                return
            }
            val dataFromLocal = withContext(Dispatchers.IO) {
                loadFromLocal(params)
            }
            result.value = dataFromLocal
            return
        }


    }

    @MainThread
    protected open fun shouldLoadFromLocal(params: Params): Boolean = true

    @MainThread
    protected open suspend fun shouldFetchFromServer(params: Params): Boolean = true

    @WorkerThread
    protected abstract suspend fun callApi(): Resource<Output>

    @MainThread
    protected open fun shouldSaveToLocal(params: Params): Boolean = false

    @WorkerThread
    protected open suspend fun saveToLocal(output: Output) = Unit

    @WorkerThread
    protected open suspend fun loadFromLocal(params: Params): Resource<Output> = Resource.loading()
}