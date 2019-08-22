package com.nguyenanhtrung.schoolmanagement.ui.profiles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.profile.GetFilterProfileDatasUseCase
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUsersByProfileStatusUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import java.util.logging.Filter
import javax.inject.Inject

class ProfilesViewModel @Inject constructor(
    private val getUsersByProfileStatusUseCase: GetUsersByProfileStatusUseCase,
    private val getFilterProfileDatasUseCase: GetFilterProfileDatasUseCase
) :
    BaseListItemViewModel() {


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
            getUsersByProfileStatusUseCase.invoke(viewModelScope, params,  _getItemsLiveData)
        }
    }


    override fun onCustomClickItem(position: Int) {
        val profileItem: ProfileItem = itemCopys[position] as ProfileItem
        val profile = profileItem.profile
        val isProfileUpdated = profile.isProfileUpdated
        if (isProfileUpdated) {
            _profileDetailScreen.value = Event(profile)
        } else {
            _profileUpdateScreen.value = Event(Pair(position, profile))
        }
    }

    override fun customCheckItemWithQuery(query: String, item: Item): Boolean {
        val profileItem = item as ProfileItem
        val profile = profileItem.profile
        return profile.name.contains(query) || profile.userId.toString().contains(query)
    }

    override fun loadMoreItems(
        lastItem: com.xwray.groupie.Item<ViewHolder>,
        itemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>
    ) {
        val lastProfileItem = lastItem as ProfileItem
        val lastProfile = lastProfileItem.profile
        val profileId = lastProfile.userId
        val params = Pair(profileId, currentProfileFilter)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, itemsLiveData)
    }

    override fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>) {
        val params = Pair(-1L, currentProfileFilter)
        getUsersByProfileStatusUseCase.invoke(viewModelScope, params, getItemsLiveData)
    }


}