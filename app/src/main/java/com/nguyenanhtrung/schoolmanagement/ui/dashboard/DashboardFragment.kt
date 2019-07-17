package com.nguyenanhtrung.schoolmanagement.ui.dashboard

import android.app.Application
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.include_search_view.*
import javax.inject.Inject

class DashboardFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val dashboardViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[DashboardViewModel::class.java]
    }
    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp  = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = dashboardViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.hideToolbar()
    }

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun setupUiEvents() {
        edit_text_search.inputType = InputType.TYPE_NULL
        subscribeUserInfo()
        dashboardViewModel.loadUserInfo()
    }

    private fun subscribeUserInfo() {
        dashboardViewModel.userInfoLiveData.observe(viewLifecycleOwner, Observer {
            showUserInfo(it.data)
        })
    }

    private fun showUserInfo(user: User?) {
        user?.let {
            text_account_name.text = it.name
            text_account_type.text = it.type
            circle_image_user.loadImageIfEmptyPath(it.avatarPath)
        }
    }
}