package com.nguyenanhtrung.schoolmanagement.ui.schoolroom

import android.app.Application
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemFragment
import com.nguyenanhtrung.schoolmanagement.ui.baselistitem.BaseListItemViewModel
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_list_item_with_adding_button.*
import kotlinx.android.synthetic.main.include_search_view.*
import javax.inject.Inject

class SchoolRoomsFragment : BaseListItemFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val schoolRoomsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SchoolRoomsViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    override fun injectDependencies(application: Application) {
        val myApp = application as MyApplication
        myApp.appComponent.inject(this)
    }

    override fun createFragmentViewModel(): BaseViewModel = schoolRoomsViewModel

    override fun bindActivityViewModel(): BaseActivityViewModel = mainViewModel

    override fun bindItemsViewModel(): BaseListItemViewModel = schoolRoomsViewModel

    override fun bindRecyclerView(): RecyclerView = recycler_view_items

    override fun bindSearchView(): SearchView = edit_text_search


    override fun inflateLayout(inflater: LayoutInflater, container: ViewGroup?): View? {
        return inflater.inflate(R.layout.fragment_list_item_with_adding_button, container, false)
    }

    override fun setupUiEvents() {
        super.setupUiEvents()
        float_button_add.setOnClickListener {
            findNavController().navigate(SchoolRoomsFragmentDirections.actionSchoolRoomsDestToAddSchoolRoomDest())
        }
    }
}