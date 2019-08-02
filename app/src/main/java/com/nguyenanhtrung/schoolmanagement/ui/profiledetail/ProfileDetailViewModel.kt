package com.nguyenanhtrung.schoolmanagement.ui.profiledetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.ProfileDetail
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.domain.profile.GetProfileDetailUseCase
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileViewModel
import javax.inject.Inject

class ProfileDetailViewModel @Inject constructor(private val getProfileDetailUseCase: GetProfileDetailUseCase) :
    BaseProfileViewModel() {

    private val _profileDetail by lazy {
        createApiResultLiveData<ProfileDetail>()
    }
    internal val profileDetail: LiveData<Resource<ProfileDetail>>
        get() = _profileDetail

    internal fun loadProfileDetail() {
        val firebaseUserId = profile.fireBaseUserId
        getProfileDetailUseCase.invoke(viewModelScope, firebaseUserId, _profileDetail)
    }
}