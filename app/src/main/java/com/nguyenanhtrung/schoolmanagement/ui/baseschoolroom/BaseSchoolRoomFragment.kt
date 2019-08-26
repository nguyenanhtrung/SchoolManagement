package com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom

import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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

    override fun setupUiEvents() {
        inputRoomIdLayout = bindInputRoomIdLayout()
        editTextRoomId = bindEditTextRoomId()
        inputRoomNameLayout = bindInputRoomNameLayout()
        editTextRoomName = bindEditTextRoomName()

        editTextRoomId.clearErrorWhenFocus(input_layout_room_id)
        edit_text_room_name.clearErrorWhenFocus(input_layout_room_name)
    }

    abstract fun bindBaseViewModel(): BaseSchoolRoomViewModel
    abstract fun bindInputRoomIdLayout(): TextInputLayout
    abstract fun bindEditTextRoomId(): TextInputEditText
    abstract fun bindInputRoomNameLayout(): TextInputLayout
    abstract fun bindEditTextRoomName(): TextInputEditText


}