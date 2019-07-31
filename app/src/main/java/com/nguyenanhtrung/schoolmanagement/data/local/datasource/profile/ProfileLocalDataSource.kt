package com.nguyenanhtrung.schoolmanagement.data.local.datasource.profile

import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData

interface ProfileLocalDataSource {

    fun getFilterProfileDatas(): Array<FilterData>
}