package com.nguyenanhtrung.schoolmanagement.ui.createaccount

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.clearErrorWhenFocus
import com.nguyenanhtrung.schoolmanagement.util.clearText
import com.nguyenanhtrung.schoolmanagement.util.getString
import com.nguyenanhtrung.schoolmanagement.util.setErrorWithState
import kotlinx.android.synthetic.main.fragment_create_new_account.*
import javax.inject.Inject

class CreateAccountFragment : BaseFragment() {

    private val args: CreateAccountFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val accountViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CreateAccountViewModel::class.java]
    }
    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = accountViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_create_new_account, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val maxUserId = args.maxUserId
        accountViewModel.maxId = maxUserId
        subscribeCreateUser()
    }

    private fun subscribeCreateUser() {
        accountViewModel.createUserLiveData.observe(this, Observer {
            it.data?.let { userFireBaseId ->
                accountViewModel.newFireBaseId = userFireBaseId
                val flowStatusInfo = FlowStatusInfo(
                    Status.SUCCESS,
                    R.string.title_success_create_account,
                    R.string.title_update,
                    R.id.dialogFlowStatusDest
                )
                findNavController().navigate(
                    CreateAccountFragmentDirections.actionCreateAccountDestToDialogFlowStatusFragment(
                        flowStatusInfo
                    )
                )
                clearAllInput()
                edit_text_name.requestFocus()
            }
        })
    }


    private fun clearAllInput() {
        edit_text_email.clearText()
        edit_text_name.clearText()
        edit_text_password.clearText()
    }


    override fun setupUiEvents() {
        setupNavigationEvent()
        setupButtonConfirmEvent()
        setupEditTextEvent()
        subscribeErrorInputName()
        subscribeErrorInputEmail()
        subscribeErrorInputPassword()
        subscribeUserTypes()
        accountViewModel.loadUserTypes()
    }

    private fun setupEditTextEvent() {
        edit_text_name.clearErrorWhenFocus(input_layout_name)
        edit_text_email.clearErrorWhenFocus(input_layout_email)
        edit_text_password.clearErrorWhenFocus(input_layout_password)
    }

    private fun subscribeErrorInputPassword() {
        accountViewModel.errorPasswordLiveData.observe(viewLifecycleOwner, Observer {
            input_layout_password.setErrorWithState(it)
        })
    }

    private fun subscribeErrorInputEmail() {
        accountViewModel.errorEmailLiveData.observe(viewLifecycleOwner, Observer {
            input_layout_email.setErrorWithState(it)
        })
    }

    private fun subscribeErrorInputName() {
        accountViewModel.errorNameLiveData.observe(viewLifecycleOwner, Observer {
            input_layout_name.setErrorWithState(it)
        })
    }

    private fun setupButtonConfirmEvent() {
        button_confirm.setOnClickListener {
            accountViewModel.onClickButtonConfirm(
                CreateAccountInput(
                    edit_text_name.getString(),
                    edit_text_email.getString(),
                    edit_text_password.getString(),
                    spinner_user_type.selectedIndex
                )
            )
        }
    }

    private fun setupNavigationEvent() {
        findNavController().addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.accountManagementDest) {
                mainViewModel.hideToolbar()
            }
        }
    }

    private fun subscribeUserTypes() {
        accountViewModel.userTypesLiveData.observe(viewLifecycleOwner, Observer {
            it.data?.let { userTypes ->
                showUserTypes(userTypes)
            }
        })
    }

    private fun showUserTypes(userTypes: List<UserType>) {
        spinner_user_type.attachDataSource(userTypes.map {
            it.name
        })
    }
}