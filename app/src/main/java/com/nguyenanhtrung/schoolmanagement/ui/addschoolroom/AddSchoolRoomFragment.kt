package com.nguyenanhtrung.schoolmanagement.ui.addschoolroom

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomFragment
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.clearText
import com.nguyenanhtrung.schoolmanagement.util.getString
import kotlinx.android.synthetic.main.fragment_add_school_room.*
import javax.inject.Inject

class AddSchoolRoomFragment : BaseSchoolRoomFragment() {

    private val args: AddSchoolRoomFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val addRoomViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AddSchoolRoomViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun bindBaseViewModel(): BaseSchoolRoomViewModel = addRoomViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeStateAddSchoolRoom()
    }

    private fun subscribeStateAddSchoolRoom() {
        addRoomViewModel.stateSchoolRoomAdding.observe(this, Observer {
            it.data?.let { schoolRoom ->
                addRoomViewModel.onSuccessAddSchoolRoom(schoolRoom.roomId)
                mainViewModel.showMessage(R.string.title_success_add_school_room)
                resetAllInput()
                mainViewModel.notifySuccessAddSchoolRoom(schoolRoom)
            }
        })
    }

    private fun resetAllInput() {
        edit_text_room_name.clearText()
        edit_text_room_id.clearText()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addRoomViewModel.lastRoomId = args.lastRoomId
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.showToolbar()
    }

    override fun setupUiEvents() {
        super.setupUiEvents()
        button_confirm.setOnClickListener {
            addRoomViewModel.onClickButtonConfirm(
                edit_text_room_id.getString(),
                edit_text_room_name.getString(),
                getSelectedRoomType()
            )
        }
    }

}