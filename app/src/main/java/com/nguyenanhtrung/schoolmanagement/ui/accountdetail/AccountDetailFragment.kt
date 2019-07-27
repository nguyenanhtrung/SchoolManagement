package com.nguyenanhtrung.schoolmanagement.ui.accountdetail

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val currentUser = args.userInfo
        detailViewModel.currentUserInfo = currentUser
    }

    override fun createFragmentViewModel(): BaseViewModel = detailViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_account_detail, container, false)
    }

    override fun setupUiEvents() {
        subscribeUserTypes()
        subscribeUserInfo()
        detailViewModel.loadUserTypes()
        detailViewModel.loadUserInfo()
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

        }
    }
}