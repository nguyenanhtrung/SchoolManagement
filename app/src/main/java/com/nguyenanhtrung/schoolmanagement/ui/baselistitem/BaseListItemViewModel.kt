package com.nguyenanhtrung.schoolmanagement.ui.baselistitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.items.AbstractItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item

abstract class BaseListItemViewModel<T> : BaseViewModel() where T : GenericItem {

    internal var posItemSelected = -1

    private var lastQueryItem: String? = null

    private var shouldLoadMoreItem = true

    protected val _getItemsLiveData: MutableLiveData<Resource<MutableList<T>>> by lazy {
        createApiResultLiveData<MutableList<T>>()
    }
    internal val getItemsLiveData: LiveData<Resource<MutableList<T>>>
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
        MutableLiveData<MutableList<T>>()
    }
    internal val itemsLiveData: LiveData<MutableList<T>>
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



    internal fun onLoadMoreItem(lastItemPosition: Int) {
        if (!shouldLoadMoreItem) {
            return
        }
        if (_stateLoadMoreItemLiveData.value == Status.LOADING) {
            return
        }
        _stateLoadMoreItemLiveData.value = Status.LOADING
        val lastItem = getItem(lastItemPosition)
        loadMoreItems(lastItem, _getItemsLiveData)
    }

    internal fun handleStatusGetItems(
        getItemsResult:
        Resource<MutableList<T>>
    ) {


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
        loadItemsFromServer(_getItemsLiveData)
    }

    internal fun onClickItem(position: Int) {
        posItemSelected = position
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

    protected fun getItem(position: Int): T {
        val items = _itemsLiveData.value ?: throw IllegalStateException()
        return items[position]
    }

    internal fun onSearchItemQueryChange(query: String, item: GenericItem): Boolean {
        if (query.isEmpty()) {
            enableLoadMore()
        } else {
            disableLoadMore()
        }
        return customCheckItemWithQuery(query, item)
    }


    protected abstract fun customCheckItemWithQuery(query: String, item: GenericItem): Boolean

    protected abstract fun loadMoreItems(
        lastItem: T,
        itemsLiveData: MutableLiveData<Resource<MutableList<T>>>
    )

    protected abstract fun loadItemsFromServer(
        getItemsLiveData:
        MutableLiveData<Resource
        <MutableList<T>>>
    )

    protected abstract fun onCustomClickItem(position: Int)


}