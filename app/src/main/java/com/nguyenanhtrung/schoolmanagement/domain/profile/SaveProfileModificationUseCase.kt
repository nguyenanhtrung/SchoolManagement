package com.nguyenanhtrung.schoolmanagement.domain.profile

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileModificationParams
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.repository.profile.ProfileRepository
import com.nguyenanhtrung.schoolmanagement.domain.base.BaseUseCase
import javax.inject.Inject

class SaveProfileModificationUseCase @Inject constructor(private val profileRepository: ProfileRepository) :
    BaseUseCase<ProfileModificationParams, String>() {


    override suspend fun execute(
        params: ProfileModificationParams,
        resultLiveData: MutableLiveData<Resource<String>>
    ) {
        profileRepository.saveProfileModified(params, resultLiveData)
    }
}