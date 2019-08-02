package com.nguyenanhtrung.schoolmanagement.domain.profile

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileDetail
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.profile.ProfileRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class GetProfileDetailUseCase @Inject constructor(private val profileRepository: ProfileRepository) :
    BaseUseCase<String, ProfileDetail>() {


    override suspend fun execute(
        params: String,
        resultLiveData: MutableLiveData<Resource<ProfileDetail>>
    ) {
        profileRepository.getProfileDetail(params, resultLiveData)
    }
}