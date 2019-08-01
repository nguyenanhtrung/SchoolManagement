package com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile

import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileUpdateParam
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource

interface ProfileRemoteDataSource {

    suspend fun updateUserProfile(
        profileUpdateParam: ProfileUpdateParam
    ): Resource<Unit>
}