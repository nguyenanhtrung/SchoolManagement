package com.nguyenanhtrung.schoolmanagement.ui.accountdetail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ModificationState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.*
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
        val currentUser = args.userInfo
        detailViewModel.currentUserInfo = currentUser
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.showToolbar()
    }

    override fun setupUiEvents() {
        subscribeUserTypes()
        detailViewModel.loadUserTypes()
        subscribeStateModifyInfo()
        subscribeStateUpdateBasicInfo()
        subscribeErrorName()
        subscribeStateChangePassword()
        setupNavigationEvent()
        setupModifyInfoButtonEvent()
        subscribeUserInfo()
        subscribeSelectedUserType()
    }

    private fun subscribeErrorName() {
        detailViewModel.errorNameLiveData.observe(viewLifecycleOwner, Observer {
            input_layout_name.setErrorWithState(it)
        })
    }

    private fun subscribeStateUpdateBasicInfo() {
        detailViewModel.stateUpdateBasicInfo.observe(viewLifecycleOwner, Observer {
            if (it.status == Status.SUCCESS) {
                mainViewModel.showMessage(R.string.success_modify_info)
            }
        })
    }

    private fun subscribeStateChangePassword() {
        detailViewModel.stateModifyPassword.observe(viewLifecycleOwner, Observer {
            when (it) {
                ModificationState.Edit -> {
                    enableEditInput(
                        button_status_change_password,
                        input_layout_password,
                        edit_text_password
                    )
                }
                ModificationState.Save -> {
                    disableEditInput(
                        button_status_change_password,
                        input_layout_password,
                        edit_text_password
                    )
                }
            }
        })
    }

    private fun subscribeStateModifyInfo() {
        detailViewModel.stateModifyInfo.observe(viewLifecycleOwner, Observer {
            when (it) {
                ModificationState.Edit -> {
                    enableEditInput(
                        button_status_mofidy_info,
                        input_layout_name,
                        edit_text_name
                    )
                    spinner_account_type.isEnabled = true
                }
                ModificationState.Save -> {
                    spinner_account_type.isEnabled = false
                    disableEditInput(button_status_mofidy_info, input_layout_name, edit_text_name)
                    detailViewModel.saveBasicInfoModification(
                        edit_text_name.getString(),
                        spinner_account_type.selectedIndex
                    )
                }
            }
        })
    }


    private fun enableEditInput(
        buttonStatus: ImageButton,
        inputLayout: TextInputLayout,
        editText: TextInputEditText
    ) {
        buttonStatus.setImageResource(R.drawable.ic_save)
        editText.enableInput(inputLayout)
    }

    private fun disableEditInput(
        buttonStatus: ImageButton,
        inputLayout: TextInputLayout,
        editText: TextInputEditText
    ) {
        buttonStatus.setImageResource(R.drawable.ic_edit)
        editText.disableInput(inputLayout)
    }


    private fun setupModifyInfoButtonEvent() {
        button_status_mofidy_info.setOnClickListener {
            detailViewModel.onClickModifyInfoButton()
        }

        button_status_change_password.setOnClickListener {
            detailViewModel.onClickModifyPasswordButton()
        }
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
            spinner_account_type.selectedIndex = it
        })
    }

    private fun subscribeUserInfo() {
        detailViewModel.currentUserLiveData.observe(viewLifecycleOwner, Observer {
            it?.let {
                showUserInfo(it)
            }
        })
    }

    private fun subscribeUserTypes() {
        detailViewModel.userTypesLiveData.observe(viewLifecycleOwner, Observer {
            it.data?.let { userTypes ->
                spinner_account_type.attachDataSource(userTypes.map { userType ->
                    userType.name
                })
                detailViewModel.loadUserInfo()
            }
        })
    }

    private fun showUserInfo(user: User) {
        with(user) {
            text_account_name.text = accountName
            text_account_id.text =
                String.format("%s: %d", getString(R.string.title_account_id), user.id)
            image_account_detail.loadImageIfEmptyPath(user.avatarPath)
            edit_text_name.setText(user.name)
            detailViewModel.showSelectedUserType(user.typeId)
        }
    }
}