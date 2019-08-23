package com.nguyenanhtrung.schoolmanagement.data.repository.profile

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.datasource.profile.ProfileLocalDataSource
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.data.remote.datasource.profile.ProfileRemoteDataSource
import com.nguyenanhtrung.schoolmanagement.di.ApplicationContext
import com.nguyenanhtrung.schoolmanagement.util.NetworkBoundResources
import javax.inject.Inject

class ProfileRepositoryImp @Inject constructor(
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    @ApplicationContext private val context: Context
) :
    ProfileRepository {

    override suspend fun saveProfileModified(
        profileModificationParams: ProfileModificationParams,
        result: MutableLiveData<Resource<String>>
    ) {
        object : NetworkBoundResources<ProfileModificationParams, String>(
            context,
            profileModificationParams,
            result
        ) {
            override fun shouldSaveToLocal(params: ProfileModificationParams): Boolean = false
            override fun shouldLoadFromLocal(params: ProfileModificationParams): Boolean = false

            override suspend fun callApi(): Resource<String> {
                return profileRemoteDataSource.saveProfileModified(params)
            }

        }.createCall()
    }

    override suspend fun getProfileDetail(
        fireBaseUserId: String,
        result: MutableLiveData<Resource<ProfileDetail>>
    ) {
        object : NetworkBoundResources<String, ProfileDetail>(context, fireBaseUserId, result) {

            override fun shouldLoadFromLocal(params: String): Boolean = false
            override fun shouldSaveToLocal(params: String): Boolean = false

            override suspend fun callApi(): Resource<ProfileDetail> {
                return profileRemoteDataSource.getProfileDetail(fireBaseUserId)
            }
        }.createCall()
    }

    override suspend fun updateUserProfile(
        profileUpdateParam: ProfileUpdateParam,
        result: MutableLiveData<Resource<String>>
    ) {
        object :
            NetworkBoundResources<ProfileUpdateParam, String>(context, profileUpdateParam, result) {

            override fun shouldLoadFromLocal(params: ProfileUpdateParam): Boolean = false
            override fun shouldSaveToLocal(params: ProfileUpdateParam): Boolean = false

            override suspend fun callApi(): Resource<String> {
                return profileRemoteDataSource.updateUserProfile(params)
            }

        }.createCall()
    }

    override fun getFilterProfileDatas(result: MutableLiveData<Resource<Array<FilterData>>>) {
        val filterDatas = profileLocalDataSource.getFilterProfileDatas()
        result.value = Resource.success(filterDatas)
    }
}