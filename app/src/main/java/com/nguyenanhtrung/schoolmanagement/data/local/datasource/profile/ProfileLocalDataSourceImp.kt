package com.nguyenanhtrung.schoolmanagement.data.local.datasource.profile

import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterState
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileFilter
import javax.inject.Inject

class ProfileLocalDataSourceImp @Inject constructor() : ProfileLocalDataSource {

    override fun getFilterProfileDatas(): Array<FilterData> {
        return arrayOf(
            FilterData(
                R.string.title_all,
                FilterState.Profile(ProfileFilter.All),
                true
            ),

            FilterData(
                R.string.title_updated,
                FilterState.Profile(ProfileFilter.Updated)
            ),

            FilterData(
                R.string.title_not_update,
                FilterState.Profile(ProfileFilter.NoUpdate)
            )

        )
    }
}