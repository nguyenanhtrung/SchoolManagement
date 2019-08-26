package com.nguyenanhtrung.schoolmanagement.ui.addschoolroom

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomFragment
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_add_school_room.*
import javax.inject.Inject

class AddSchoolRoomFragment : BaseSchoolRoomFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val addRoomViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AddSchoolRoomViewModel::class.java]
    }

    override fun bindBaseViewModel(): BaseSchoolRoomViewModel = addRoomViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel {
        return ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun bindInputRoomIdLayout(): TextInputLayout = input_layout_room_id

    override fun bindEditTextRoomId(): TextInputEditText = edit_text_room_id

    override fun bindInputRoomNameLayout(): TextInputLayout = input_layout_room_name

    override fun bindEditTextRoomName(): TextInputEditText = edit_text_room_name

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_add_school_room, container, false)
    }
}