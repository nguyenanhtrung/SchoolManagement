package com.nguyenanhtrung.schoolmanagement.ui.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.profile.GetFilterProfileDatasUseCase
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUsersByProfileStatusUseCase
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import java.util.*
import javax.inject.Inject

class ProfilesViewModel @Inject constructor(
    private val getUsersByProfileStatusUseCase: GetUsersByProfileStatusUseCase,
    private val getFilterProfileDatasUseCase: GetFilterProfileDatasUseCase
) : BaseListItemViewModel<ProfileItem>() {


    private var currentProfileFilter: ProfileFilter = ProfileFilter.All

    private val _filterProfileDatas by lazy {
        MutableLiveData<Resource<Array<FilterData>>>()
    }
    internal val filterProfileDatas: LiveData<Resource<Array<FilterData>>>
        get() = _filterProfileDatas

    private val _profileUpdateScreen by lazy {
        MutableLiveData<Event<Pair<Int, Profile>>>()
    }
    internal val profileUpdateScreen: LiveData<Event<Pair<Int, Profile>>>
        get() = _profileUpdateScreen

    private val _profileDetailScreen by lazy {
        MutableLiveData<Event<Profile>>()
    }
    internal val profileDetailScreen: LiveData<Event<Profile>>
        get() = _profileDetailScreen

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
            _clearItemsLiveData.value = true
            val params = Pair(-1L, currentProfileFilter)
            getUsersByProfileStatusUseCase.invoke(viewModelScope, params, _getItemsLiveData)
        }
    }


    override fun onCustomClickItem(position: Int) {
        val profileItem: ProfileItem = getItem(position)
        val profile = profileItem.profile
        _profileDetailScreen.value = Event(profile)
    }

    override fun customCheckItemWithQuery(query: String, item: GenericItem): Boolean {
        val profileItem = item as ProfileItem
        val name = profileItem.profile.name
        return name.toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))
    }

    override fun loadMoreItems(
        lastItem: ProfileItem,
        itemsLiveData: MutableLiveData<Resource<MutableList<ProfileItem>>>
    ) {
        val lastProfile = lastItem.profile
        val profileId = lastProfile.userId
        val params = Pair(profileId, currentProfileFilter)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, itemsLiveData)
    }

    override fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<ProfileItem>>>) {
        val params = Pair(-1L, currentProfileFilter)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, getItemsLiveData)
    }


}