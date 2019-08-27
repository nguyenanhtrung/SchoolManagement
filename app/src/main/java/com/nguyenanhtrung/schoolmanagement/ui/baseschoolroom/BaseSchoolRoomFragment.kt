package com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom

import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomType
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.util.clearErrorWhenFocus
import com.nguyenanhtrung.schoolmanagement.util.setErrorWithState
import kotlinx.android.synthetic.main.fragment_add_school_room.*

abstract class BaseSchoolRoomFragment : BaseFragment() {

    private lateinit var inputRoomIdLayout: TextInputLayout
    private lateinit var editTextRoomId: TextInputEditText
    private lateinit var inputRoomNameLayout: TextInputLayout
    private lateinit var editTextRoomName: TextInputEditText
    private lateinit var toggleRoomTypeGroup: MaterialButtonToggleGroup
    private lateinit var buttonOfficeRoom: MaterialButton
    private lateinit var buttonClassRoom: MaterialButton

    private val schoolRoomViewModel by lazy {
        bindBaseViewModel()
    }

    override fun createFragmentViewModel(): BaseViewModel = schoolRoomViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeErrorStateInputRoomId()
        subscribeErrorStateInputRoomName()
    }

    private fun subscribeErrorStateInputRoomName() {
        schoolRoomViewModel.stateErrorInputRoomName.observe(this, Observer {
            inputRoomNameLayout.setErrorWithState(it)
        })
    }

    private fun subscribeErrorStateInputRoomId() {
        schoolRoomViewModel.stateErrorInputRoomId.observe(this, Observer {
            inputRoomIdLayout.setErrorWithState(it)
        })
    }

    protected fun getSelectedRoomType(): SchoolRoomType =
        when (toggleRoomTypeGroup.checkedButtonId) {
            buttonClassRoom.id -> SchoolRoomType.CLASS_ROOM
            buttonOfficeRoom.id -> SchoolRoomType.OFFICE
            else -> SchoolRoomType.OFFICE
        }


    override fun setupUiEvents() {
        inputRoomIdLayout = bindInputRoomIdLayout()
        editTextRoomId = bindEditTextRoomId()
        inputRoomNameLayout = bindInputRoomNameLayout()
        editTextRoomName = bindEditTextRoomName()
        toggleRoomTypeGroup = bindToggleRoomTypeGroup()
        buttonClassRoom = bindButtonClassRoom()
        buttonOfficeRoom = bindButtonOfficeRoom()

        editTextRoomId.clearErrorWhenFocus(input_layout_room_id)
        edit_text_room_name.clearErrorWhenFocus(input_layout_room_name)
    }

    abstract fun bindBaseViewModel(): BaseSchoolRoomViewModel
    abstract fun bindInputRoomIdLayout(): TextInputLayout
    abstract fun bindEditTextRoomId(): TextInputEditText
    abstract fun bindInputRoomNameLayout(): TextInputLayout
    abstract fun bindEditTextRoomName(): TextInputEditText
    abstract fun bindToggleRoomTypeGroup(): MaterialButtonToggleGroup
    abstract fun bindButtonOfficeRoom(): MaterialButton
    abstract fun bindButtonClassRoom(): MaterialButton

}