package com.nguyenanhtrung.schoolmanagement.ui.faculties

import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class FacultiesViewModel @Inject constructor() : BaseListItemViewModel() {

    override fun customCheckItemWithQuery(query: String, item: Item): Boolean {
    }

    override fun loadMoreItems(
        lastItem: com.xwray.groupie.Item<ViewHolder>,
        itemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>
    ) {
    }

    override fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>) {
    }

    override fun onCustomClickItem(position: Int) {
    }
}