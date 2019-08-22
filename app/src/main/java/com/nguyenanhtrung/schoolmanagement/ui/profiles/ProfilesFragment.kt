package com.nguyenanhtrung.schoolmanagement.ui.profiles

import android.app.Application
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
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
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemFragment
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.nguyenanhtrung.schoolmanagement.util.EndlessScrollListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_profiles.*
import kotlinx.android.synthetic.main.include_search_view.*
import javax.inject.Inject

class ProfilesFragment : BaseListItemFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val profileViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ProfilesViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun bindRecyclerView(): RecyclerView = recycler_view_profiles

    override fun bindItemsViewModel(): BaseListItemViewModel = profileViewModel

    override fun bindSearchView(): SearchView = edit_text_search

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
        subscribeProfileUpdated()
        subscribeFilterProfileDatas()
        subscribeSelectedFilterItem()
    }

    private fun subscribeSelectedFilterItem() {
        mainViewModel.filterItemLiveData.observe(this, Observer {
            profileViewModel.onSelectedFilterItem(it)
        })
    }

    private fun subscribeFilterProfileDatas() {
        profileViewModel.filterProfileDatas.observe(this, Observer {
            it.data?.let { filterDatas ->
                navigateToFilterScreen(filterDatas)
            }
        })
    }

    private fun navigateToFilterScreen(filterDatas: Array<FilterData>) {
        findNavController().navigate(
            ProfilesFragmentDirections.actionProfilesDestToFilterBottomSheetDialogFragment(
                filterDatas
            )
        )
    }

    override fun setupUiEvents() {
        super.setupUiEvents()
        setupToolbar()
        subscribeNavigateToProfileUpdateScreen()
        subscribeNavigateToProfileDetailScreen()
    }

    private fun subscribeProfileUpdated() {
        mainViewModel.profileUpdated.observe(this, Observer {
            it.getContentIfNotHandled()?.let { imageUri ->
                val posItemSelected = profileViewModel.posItemSelected
                val profileItem = itemAdapter.getItem(posItemSelected)
                if (profileItem is ProfileItem) {
                    val profile = profileItem.profile
                    profile.isProfileUpdated = true
                    profile.profileImagePath = imageUri
                    itemAdapter.notifyItemChanged(posItemSelected)
                }
            }
        })
    }

    private fun subscribeNavigateToProfileDetailScreen() {
        profileViewModel.profileDetailScreen.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { profile ->
                findNavController().navigate(
                    ProfilesFragmentDirections.actionProfilesDestToProfileDetailFragment(
                        profile
                    )
                )
            }
        })
    }

    private fun subscribeNavigateToProfileUpdateScreen() {
        profileViewModel.profileUpdateScreen.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let { params ->
                findNavController().navigate(
                    ProfilesFragmentDirections.actionProfilesDestToProfileUpdateFragment(
                        params.second, params.first
                    )
                )
            }
        })
    }

    private fun setupToolbar() {
        mainViewModel.showToolbar()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_profiles, menu)
        super.onCreateOptionsMenu(menu, inflater);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_dest -> {
                true
            }
            R.id.filter_dest -> {
                profileViewModel.onClickItemFilterProfiles()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}