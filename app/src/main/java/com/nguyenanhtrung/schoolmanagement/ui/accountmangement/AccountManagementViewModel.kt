package com.nguyenanhtrung.schoolmanagement.ui.accountmangement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.user.GetUsersUseCase
import com.nguyenanhtrung.schoolmanagement.domain.userid.GetMaxUserIdUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import javax.inject.Inject

class AccountManagementViewModel @Inject constructor(
    private val getMaxUserIdUseCase: GetMaxUserIdUseCase,
    private val getUsersUseCase: GetUsersUseCase
) : BaseListItemViewModel() {


    private val _navToCreateAccountFragment by lazy {
        MutableLiveData<Event<Long>>()
    }
    internal val navToCreateAccountFragment: LiveData<Event<Long>>
        get() = _navToCreateAccountFragment

    private val _navToAccountDetail by lazy {
        MutableLiveData<Event<Int>>()
    }
    internal val navToAccountDetail: LiveData<Event<Int>>
        get() = _navToAccountDetail

    private val _maxUserIdLiveData by lazy {
        createApiResultLiveData<Long>()
    }
    internal val maxUserIdLiveData: LiveData<Resource<Long>>
        get() = _maxUserIdLiveData



    override fun loadMoreItems(
        lastItem: Item<ViewHolder>,
        itemsLiveData: MutableLiveData<Resource<MutableList<out com.xwray.groupie.kotlinandroidextensions.Item>>>
    ) {
        val userItem = lastItem as UserItem
        val user = userItem.user
        getUsersUseCase.invoke(viewModelScope, user.id, itemsLiveData)
    }

    override fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out com.xwray.groupie.kotlinandroidextensions.Item>>>) {
        getUsersUseCase.invoke(viewModelScope, -1, getItemsLiveData)
    }

    override fun onCustomClickItem(position: Int) {
        _navToAccountDetail.value = Event(position)
    }

    override fun customCheckItemWithQuery(
        query: String,
        item: com.xwray.groupie.kotlinandroidextensions.Item
    ): Boolean {
        if (item is UserItem) {
            val user = item.user
            val accountName = user.accountName
            return accountName.contains(query)
        }
        return false
    }

    internal fun onSuccessCreateAccount(newUser: User) {
        if (isItemsEmpty()) {
            _emptyUsersLiveData.value = ListEmptyState.CLEAR
        }
        val newUserItem = UserItem(newUser)
        addItems(mutableListOf(newUserItem))
    }



    internal fun onClickButtonCreateAccount() {
        getMaxUserIdUseCase.invoke(viewModelScope, Unit, _maxUserIdLiveData)
    }

    internal fun onSuccessGetMaxUserId(id: Long) {
        _navToCreateAccountFragment.value = Event(id)
    }
}