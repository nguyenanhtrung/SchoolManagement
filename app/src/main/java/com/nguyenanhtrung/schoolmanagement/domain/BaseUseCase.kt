package com.nguyenanhtrung.schoolmanagement.domain

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseUseCase<in Params, Output> where Output : Any{

    operator fun invoke(
        coroutineScope: CoroutineScope,
        params: Params,
        resultLiveData: LiveData<Result<Output>>
    ) {

        coroutineScope.launch {
            execute(params, resultLiveData)
        }
    }

    protected abstract suspend fun execute(params: Params, resultLiveData: LiveData<Result<Output>>)
}