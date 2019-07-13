package com.nguyenanhtrung.schoolmanagement.ui.dashboard

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
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

    }
}