package com.nguyenanhtrung.schoolmanagement.ui.faculties

import androidx.lifecycle.MutableLiveData
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class FacultiesViewModel @Inject constructor() : BaseListItemViewModel() {

    override fun customCheckItemWithQuery(query: String, item: GenericItem): Boolean {
        return false
    }

    override fun loadMoreItems(
        lastItem: GenericItem,
        itemsLiveData: MutableLiveData<Resource<MutableList<out GenericItem>>>
    ) {
    }

    override fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out GenericItem>>>) {
    }

    override fun onCustomClickItem(position: Int) {
    }
}