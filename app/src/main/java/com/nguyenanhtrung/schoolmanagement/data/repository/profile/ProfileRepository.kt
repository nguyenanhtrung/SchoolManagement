package com.nguyenanhtrung.schoolmanagement.data.repository.profile

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource

interface ProfileRepository {

    fun getFilterProfileDatas(result: MutableLiveData<Resource<Array<FilterData>>>)
}