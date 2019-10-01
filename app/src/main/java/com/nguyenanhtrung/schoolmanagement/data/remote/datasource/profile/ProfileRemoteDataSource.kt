package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile

import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.xwray.groupie.kotlinandroidextensions.Item

interface ProfileRemoteDataSource {

    suspend fun updateUserProfile(
        profileUpdateParam: ProfileUpdateParam
    ): Resource<String>

    suspend fun getProfileDetail(fireBaseUserId: String): Resource<ProfileDetail>

    suspend fun saveProfileModified(profileModificationParams: ProfileModificationParams)
            : Resource<String>

    suspend fun getProfiles(lastUserId: Long,
                            userTypes: Map<String, String>,
                            profileFilter: ProfileFilter
    ): Resource<MutableList<out Item>>
}