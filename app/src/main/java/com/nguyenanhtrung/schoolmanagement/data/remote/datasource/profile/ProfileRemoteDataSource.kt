package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile

import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileDetail
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileModificationParams
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileUpdateParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource

interface ProfileRemoteDataSource {

    suspend fun updateUserProfile(
        profileUpdateParam: ProfileUpdateParam
    ): Resource<String>

    suspend fun getProfileDetail(fireBaseUserId: String): Resource<ProfileDetail>

    suspend fun saveProfileModified(profileModificationParams: ProfileModificationParams)
            : Resource<String>
}