package com.nguyenanhtrung.schoolmanagement.ui.schoolroomdetail

import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UpdateSchoolRoomParams
import com.nguyenanhtrung.schoolmanagement.domain.schoolroomdetail.UpdateSchoolRoomUseCase
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomViewModel
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import javax.inject.Inject

class SchoolRoomDetailViewModel @Inject constructor(private val updateSchoolRoomUseCase: UpdateSchoolRoomUseCase) :
    BaseSchoolRoomViewModel() {

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

    private val _stateUpdateSchoolRoom by lazy {
        createApiResultLiveData<Unit>()
    }
    internal val stateUpdateSchoolRoom: LiveData<Resource<Unit>>
        get() = _stateUpdateSchoolRoom

    private val _notifyUpdateSchoolRooms by lazy {
        MutableLiveData<SchoolRoom>()
    }
    internal val notifyUpdateSchoolRooms: LiveData<SchoolRoom>
        get() = _notifyUpdateSchoolRooms

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
        val originSchoolRoom = _schoolRoomLiveData.value ?: return
        val updateFields = getUpdateSchoolRoomFields(newRoomName, newRoomNumber, newRoomType)
        val updateSchoolRoomParams =
            UpdateSchoolRoomParams(originSchoolRoom.fireBaseId, updateFields)
        updateSchoolRoomUseCase.invoke(
            viewModelScope,
            updateSchoolRoomParams,
            _stateUpdateSchoolRoom
        )
    }

    internal fun onSuccessUpdateSchoolRoom(
        newRoomName: String,
        newRoomNumber: String,
        newRoomType: SchoolRoomType
    ) {
        _messageLiveData.value = R.string.title_success_update_room_info
        val oldRoomInfo = _schoolRoomLiveData.value ?: return
        val isOfficeRoom = isOfficeRoomTypeSelected(newRoomType)
        val newRoomInfo = oldRoomInfo.copy(
            roomName = newRoomName,
            roomNumber = newRoomNumber,
            isOfficeRoom = isOfficeRoom
        )
        _stateClickMenuItem.value = ModificationState.Save
        _notifyUpdateSchoolRooms.value = newRoomInfo
    }

    private fun getUpdateSchoolRoomFields(
        newRoomName: String,
        newRoomNumber: String,
        newRoomType: SchoolRoomType
    ): ArrayMap<String, Any> {
        val updateFields = arrayMapOf<String, Any>()
        val originSchoolRoom = _schoolRoomLiveData.value ?: return arrayMapOf()

        if (newRoomName != originSchoolRoom.roomName) {
            updateFields[AppKey.NAME_FIELD_SCHOOL_ROOMS] = newRoomName
        }

        if (newRoomNumber != originSchoolRoom.roomNumber) {
            updateFields[AppKey.ROOM_NUMBER_FIELD_SCHOOL_ROOMS] = newRoomNumber
        }

        val isOfficeRoom = isOfficeRoomTypeSelected(newRoomType)
        if (isOfficeRoom != originSchoolRoom.isOfficeRoom) {
            updateFields[AppKey.IS_OFFICE_ROOM_FIELD_SCHOOL_ROOMS] = isOfficeRoom
        }
        return updateFields
    }

    private fun isOfficeRoomTypeSelected(schoolRoomType: SchoolRoomType): Boolean {
        var isOfficeRoom = false
        if (schoolRoomType == SchoolRoomType.OFFICE) {
            isOfficeRoom = true
        }
        return isOfficeRoom
    }

    private fun isSchoolRoomInfoChanged(
        newRoomName: String,
        newRoomNumber: String,
        newRoomType: SchoolRoomType
    ): Boolean {
        val originSchoolRoom = _schoolRoomLiveData.value ?: return false
        val isOfficeRoom = isOfficeRoomTypeSelected(newRoomType)
        if (newRoomNumber == originSchoolRoom.roomNumber &&
            newRoomName == originSchoolRoom.roomName &&
            isOfficeRoom == originSchoolRoom.isOfficeRoom
        ) {
            return false
        }
        return true
    }
}