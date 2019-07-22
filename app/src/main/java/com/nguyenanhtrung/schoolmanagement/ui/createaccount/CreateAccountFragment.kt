package com.nguyenanhtrung.schoolmanagement.ui.createaccount

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_create_new_account.*
import javax.inject.Inject

class CreateAccountFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val accountViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[CreateAccountViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = accountViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel =
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_create_new_account, container, false)
    }

    override fun setupUiEvents() {
        subscribeUserTypes()
        accountViewModel.loadUserTypes()
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