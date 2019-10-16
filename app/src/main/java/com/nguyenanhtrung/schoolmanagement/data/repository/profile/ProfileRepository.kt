package com.nguyenanhtrung.schoolmanagement.data.repository.profile

import androidx.lifecycle.MutableLiveData
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.xwray.groupie.kotlinandroidextensions.Item

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

    suspend fun getPagingUserByProfileFilter(
        lastUserId: Long,
        params: Pair<ProfileFilter, Map<String, String>>,
        result: MutableLiveData<Resource<MutableList<ProfileItem>>>
    )

    suspend fun getUsersByProfileFilter(
        params: Pair<ProfileFilter, Map<String, String>>,
        result: MutableLiveData<Resource<MutableList<ProfileItem>>>
    )
}