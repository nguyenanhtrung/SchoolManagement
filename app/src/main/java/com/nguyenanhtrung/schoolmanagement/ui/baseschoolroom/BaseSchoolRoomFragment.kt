package com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.SchoolRoomType
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.util.clearErrorWhenFocus
import com.nguyenanhtrung.schoolmanagement.util.setErrorWithState
import kotlinx.android.synthetic.main.fragment_add_school_room.*

abstract class BaseSchoolRoomFragment : BaseFragment() {


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
            input_layout_room_name.setErrorWithState(it)
        })
    }

    private fun subscribeErrorStateInputRoomId() {
        schoolRoomViewModel.stateErrorInputRoomId.observe(this, Observer {
            input_layout_room_id.setErrorWithState(it)
        })
    }

    protected fun getSelectedRoomType(): SchoolRoomType =
        when (toggle_room_type_group.checkedButtonId) {
            button_class_room.id -> SchoolRoomType.CLASS_ROOM
            button_office_room.id -> SchoolRoomType.OFFICE
            else -> SchoolRoomType.OFFICE
        }


    override fun setupUiEvents() {
        edit_text_room_id.clearErrorWhenFocus(input_layout_room_id)
        edit_text_room_name.clearErrorWhenFocus(input_layout_room_name)
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_add_school_room, container, false)
    }

    abstract fun bindBaseViewModel(): BaseSchoolRoomViewModel
}