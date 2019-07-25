package com.nguyenanhtrung.schoolmanagement.ui.accountmangement

import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserItem
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
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

    private val usersAdapter by lazy {
        GroupAdapter<ViewHolder>()
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeMaxUserId()
    }

    override fun setupUiEvents() {
        subscribeNavigateToCreateAccount()
        float_button_create_account.setOnClickListener {
            accountViewModel.onClickButtonCreateAccount()
        }
        setupRecyclerViewUsers()
        subscribeUsers()
        subscribeStateUsers()
        subscribeErrorUsers()
        accountViewModel.loadUsers()
    }

    private fun subscribeErrorUsers() {
        accountViewModel.errorUsersLiveData.observe(viewLifecycleOwner, Observer {

        })
    }

    private fun subscribeStateUsers() {
        accountViewModel.stateUsersLiveData.observe(viewLifecycleOwner, Observer {
            addUserItems(it)
        })
    }

    private fun addUserItems(users: MutableList<Item>) {
        if (usersAdapter.itemCount > 0) {
            usersAdapter.removeGroup(0)
        }
        usersAdapter.addAll(users)
    }

    private fun subscribeUsers() {
        accountViewModel.getUsersLiveData.observe(viewLifecycleOwner, Observer {
            accountViewModel.handleStatusGetUsers(it)
        })
    }

    private fun setupRecyclerViewUsers() {
        recycler_view_accounts.layoutManager = LinearLayoutManager(requireActivity())
        recycler_view_accounts.adapter = usersAdapter
    }

    private fun subscribeMaxUserId() {
        accountViewModel.maxUserIdLiveData.observe(this, Observer {
            it.data?.let { id ->
                accountViewModel.onSuccessGetMaxUserId(id)
            }
        })
    }

    private fun subscribeNavigateToCreateAccount() {
        accountViewModel.navToCreateAccountFragment.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { id ->
                findNavController().navigate(
                    AccountManagementFragmentDirections.actionAccountManagementDestToCreateAccountDest(
                        id
                    )
                )
                mainViewModel.showToolbar()
            }
        })
    }
}