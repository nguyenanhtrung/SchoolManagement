package com.nguyenanhtrung.schoolmanagement.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResources<Params, Output>
constructor(
    protected val params: Params,
    private val result: MutableLiveData<Resource<Output>>
) where Output : Any {


    internal suspend fun createCall() {
            if (shouldFetchFromServer(params)) {
                result.value = Resource.loading()
                //call api
                val response: Resource<Output> = callApi()
                if (response.status == Status.SUCCESS && shouldSaveToLocal(params)) {
                    withContext(Dispatchers.IO) {
                        val data = response.data
                        if (data != null) {
                            saveToLocal(response.data)
                        }
                    }
                }
                result.value = response
            } else {
                val dataFromLocal = withContext(Dispatchers.IO) {
                    loadFromLocal(params)
                }
                result.value = dataFromLocal
            }


    }

    @MainThread
    protected fun shouldFetchFromServer(params: Params): Boolean = true

    @WorkerThread
    protected abstract suspend fun callApi(): Resource<Output>

    @MainThread
    protected fun shouldSaveToLocal(params: Params): Boolean = false

    @WorkerThread
    protected suspend fun saveToLocal(output: Output) = Unit

    @WorkerThread
    protected suspend fun loadFromLocal(params: Params): Resource<Output>
        = Resource.loading()
}