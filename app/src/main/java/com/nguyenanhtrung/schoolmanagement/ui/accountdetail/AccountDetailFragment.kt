package com.nguyenanhtrung.schoolmanagement.ui.accountdetail

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.*
import kotlinx.android.synthetic.main.dropdown_menu.*
import kotlinx.android.synthetic.main.fragment_account_detail.*
import javax.inject.Inject

class AccountDetailFragment : BaseFragment() {

    private val args: AccountDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val detailViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AccountDetailViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    private lateinit var detailMenu: Menu
    private lateinit var userTypeAdapter: ArrayAdapter<String>

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = detailViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_account_detail, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailViewModel.accountDetailParams = args.accountDetailParams

        subscribeUserInfo()
        subscribeStateEditAccountInfo()
        subscribeStateSaveModifiedInfo()
    }

    private fun subscribeStateSaveModifiedInfo() {
        detailViewModel.stateSaveModifiedInfo.observe(this, Observer {
            if (it.status == Status.SUCCESS) {
                detailViewModel.onSuccessSaveModifiedAccountInfo()
                val accountDetailParams = detailViewModel.accountDetailParams
                val oldAccInfo = accountDetailParams.user
//                val modifiedAccountInfo = User(
//                    oldAccInfo.id,
//                    oldAccInfo.firebaseUserId,
//                    edit_text_name.getString(),
//                    spinner_account_type.selectedItem as String,
//                    detailViewModel.getUserTypeIdByIndex(spinner_account_type.selectedIndex),
//                    name = oldAccInfo.accountName
//                )
//                mainViewModel.updateAccountInfo.value = Event(modifiedAccountInfo)
            }
        })
    }

    private fun subscribeStateEditAccountInfo() {
        detailViewModel.stateEditAccountInfo.observe(this, Observer {
            handleStateModifyInput(it, edit_text_name, input_layout_name)
            handleStateModifyInput(it, edit_text_password, input_layout_password)
            when (it) {
                ModificationState.Edit -> {
                    filled_exposed_dropdown.isEnabled = true
                    showMenuItemWithEditState()
                }
                ModificationState.Save -> {
                    filled_exposed_dropdown.isEnabled = false
                    showMenuItemWithSaveState()
                }
            }
        })
    }

    private fun showMenuItemWithEditState() {
        if (::detailMenu.isInitialized) {
            val editItem = detailMenu.findItem(R.id.menu_item_edit)
            editItem.isVisible = false
            val saveItem = detailMenu.findItem(R.id.menu_item_save)
            saveItem.isVisible = true
        }
    }

    private fun showMenuItemWithSaveState() {
        if (::detailMenu.isInitialized) {
            val editItem = detailMenu.findItem(R.id.menu_item_edit)
            editItem.isVisible = true
            val saveItem = detailMenu.findItem(R.id.menu_item_save)
            saveItem.isVisible = false
        }
    }

    private fun handleStateModifyInput(
        state: ModificationState,
        editText: TextInputEditText,
        inputLayout: TextInputLayout
    ) {
        when (state) {
            ModificationState.Edit -> editText.enableInput(inputLayout)
            ModificationState.Save -> editText.disableInput(inputLayout)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
        mainViewModel.showToolbar()
        edit_text_password.clearErrorWhenFocus(input_layout_password)
        edit_text_name.clearErrorWhenFocus(input_layout_name)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_account_detail, menu)
        detailMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_edit -> {
                detailViewModel.onClickButtonEdit()
                true
            }
            R.id.menu_item_save -> {
                detailViewModel.onClickButtonSave(
                    edit_text_name.getString(),
                    filled_exposed_dropdown.listSelection,
                    edit_text_password.getString()
                )
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun setupUiEvents() {
        setSupportActionBar()
        setupNavigationEvent()
        subscribeUserTypes()
        subscribeSelectedUserType()
        subscribeErrorPassword()
        subscribeErrorName()
        detailViewModel.loadUserTypes()

    }

    private fun setSupportActionBar() {
        mainViewModel.mutableSupportMainToolbar.value = true
    }

    private fun subscribeErrorPassword() {
        detailViewModel.errorPasswordLiveData.observe(viewLifecycleOwner, Observer {
            input_layout_password.setErrorWithState(it)
        })
    }

    private fun subscribeErrorName() {
        detailViewModel.errorNameLiveData.observe(viewLifecycleOwner, Observer {
            input_layout_name.setErrorWithState(it)
        })
    }


    private fun setupNavigationEvent() {
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.accountManagementDest) {
                mainViewModel.hideToolbar()
            }
        }
    }

    private fun subscribeSelectedUserType() {
        detailViewModel.indexUserTypeSelected.observe(viewLifecycleOwner, Observer {
            filled_exposed_dropdown.listSelection = it
        })
    }

    private fun subscribeUserInfo() {
        detailViewModel.currentUserLiveData.observe(this, Observer {
            it?.let {
                showAccountDetailInfo(it)
            }
        })
    }

    private fun subscribeUserTypes() {
        detailViewModel.userTypesLiveData.observe(viewLifecycleOwner, Observer {
            it.data?.let { userTypes ->
                if (!::userTypeAdapter.isInitialized) {
                    userTypeAdapter = ArrayAdapter(
                        requireActivity(),
                        R.layout.dropdown_menu_popup_item,
                        userTypes.map { userType ->
                            userType.name
                        })
                }
                filled_exposed_dropdown.setAdapter(userTypeAdapter)
                detailViewModel.loadUserInfo()
            }
        })
    }

    private fun showAccountDetailInfo(accountDetailParams: AccountDetailParams) {
        val user = accountDetailParams.user
        text_account_name.text = user.name
        image_account_detail.loadImageIfEmptyPath(user.avatarPath)
        edit_text_name.setText(user.name)
        text_account_detail_id.text = "${getString(R.string.title_account_id)}: ${user.id}"
        detailViewModel.showSelectedUserType(user.type.id)
        edit_text_password.setText(accountDetailParams.password)
    }
}