package com.nguyenanhtrung.schoolmanagement.ui.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUsersByProfileStatusUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class ProfilesViewModel @Inject constructor(private val getUsersByProfileStatusUseCase: GetUsersByProfileStatusUseCase) :
    BaseViewModel() {

    private val _userProfilesLiveData by lazy {
        createApiResultLiveData<MutableList<ProfileItem>>()
    }
    internal val userProfilesLiveData: LiveData<Resource<MutableList<ProfileItem>>>
        get() = _userProfilesLiveData

    private val _stateLoadingMoreProfiles by lazy {
        MutableLiveData<Status>()
    }
    internal val stateLoadingMoreProfiles: LiveData<Status>
        get() = _stateLoadingMoreProfiles

    internal fun loadProfiles() {
        val profiles = _userProfilesLiveData.value
        if (profiles != null) {
            return
        }
        val params = Pair(-1L, ProfileFilter.All)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, _userProfilesLiveData)
    }

    internal fun onLoadMoreSuccess() {
        if (_stateLoadingMoreProfiles.value == Status.LOADING) {
            _stateLoadingMoreProfiles.value = Status.COMPLETE
        }
    }

    internal fun onLoadMoreProfiles(lastProfile: Profile) {
        val profileId = lastProfile.userId
        val params = Pair(profileId, ProfileFilter.All)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, _userProfilesLiveData)
    }


}