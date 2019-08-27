package com.nguyenanhtrung.schoolmanagement.ui.schoolroomdetail

import android.app.Application
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ModificationState
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomFragment
import com.nguyenanhtrung.schoolmanagement.ui.baseschoolroom.BaseSchoolRoomViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.disableInput
import com.nguyenanhtrung.schoolmanagement.util.enableInput
import com.nguyenanhtrung.schoolmanagement.util.getString
import kotlinx.android.synthetic.main.fragment_add_school_room.*
import javax.inject.Inject

class SchoolRoomDetailFragment : BaseSchoolRoomFragment() {

    private val detailArgs: SchoolRoomDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val roomDetailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SchoolRoomDetailViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    private lateinit var roomDetailMenu: Menu

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        subscribeSchoolRoomDetail()
        roomDetailViewModel.showSchoolRoomDetail(detailArgs.schoolRoom)
        subscribeStateOnClickMenuItem()
    }

    private fun subscribeStateOnClickMenuItem() {
        roomDetailViewModel.stateClickMenuItem.observe(this, Observer {
            when (it) {
                ModificationState.Edit -> {
                    val editItem = roomDetailMenu.findItem(R.id.menu_item_edit)
                    editItem.isVisible = false
                    val saveItem = roomDetailMenu.findItem(R.id.menu_item_save)
                    saveItem.isVisible = true
                    enableAllInput()
                }
                ModificationState.Save -> {
                    val editItem = roomDetailMenu.findItem(R.id.menu_item_edit)
                    editItem.isVisible = true
                    val saveItem = roomDetailMenu.findItem(R.id.menu_item_save)
                    saveItem.isVisible = false
                    disableAllInput()
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.showToolbar()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_detail, menu)
        roomDetailMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_item_edit -> {
            roomDetailViewModel.onClickButtonEdit()
            true
        }
        R.id.menu_item_save -> {
            roomDetailViewModel.onClickButtonSave(
                edit_text_room_name.getString(),
                edit_text_room_id.getString(),
                getSelectedRoomType()
            )
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun subscribeSchoolRoomDetail() {
        roomDetailViewModel.schoolRoomLiveData.observe(this, Observer {
            it?.let {
                edit_text_room_id.setText(it.roomNumber)
                edit_text_room_name.setText(it.roomName)
                showRoomType(it.isOfficeRoom)
                disableAllInput()
            }
        })
    }

    override fun setupUiEvents() {
        super.setupUiEvents()
        button_confirm.visibility = View.GONE
    }

    private fun disableAllInput() {
        edit_text_room_id.disableInput(input_layout_room_id)
        edit_text_room_name.disableInput(input_layout_room_name)
        toggle_room_type_group.isEnabled = false
        button_class_room.isClickable = false
        button_office_room.isClickable = false
    }

    private fun enableAllInput() {
        edit_text_room_id.enableInput(input_layout_room_id)
        edit_text_room_name.enableInput(input_layout_room_name)
        toggle_room_type_group.isEnabled = true
        button_class_room.isClickable = true
        button_office_room.isClickable = true
    }

    private fun showRoomType(isOfficeRoom: Boolean) {
        when (isOfficeRoom) {
            true -> toggle_room_type_group.check(R.id.button_office_room)
            else -> toggle_room_type_group.check(R.id.button_class_room)
        }
    }

    override fun bindBaseViewModel(): BaseSchoolRoomViewModel = roomDetailViewModel
    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel


}