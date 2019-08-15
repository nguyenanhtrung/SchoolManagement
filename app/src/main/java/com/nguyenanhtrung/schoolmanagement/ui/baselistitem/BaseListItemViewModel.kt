package com.nguyenanhtrung.schoolmanagement.ui.baselistitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.EmptyItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

abstract class BaseListItemViewModel : BaseViewModel() {

    private val _getItemsLiveData by lazy {
        createApiResultLiveData<MutableList<out Item>>()
    }
    internal val getItemsLiveData: LiveData<Resource<MutableList<out Item>>>
        get() = _getItemsLiveData

    private val _emptyUsersLiveData by lazy {
        MutableLiveData<EmptyItem>()
    }
    internal val emptyUsersLiveData: LiveData<EmptyItem>
        get() = _emptyUsersLiveData

    private val _errorItemsLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val errorItemsLiveData: LiveData<ErrorState>
        get() = _errorItemsLiveData

    private val _itemsLiveData by lazy {
        MutableLiveData<MutableList<out Item>>()
    }
    internal val itemsLiveData: LiveData<MutableList<out Item>>
        get() = _itemsLiveData

    private val _stateLoadMoreItemLiveData by lazy {
        MutableLiveData<Status>()
    }
    internal val stateLoadMoreItemLiveData: LiveData<Status>
        get() = _stateLoadMoreItemLiveData


    internal fun onLoadMoreItem(lastItem: com.xwray.groupie.Item<ViewHolder>) {
        if (_stateLoadMoreItemLiveData.value == Status.LOADING) {
            return
        }
        _stateLoadMoreItemLiveData.value = Status.LOADING
        loadMoreItems(lastItem, _getItemsLiveData)
    }


    internal fun handleStatusGetItems(getItemsResult: Resource<MutableList<out Item>>) {
        when (getItemsResult.status) {
            Status.EMPTY -> {
                _emptyUsersLiveData.value = EmptyItem(getItemsResult.error)
            }
            Status.FAILURE, Status.EXCEPTION -> {
                if (_errorItemsLiveData.value == null) {
                    _errorItemsLiveData.value = ErrorState.NoAction(getItemsResult.error)
                }
            }
            Status.SUCCESS -> {
                if (_stateLoadMoreItemLiveData.value == Status.LOADING) {
                    _stateLoadMoreItemLiveData.value = Status.COMPLETE
                }

                if (_errorItemsLiveData.value is ErrorState.NoAction) {
                    _errorItemsLiveData.value = ErrorState.Empty
                }

                val items = getItemsResult.data
                items?.let {
                    _itemsLiveData.value = it
                }
            }
            Status.COMPLETE -> {
                if (_stateLoadMoreItemLiveData.value == Status.LOADING) {
                    _stateLoadMoreItemLiveData.value = Status.COMPLETE
                }
            }
            else -> Unit
        }
    }

    internal fun loadItems() {
        val getItemsResult = _getItemsLiveData.value
        if (getItemsResult != null && (getItemsResult.status == Status.SUCCESS ||
                    getItemsResult.status == Status.COMPLETE)
        ) {
            return
        }
        loadItemsFromServer(_getItemsLiveData)
    }

    internal fun onClickItem(position: Int) {
        onCustomClickItem(position)
    }

    internal fun onClickButtonRetry() {
        loadItemsFromServer(_getItemsLiveData)
    }

    protected abstract fun loadMoreItems(
        lastItem: com.xwray.groupie.Item<ViewHolder>,
        itemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>
    )

    protected abstract fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>)

    protected abstract fun onCustomClickItem(position: Int)
}