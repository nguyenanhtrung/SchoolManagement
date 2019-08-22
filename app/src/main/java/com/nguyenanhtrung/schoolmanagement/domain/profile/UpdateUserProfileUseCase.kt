package com.nguyenanhtrung.schoolmanagement.domain.profile

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileUpdateParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.profile.ProfileRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) :
    BaseUseCase<ProfileUpdateParam, String>() {


    override suspend fun execute(
        params: ProfileUpdateParam,
        resultLiveData: MutableLiveData<Resource<String>>
    ) {
        profileRepository.updateUserProfile(params, resultLiveData)
    }
}