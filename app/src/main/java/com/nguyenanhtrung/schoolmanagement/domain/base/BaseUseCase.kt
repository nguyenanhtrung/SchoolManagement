package com.nguyenanhtrung.schoolmanagement.domain.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseUseCase<in Params, Output> where Output : Any{

    operator fun invoke(
        coroutineScope: CoroutineScope,
        params: Params,
        resultLiveData: MutableLiveData<Resource<Output>>
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            resultLiveData.value = Resource.exception(exception)
        }

        coroutineScope.launch(handler) {
            execute(params, resultLiveData)
        }
    }

    protected abstract suspend fun execute(params: Params, resultLiveData: MutableLiveData<Resource<Output>>)
}