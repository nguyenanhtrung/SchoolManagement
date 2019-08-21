package com.nguyenanhtrung.schoolmanagement.ui.baselistitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

abstract class BaseListItemViewModel : BaseViewModel() {

    internal val itemCopys by lazy {
        mutableListOf<Item>()
    }

    private var lastQueryItem: String? = null

    private var shouldLoadMoreItem = true

    protected val _getItemsLiveData by lazy {
        createApiResultLiveData<MutableList<out Item>>()
    }
    internal val getItemsLiveData: LiveData<Resource<MutableList<out Item>>>
        get() = _getItemsLiveData

    protected val _emptyUsersLiveData by lazy {
        MutableLiveData<ListEmptyState>()
    }
    internal val emptyUsersLiveData: LiveData<ListEmptyState>
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

    protected val _clearItemsLiveData by lazy {
        MutableLiveData<Boolean>()
    }
    internal val clearItemsLiveData: LiveData<Boolean>
        get() = _clearItemsLiveData

    private val _stateLoadMoreItemLiveData by lazy {
        MutableLiveData<Status>()
    }
    internal val stateLoadMoreItemLiveData: LiveData<Status>
        get() = _stateLoadMoreItemLiveData


    internal fun onLoadMoreItem(lastItem: com.xwray.groupie.Item<ViewHolder>) {
        if (!shouldLoadMoreItem) {
            return
        }
        if (_stateLoadMoreItemLiveData.value == Status.LOADING) {
            return
        }
        _stateLoadMoreItemLiveData.value = Status.LOADING
        loadMoreItems(lastItem, _getItemsLiveData)
    }

    internal fun handleStatusGetItems(getItemsResult: Resource<MutableList<out Item>>) {
        when (getItemsResult.status) {
            Status.EMPTY -> {
                val emptyItem = EmptyItem(getItemsResult.error)
                _emptyUsersLiveData.value = ListEmptyState.EMPTY(emptyItem)
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
                    itemCopys.addAll(it)
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
                    getItemsResult.status == Status.COMPLETE || getItemsResult.status == Status.EMPTY)
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

    private fun enableLoadMore() {
        if (!shouldLoadMoreItem) {
            shouldLoadMoreItem = true
        }
    }

    private fun disableLoadMore() {
        if (shouldLoadMoreItem) {
            shouldLoadMoreItem = false
        }
    }

    protected fun isItemsEmpty(): Boolean {
        val emptyState = _emptyUsersLiveData.value ?: true
        return emptyState is ListEmptyState.EMPTY
    }

    internal fun onSearchItemQueryChange(query: String) {
        if ((lastQueryItem == null && query.isEmpty()) || (lastQueryItem == query) || isItemsEmpty()) {
            return
        }
        lastQueryItem = query

        //clear current list item in recycler view
        _clearItemsLiveData.value = true
        if (query.isEmpty()) {
            enableLoadMore()
            //copy list from item copys to current list in recycler view
            _itemsLiveData.value = itemCopys
        } else {
            disableLoadMore()
            val newItems = mutableListOf<Item>()
            val items = _itemsLiveData.value ?: return
            items.forEach {
                if (customCheckItemWithQuery(query, it)) {
                    newItems += it
                }
            }
            _itemsLiveData.value = newItems
        }
    }

    protected fun addItems(items: MutableList<Item>) {
        _itemsLiveData.value = items
        itemCopys.addAll(items)
    }


    protected abstract fun customCheckItemWithQuery(query: String, item: Item): Boolean

    protected abstract fun loadMoreItems(
        lastItem: com.xwray.groupie.Item<ViewHolder>,
        itemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>
    )

    protected abstract fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>)

    protected abstract fun onCustomClickItem(position: Int)
}