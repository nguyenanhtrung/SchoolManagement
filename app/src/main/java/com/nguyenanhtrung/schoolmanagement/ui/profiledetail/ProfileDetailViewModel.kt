package com.nguyenanhtrung.schoolmanagement.ui.profiledetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.ModificationState
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

    private val _stateModifyProfileInfo by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateModifyProfileInfo: LiveData<ModificationState>
        get() = _stateModifyProfileInfo

    internal fun onClickButtonEditProfile() {
        _stateModifyProfileInfo.value = ModificationState.Edit
    }

    internal fun loadProfileDetail() {
        val fireBaseUserId = profile.fireBaseUserId
        getProfileDetailUseCase.invoke(viewModelScope, fireBaseUserId, _profileDetail)
    }

    internal fun initStateModifyProfileInfo() {
        _stateModifyProfileInfo.value = ModificationState.Save
    }
}