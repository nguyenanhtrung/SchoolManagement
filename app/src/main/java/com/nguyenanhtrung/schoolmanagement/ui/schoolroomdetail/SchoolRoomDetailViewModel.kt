package com.nguyenanhtrung.schoolmanagement.ui.schoolroomdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ModificationState
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomType
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomViewModel
import javax.inject.Inject

class SchoolRoomDetailViewModel @Inject constructor() : BaseSchoolRoomViewModel() {

    private val _schoolRoomLiveData by lazy {
        MutableLiveData<SchoolRoom>()
    }
    internal val schoolRoomLiveData: LiveData<SchoolRoom>
        get() = _schoolRoomLiveData

    private val _stateClickMenuItem by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateClickMenuItem: LiveData<ModificationState>
        get() = _stateClickMenuItem

    internal fun showSchoolRoomDetail(schoolRoom: SchoolRoom) {
        _schoolRoomLiveData.value = schoolRoom
    }


    internal fun onClickButtonEdit() {
        _stateClickMenuItem.value = ModificationState.Edit
    }

    internal fun onClickButtonSave(
        newRoomName: String,
        newRoomNumber: String,
        newRoomType: SchoolRoomType
    ) {
        if (!isSchoolRoomInfoChanged(newRoomName, newRoomNumber, newRoomType)) {
            _stateClickMenuItem.value = ModificationState.Save
            return
        }


    }

    private fun isSchoolRoomInfoChanged(
        newRoomName: String,
        newRoomNumber: String,
        newRoomType: SchoolRoomType
    ): Boolean {
        val originSchoolRoom = _schoolRoomLiveData.value ?: return false
        var isOfficeRoom = false
        if (newRoomType == SchoolRoomType.OFFICE) {
            isOfficeRoom = true
        }
        if (newRoomNumber == originSchoolRoom.roomNumber &&
            newRoomName == originSchoolRoom.roomName &&
            isOfficeRoom == originSchoolRoom.isOfficeRoom
        ) {
            return false
        }
        return true
    }
}