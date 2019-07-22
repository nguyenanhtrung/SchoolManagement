package com.nguyenanhtrung.schoolmanagement.ui.accountmangement

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_list_account.*
import javax.inject.Inject

class AccountManagementFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val accountViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AccountManagementViewModel::class.java]
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
        return inflater.inflate(R.layout.fragment_list_account, container, false)
    }

    override fun setupUiEvents() {
        subscribeNavigateToCreateAccount()
        float_button_create_account.setOnClickListener {
            accountViewModel.onClickButtonCreateAccount()
        }
    }

    private fun subscribeNavigateToCreateAccount() {
        accountViewModel.navToCreateAccountFragment.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(AccountManagementFragmentDirections.actionAccountManagementDestToCreateAccountDest())
                mainViewModel.showToolbar()
            }
        })
    }
}