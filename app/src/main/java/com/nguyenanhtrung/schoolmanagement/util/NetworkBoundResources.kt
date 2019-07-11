package com.nguyenanhtrung.schoolmanagement.util

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResources<Params, Output>
constructor(
    protected val params: Params,
    private val result: MutableLiveData<ResultModel<Output>>
) where Output : Any {


    internal suspend fun createCall() {
            if (shouldFetchFromServer(params)) {
                result.value = ResultModel.Loading
                //call api
                val response: ResultModel<Output> = callApi()
                if (response is ResultModel.Success && shouldSaveToLocal(params)) {
                    withContext(Dispatchers.IO) {
                        saveToLocal(response.value)
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
    protected abstract fun shouldFetchFromServer(params: Params): Boolean

    @WorkerThread
    protected abstract suspend fun callApi(): ResultModel<Output>

    @MainThread
    protected abstract fun shouldSaveToLocal(params: Params): Boolean

    @WorkerThread
    protected abstract suspend fun saveToLocal(output: Output)

    @WorkerThread
    protected abstract suspend fun loadFromLocal(params: Params): ResultModel<Output>
}