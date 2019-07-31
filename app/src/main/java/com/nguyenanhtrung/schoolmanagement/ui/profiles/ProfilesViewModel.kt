package com.nguyenanhtrung.schoolmanagement.ui.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.profile.GetFilterProfileDatasUseCase
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUsersByProfileStatusUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import java.util.logging.Filter
import javax.inject.Inject

class ProfilesViewModel @Inject constructor(
    private val getUsersByProfileStatusUseCase: GetUsersByProfileStatusUseCase,
    private val getFilterProfileDatasUseCase: GetFilterProfileDatasUseCase
) :
    BaseViewModel() {

    private var currentProfileFilter: ProfileFilter = ProfileFilter.All

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

    private val _filterProfileDatas by lazy {
        MutableLiveData<Resource<Array<FilterData>>>()
    }
    internal val filterProfileDatas: LiveData<Resource<Array<FilterData>>>
        get() = _filterProfileDatas

    private val _clearProfileItems by lazy {
        MutableLiveData<Boolean>()
    }
    internal val clearProfileItems: LiveData<Boolean>
        get() = _clearProfileItems

    internal fun loadProfiles() {
        val profiles = _userProfilesLiveData.value
        if (profiles != null) {
            return
        }
        val params = Pair(-1L, currentProfileFilter)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, _userProfilesLiveData)
    }

    internal fun onLoadMoreSuccess() {
        if (_stateLoadingMoreProfiles.value == Status.LOADING) {
            _stateLoadingMoreProfiles.value = Status.COMPLETE
        }
    }

    internal fun onLoadMoreProfiles(lastProfile: Profile) {
        val profileId = lastProfile.userId
        val params = Pair(profileId, currentProfileFilter)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, _userProfilesLiveData)
    }

    internal fun onClickItemFilterProfiles() {
        getFilterProfileDatasUseCase.invoke(viewModelScope, Unit, _filterProfileDatas)
    }

    internal fun onSelectedFilterItem(filterData: FilterData) {
        val selectedFilterState = filterData.state
        if (selectedFilterState is FilterState.Profile) {
            val profileStatus = selectedFilterState.status
            if (profileStatus == currentProfileFilter) {
                return
            }
            currentProfileFilter = profileStatus
            //reload all list
            _clearProfileItems.value = true
            val params = Pair(-1L, currentProfileFilter)
            getUsersByProfileStatusUseCase.invoke(viewModelScope, params, _userProfilesLiveData)
        }

    }

}