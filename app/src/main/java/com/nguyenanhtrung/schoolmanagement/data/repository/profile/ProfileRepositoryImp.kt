package com.nguyenanhtrung.schoolmanagement.data.repository.profile

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.profile.ProfileLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import javax.inject.Inject

class ProfileRepositoryImp @Inject constructor(private val profileLocalDataSource: ProfileLocalDataSource) :
    ProfileRepository {


    override fun getFilterProfileDatas(result: MutableLiveData<Resource<Array<FilterData>>>) {
        val filterDatas = profileLocalDataSource.getFilterProfileDatas()
        result.value = Resource.success(filterDatas)
    }
}