package com.nguyenanhtrung.schoolmanagement.domain.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ResultModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseUseCase<in Params, Output> where Output : Any{

    operator fun invoke(
        coroutineScope: CoroutineScope,
        params: Params,
        resultLiveData: MutableLiveData<ResultModel<Output>>
    ) {
        val handler = CoroutineExceptionHandler { _, exception ->
            resultLiveData.value = ResultModel.Failure(exception.toString())
        }

        coroutineScope.launch(handler) {
            execute(params, resultLiveData)
        }
    }

    protected abstract suspend fun execute(params: Params, resultLiveData: MutableLiveData<ResultModel<Output>>)
}