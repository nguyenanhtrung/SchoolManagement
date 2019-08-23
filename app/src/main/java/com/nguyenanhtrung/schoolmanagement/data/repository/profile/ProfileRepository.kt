package com.nguyenanhtrung.schoolmanagement.data.repository.profile

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.*

interface ProfileRepository {

    fun getFilterProfileDatas(result: MutableLiveData<Resource<Array<FilterData>>>)

    suspend fun updateUserProfile(
        profileUpdateParam: ProfileUpdateParam,
        result: MutableLiveData<Resource<String>>
    )

    suspend fun getProfileDetail(
        fireBaseUserId: String,
        result: MutableLiveData<Resource<ProfileDetail>>
    )

    suspend fun saveProfileModified(
        profileModificationParams: ProfileModificationParams,
        result: MutableLiveData<Resource<String>>
    )
}