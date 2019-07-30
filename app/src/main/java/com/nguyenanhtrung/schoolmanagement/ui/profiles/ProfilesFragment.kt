package com.nguyenanhtrung.schoolmanagement.ui.profiles

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_profiles.*
import javax.inject.Inject

class ProfilesFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val profileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ProfilesViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    private val profileAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = profileViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_profiles, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        subscribeGetProfiles()
        subscribeStateLoadMoreProfiles()
    }

    private fun subscribeStateLoadMoreProfiles() {
        profileViewModel.stateLoadingMoreProfiles.observe(this, Observer {
            when (it) {
                Status.LOADING -> profileAdapter.add(LoadMoreItem())
                else -> profileAdapter.removeGroup(
                    profileAdapter.itemCount - 1
                )
            }
        })
    }

    private fun subscribeGetProfiles() {
        profileViewModel.userProfilesLiveData.observe(this, Observer {
            when (it.status) {
                Status.EMPTY -> {
                    profileAdapter.add(EmptyItem(it.error))
                }
                Status.FAILURE, Status.EXCEPTION -> {
                    showErrorViewProfileItem(it.error)
                }
                Status.SUCCESS -> {
                    removeErrorState()
                    profileViewModel.onLoadMoreSuccess()
                    it.data?.let {profiles ->
                        showUserProfiles(profiles)
                    }
                }
                Status.COMPLETE -> profileViewModel.onLoadMoreSuccess()
            }
        })
    }

    private fun showUserProfiles(profileItems: MutableList<ProfileItem>) {
        profileAdapter.addAll(profileItems)
    }

    private fun removeErrorState() {
        if (profileAdapter.itemCount == 0) {
            return
        }
        profileAdapter.removeGroup(0)
    }

    private fun showErrorViewProfileItem(errorMessageId: Int) {
        if (profileAdapter.itemCount == 0) {
            profileAdapter.add(
                ErrorItem(
                    errorMessageId,
                    object : ErrorItem.OnClickButtonRetryListener {
                        override fun onClickButtonRetry(view: View) {
                            //load profile again
                            profileViewModel.loadProfiles()
                        }
                    })
            )
        }
    }

    override fun setupUiEvents() {
        setupToolbar()
        setupProfilesRecyclerView()
        setupLoadMoreProfilesEvent()
        profileViewModel.loadProfiles()
    }

    private fun setupLoadMoreProfilesEvent() {
        val layoutMananger = recycler_view_profiles.layoutManager as LinearLayoutManager
        recycler_view_profiles.addOnScrollListener(object : EndlessScrollListener(layoutMananger) {
            override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                val lastProfileItem = profileAdapter.getItem(profileAdapter.itemCount - 1)
                if (lastProfileItem is ProfileItem) {
                    profileViewModel.onLoadMoreProfiles(lastProfileItem.profile)
                }
            }
        })
    }

    private fun setupToolbar() {
        mainViewModel.showToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_profiles, menu)
        super.onCreateOptionsMenu(menu,inflater);
    }

    private fun setupProfilesRecyclerView() {
        recycler_view_profiles.layoutManager = LinearLayoutManager(requireActivity())
        recycler_view_profiles.adapter = profileAdapter
    }
}