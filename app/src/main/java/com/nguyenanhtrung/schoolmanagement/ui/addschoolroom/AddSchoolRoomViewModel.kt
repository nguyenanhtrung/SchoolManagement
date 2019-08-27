package com.nguyenanhtrung.schoolmanagement.ui.addschoolroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoom
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomType
import com.nguyenanhtrung.schoolmanagement.data.remote.model.CreateSchoolRoomParams
import com.nguyenanhtrung.schoolmanagement.domain.schoolrooms.AddSchoolRoomUseCase
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject

class AddSchoolRoomViewModel @Inject constructor(private val addSchoolRoomUseCase: AddSchoolRoomUseCase) :
    BaseSchoolRoomViewModel() {

    private val _stateSchoolRoomAdding by lazy {
        createApiResultLiveData<SchoolRoom>()
    }
    internal val stateSchoolRoomAdding: LiveData<Resource<SchoolRoom>>
        get() = _stateSchoolRoomAdding

    var lastRoomId: Long = 0L

    internal fun onClickButtonConfirm(
        roomNumber: String,
        roomName: String,
        roomType: SchoolRoomType
    ) {
        if (!Validator.isRoomNumberValid(roomNumber, _stateErrorInputRoomId)) {
            return
        }

        if (!Validator.isRoomNameValid(roomName, _stateErrorInputRoomName)) {
            return
        }

        var isOfficeRoom = false
        if (roomType == SchoolRoomType.OFFICE) {
            isOfficeRoom = true
        }

        val createRoomParams = CreateSchoolRoomParams(
            lastRoomId,
            roomNumber,
            roomName,
            isOfficeRoom
        )
        addSchoolRoomUseCase.invoke(viewModelScope, createRoomParams, _stateSchoolRoomAdding)
    }
}