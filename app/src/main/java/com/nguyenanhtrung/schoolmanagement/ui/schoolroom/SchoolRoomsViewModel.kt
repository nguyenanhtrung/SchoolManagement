package com.nguyenanhtrung.schoolmanagement.ui.schoolroom

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.domain.schoolrooms.GetSchoolRoomsUseCase
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class SchoolRoomsViewModel @Inject constructor(private val getSchoolRoomsUseCase: GetSchoolRoomsUseCase) :
    BaseListItemViewModel() {

    override fun customCheckItemWithQuery(query: String, item: Item): Boolean {
        val schoolRoomItem = item as SchoolRoomItem
        val schoolRoom = schoolRoomItem.schoolRoom
        return schoolRoom.roomName.contains(query) || schoolRoom.roomNumber.contains(query)
    }

    override fun loadMoreItems(
        lastItem: com.xwray.groupie.Item<ViewHolder>,
        itemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>
    ) {
        val schoolRoomItem = lastItem as SchoolRoomItem
        val lastRoom = schoolRoomItem.schoolRoom
        getSchoolRoomsUseCase.invoke(viewModelScope, lastRoom.roomId, itemsLiveData)
    }

    override fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out Item>>>) {
        getSchoolRoomsUseCase.invoke(viewModelScope, -1, getItemsLiveData)
    }

    override fun onCustomClickItem(position: Int) {
    }
}