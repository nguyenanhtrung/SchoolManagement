package com.nguyenanhtrung.schoolmanagement.domain.navigation

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import com.nguyenanhtrung.schoolmanagement.ui.dashboard.DashboardFragmentDirections
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import javax.inject.Inject

class
GetDestinationIdUseCase @Inject constructor() : BaseUseCase<Int, Event<NavDirections>>() {


    override suspend fun execute(
        params: Int,
        resultLiveData: MutableLiveData<Resource<Event<NavDirections>>>
    ) {
        when (params) {
            AppKey.ID_TASK_ACCOUNT -> resultLiveData.value =
                Resource.success(Event(DashboardFragmentDirections.actionDashboardFragmentToAccountManagementFragment()))
            AppKey.ID_TASK_PROFILES -> resultLiveData.value =
                Resource.success(Event(DashboardFragmentDirections.actionDashboardDestToProfilesDest()))
        }
    }

}