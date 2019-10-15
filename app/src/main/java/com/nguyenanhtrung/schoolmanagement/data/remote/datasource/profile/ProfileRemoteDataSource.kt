package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile

import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.xwray.groupie.kotlinandroidextensions.Item

interface ProfileRemoteDataSource {

    suspend fun updateUserProfile(
        profileUpdateParam: ProfileUpdateParam
    ): Resource<String>

    suspend fun getProfileDetail(fireBaseUserId: String): Resource<ProfileDetail>

    suspend fun saveProfileModified(profileModificationParams: ProfileModificationParams)
            : Resource<String>

    suspend fun getProfiles(
        userTypes: Map<String, String>,
        profileFilter: ProfileFilter
    ): Resource<MutableList<out GenericItem>>

    suspend fun getPagingUsesByProfileStatus(
        lastUserId: Long,
        userTypes: Map<String, String>,
        profileFilter: ProfileFilter
    ): Resource<MutableList<out GenericItem>>
}