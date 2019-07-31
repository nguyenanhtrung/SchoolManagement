package com.nguyenanhtrung.schoolmanagement.domain.profile

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.profile.ProfileRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetFilterProfileDatasUseCase @Inject constructor(private val profileRepository: ProfileRepository) :
    BaseUseCase<Unit, Array<FilterData>>() {

    override suspend fun execute(
        params: Unit,
        resultLiveData: MutableLiveData<Resource<Array<FilterData>>>
    ) {
        profileRepository.getFilterProfileDatas(result = resultLiveData)
    }
}