package com.nguyenanhtrung.schoolmanagement.util

import kotlinx.coroutines.CoroutineScope

abstract class NetworkBoundResources<in Params, out Output>
    constructor(private val coroutineScope: CoroutineScope) {

    

}