package com.nguyenanhtrung.schoolmanagement.ui.filtersheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nguyenanhtrung.schoolmanagement.MyApplication
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.ui.main.MainViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.bottom_sheet_filter_fragment.*
import javax.inject.Inject

class FilterBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private val filterArgs: FilterBottomSheetDialogFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val filterViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[FilterSheetViewModel::class.java]
    }

    private val mainViewModel by lazy {
        ViewModelProviders.of(requireActivity())[MainViewModel::class.java]
    }

    private val filterAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        onReceivedArgs()
        subscribeFilterItems()
        subscribeFilterItemChanged()
        subscribeConfirmFilter()
    }

    private fun subscribeConfirmFilter() {
        filterViewModel.stateConfirmFilter.observe(this, Observer {
            mainViewModel._filterItemLiveData.value = it
            findNavController().popBackStack()
        })
    }

    private fun subscribeFilterItemChanged() {
        filterViewModel.stateFilterItemChange.observe(this, Observer {
            filterAdapter.notifyItemChanged(it)
        })
    }

    private fun injectDependencies() {
        val myApp = requireActivity().application as MyApplication
        myApp.appComponent.inject(this)
    }

    private fun subscribeFilterItems() {
        filterViewModel.filterItems.observe(this, Observer {
            filterAdapter.addAll(it)
        })
    }

    private fun onReceivedArgs() {
        val filterDatas = filterArgs.filterDatas
        filterViewModel.filterDatas = filterDatas
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_filter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonEvents()
        setupFilterItemsRecyclerView()
        setupEventFilterItems()
        filterViewModel.loadFilterItems()
    }

    private fun setupButtonEvents() {
        button_close_sheet.setOnClickListener {
            findNavController().popBackStack()
        }

        button_confirm_filter.setOnClickListener {
            filterViewModel.onClickButtonConfirm()
        }
    }

    private fun setupEventFilterItems() {
        filterAdapter.setOnItemClickListener { item, view ->
            val position = filterAdapter.getAdapterPosition(item)
            filterViewModel.onClickFilterItem(position)
        }
    }

    private fun setupFilterItemsRecyclerView() {
        val flexBoxLayoutManager = FlexboxLayoutManager(requireActivity())
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW
        flexBoxLayoutManager.justifyContent = JustifyContent.SPACE_BETWEEN
        recycler_view_filters.layoutManager = flexBoxLayoutManager

        recycler_view_filters.adapter = filterAdapter
    }
}