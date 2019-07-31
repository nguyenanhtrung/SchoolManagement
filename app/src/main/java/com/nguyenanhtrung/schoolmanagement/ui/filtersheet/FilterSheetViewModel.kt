package com.nguyenanhtrung.schoolmanagement.ui.filtersheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterData
import com.nguyenanhtrung.schoolmanagement.data.local.model.FilterItem
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class FilterSheetViewModel @Inject constructor() : BaseViewModel() {
    internal lateinit var filterDatas: Array<FilterData>
    private var currentPosFilterItem = -1

    private val _filterItems by lazy {
        MutableLiveData<MutableList<FilterItem>>()
    }
    internal val filterItems: LiveData<MutableList<FilterItem>>
        get() = _filterItems

    private val _stateFilterItemChange by lazy {
        MutableLiveData<Int>()
    }
    internal val stateFilterItemChange: LiveData<Int>
        get() = _stateFilterItemChange

    private val _stateConfirmFilter by lazy {
        MutableLiveData<FilterData>()
    }
    internal val stateConfirmFilter: LiveData<FilterData>
        get() = _stateConfirmFilter

    internal fun loadFilterItems() {
        val filterItems = filterDatas.map {
            FilterItem(it)
        }
        _filterItems.value = filterItems.toMutableList()
    }

    internal fun onClickFilterItem(position: Int) {
        currentPosFilterItem = when (currentPosFilterItem) {
            -1 -> {
                notifyFilterItemChange(position, true)
                position
            }
            position -> return
            else -> {
                notifyFilterItemChange(currentPosFilterItem, false)
                notifyFilterItemChange(position, true)
                position
            }
        }
    }

    internal fun onClickButtonConfirm() {
        val filterItems = _filterItems.value ?: return
        val selectedItem = filterItems[currentPosFilterItem]
        _stateConfirmFilter.value = selectedItem.filterData
    }

    private fun notifyFilterItemChange(position: Int, isSelected: Boolean) {
        val filterItems = _filterItems.value ?: return
        filterItems[position].setItemSelected(isSelected)
        _stateFilterItemChange.value = position
    }
}