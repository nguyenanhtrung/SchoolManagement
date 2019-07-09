package com.nguyenanhtrung.schoolmanagement.domain

import com.nguyenanhtrung.schoolmanagement.data.local.model.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseUseCase<in Params, out Output> {

    operator fun invoke(
        coroutineScope: CoroutineScope,
        params: Params,
        onComplete: (ApiResult) -> Unit = {}
    ) {

        coroutineScope.launch {

        }
    }

    protected abstract suspend fun execute(params: Params): ApiResult
}