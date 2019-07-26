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
import androidx.recyclerview.widget.RecyclerView
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.EndlessScrollListener
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
        subscribeUsers()
        subscribeGetUsers()
        subscribeStateLoadingMoreUsers()
        subscribeEmptyStateGetUsers()
        subscribeErrorUsers()
    }

    override fun setupUiEvents() {
        subscribeNavigateToCreateAccount()
        float_button_create_account.setOnClickListener {
            accountViewModel.onClickButtonCreateAccount()
        }
        setupRecyclerViewUsers()
        setupLoadMoreUsersEvent()

        accountViewModel.loadUsers()
    }

    private fun subscribeStateLoadingMoreUsers() {
        accountViewModel.stateLoadMoreUsersLiveData.observe(this, Observer {
            when (it) {
                Status.LOADING -> usersAdapter.add(LoadMoreItem())
                else -> usersAdapter.removeGroup(
                    usersAdapter.itemCount - 1
                )
            }
        })
    }

    private fun setupLoadMoreUsersEvent() {
        recycler_view_accounts.addOnScrollListener(object :
            EndlessScrollListener(recycler_view_accounts.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                val lastUserItem = usersAdapter.getItem(usersAdapter.itemCount - 1)
                if (lastUserItem is UserItem) {
                    val lastUser = lastUserItem.user
                    accountViewModel.onLoadMoreUsers(lastUser.id)
                }
            }
        })
    }

    private fun subscribeUsers() {
        accountViewModel.usersLiveData.observe(this, Observer {
            addUserItems(it)
        })
    }

    private fun subscribeErrorUsers() {
        accountViewModel.errorUsersLiveData.observe(this, Observer {
            usersAdapter.add(ErrorItem(it, object : ErrorItem.OnClickButtonRetryListener {
                override fun onClickButtonRetry(view: View) {
                    accountViewModel.onClickButtonRetry()
                }

            }))
        })
    }

    private fun subscribeEmptyStateGetUsers() {
        accountViewModel.emptyUsersLiveData.observe(this, Observer {
            usersAdapter.add(it)
        })
    }


    private fun addUserItems(users: MutableList<UserItem>) {
        usersAdapter.addAll(users)
    }

    private fun subscribeGetUsers() {
        accountViewModel.getUsersLiveData.observe(this, Observer {
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