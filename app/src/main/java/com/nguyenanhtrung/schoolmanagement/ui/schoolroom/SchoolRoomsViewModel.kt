package com.nguyenanhtrung.schoolmanagement.ui.schoolroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mikepenz.fastadapter.GenericItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.Event
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem
import com.nguyenanhtrung.schoolmanagement.domain.schoolrooms.GetSchoolRoomsUseCase
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import javax.inject.Inject

class SchoolRoomsViewModel<SchoolRoomItem> @Inject constructor(private val getSchoolRoomsUseCase: GetSchoolRoomsUseCase) :
    BaseListItemViewModel<com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomItem>() {

    private val _roomDetailNavigation by lazy {
        MutableLiveData<Event<SchoolRoom>>()
    }
    internal val roomDetailNavigation: LiveData<Event<SchoolRoom>>
        get() = _roomDetailNavigation

    override fun customCheckItemWithQuery(query: String, item: GenericItem): Boolean {
        val schoolRoomItem = item as SchoolRoomItem
        val schoolRoom = schoolRoomItem.schoolRoom
        return schoolRoom.roomName.contains(query, true) || schoolRoom.roomNumber.contains(
            query,
            true
        )
    }

    override fun loadMoreItems(
        lastItem: GenericItem,
        itemsLiveData: MutableLiveData<Resource<MutableList<out GenericItem>>>
    ) {
        val schoolRoomItem = lastItem as SchoolRoomItem
        val lastRoom = schoolRoomItem.schoolRoom
        getSchoolRoomsUseCase.invoke(viewModelScope, lastRoom.roomId, itemsLiveData)
    }

    override fun loadItemsFromServer(getItemsLiveData: MutableLiveData<Resource<MutableList<out GenericItem>>>) {
        getSchoolRoomsUseCase.invoke(viewModelScope, -1, getItemsLiveData)
    }

    override fun onCustomClickItem(position: Int) {
//        val schoolRoomSelected = itemCopys[position]
//        if (schoolRoomSelected is SchoolRoomItem) {
//            val schoolRoom = schoolRoomSelected.schoolRoom
//            _roomDetailNavigation.value = Event(schoolRoom)
//        }
    }

}