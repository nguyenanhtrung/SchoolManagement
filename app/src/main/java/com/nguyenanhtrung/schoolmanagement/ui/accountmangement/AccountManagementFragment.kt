package com.nguyenanhtrung.schoolmanagement.ui.accountmangement

import android.app.Application
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.AccountDetailParams
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserItem
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemFragment
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.hideKeyboard
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_list_account.*
import kotlinx.android.synthetic.main.include_search_view.*
import javax.inject.Inject

class AccountManagementFragment : BaseListItemFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val accountViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AccountManagementViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun bindRecyclerView(): RecyclerView = recycler_view_accounts

    override fun bindItemsViewModel(): BaseListItemViewModel = accountViewModel

    override fun bindSearchView(): SearchView = edit_text_search

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
        setHasOptionsMenu(true)
        subscribeMaxUserId()
        subscribeCreateAccountEvent()
        subscribeGetSelectedUserPassword()
        subscribeModifyAccountInfo()
    }

    private fun subscribeModifyAccountInfo() {
        mainViewModel.stateModifyAccInfo.observe(this, Observer {
            it.getContentIfNotHandled()?.let {modifiedUser ->
                val posAccountSelected = accountViewModel.posAccountSelected
                val selectedItem = itemAdapter.getItem(posAccountSelected) as UserItem
                selectedItem.user = modifiedUser
                itemAdapter.notifyItemChanged(posAccountSelected)
            }
        })
    }

    private fun subscribeGetSelectedUserPassword() {
        accountViewModel.userPasswordLiveData.observe(this, Observer {
            it.data?.let { password ->
                accountViewModel.onSuccessGetSelectedUserPassword(password)
            }
        })
    }

    private fun subscribeCreateAccountEvent() {
        mainViewModel.accountEvent.observe(this, Observer {
            it.getContentIfNotHandled()?.let { user ->
                accountViewModel.onSuccessCreateAccount(user)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ViewCompat.setTranslationZ(view, 1F)
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity() as AppCompatActivity
        activity.setSupportActionBar(bottom_app_bar_accounts)
    }

    override fun setupUiEvents() {
        super.setupUiEvents()
        subscribeNavigateToCreateAccount()
        float_button_create_account.setOnClickListener {
            accountViewModel.onClickButtonCreateAccount()
        }
        subscribeNavigateToAccountDetail()
    }

    private fun subscribeNavigateToAccountDetail() {
        accountViewModel.navToAccountDetail.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { accountDetailParams ->
                hideKeyboard(edit_text_search)
                openAccountDetailFragment(accountDetailParams)
            }
        })
    }

    private fun openAccountDetailFragment(accountDetailParams: AccountDetailParams) {
        val layoutManager = recycler_view_accounts.layoutManager
        val position = accountViewModel.posAccountSelected
        val itemView = layoutManager?.findViewByPosition(position) ?: return
        val imageViewAcc = itemView.findViewById<CircleImageView>(R.id.image_account)
        val textNameAcc = itemView.findViewById<TextView>(R.id.text_account_name)

        val shareViews = FragmentNavigatorExtras(
            imageViewAcc to getString(R.string.transition_name_image_account),
            textNameAcc to getString(R.string.transition_name_account_email)
        )

        findNavController().navigate(
            AccountManagementFragmentDirections.actionAccountManagementDestToAccountDetailFragment(
                accountDetailParams
            ), shareViews
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_accounts_management, menu)
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