package com.nguyenanhtrung.schoolmanagement.ui.baselistitem

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.LoadMoreItem
import com.nguyenanhtrung.schoolmanagement.data.local.model.Status
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.util.EndlessScrollListener
import com.nguyenanhtrung.schoolmanagement.util.addErrorItem
import com.nguyenanhtrung.schoolmanagement.util.removeFirstItem
import com.nguyenanhtrung.schoolmanagement.util.removeLastItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

abstract class BaseListItemFragment : BaseFragment() {

    private val itemsViewModel by lazy {
        bindItemsViewModel()
    }

    private lateinit var recyclerView: RecyclerView

    protected val itemAdapter by lazy {
        GroupAdapter<ViewHolder>()
    }

    private lateinit var endlessScrollListener: EndlessScrollListener
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeGetItems()
        subscribeItems()
        subscribeEmptyStateGetItems()
        subscribeErrorItems()
        subscribeStateLoadingMoreItems()
        subscribeClearItems()
    }



    private fun subscribeClearItems() {
        itemsViewModel.clearItemsLiveData.observe(this, Observer {
            if (it) {
                itemAdapter.clear()
            }
        })
    }

    private fun subscribeItems() {
        itemsViewModel.itemsLiveData.observe(this, Observer {
            itemAdapter.addAll(it)
        })
    }

    private fun subscribeGetItems() {
        itemsViewModel.getItemsLiveData.observe(this, Observer {
            itemsViewModel.handleStatusGetItems(it)
        })
    }

    private fun subscribeEmptyStateGetItems() {
        itemsViewModel.emptyUsersLiveData.observe(this, Observer {
            itemAdapter.add(it)
        })
    }

    private fun subscribeErrorItems() {
        itemsViewModel.errorItemsLiveData.observe(this, Observer {
            when (it) {
                is ErrorState.Empty -> itemAdapter.removeFirstItem()
                is ErrorState.NoAction -> {
                    itemAdapter.addErrorItem(it.messageId) {
                        itemsViewModel.onClickButtonRetry()
                    }
                }
            }
        })
    }

    private fun subscribeStateLoadingMoreItems() {
        itemsViewModel.stateLoadMoreItemLiveData.observe(this, Observer {
            when (it) {
                Status.LOADING -> itemAdapter.add(LoadMoreItem())
                else -> itemAdapter.removeLastItem()
            }
        })
    }

    private fun bindBaseViews() {
        recyclerView = bindRecyclerView()
        searchView = bindSearchView()
    }

    override fun setupUiEvents() {
        bindBaseViews()
        setupSearchItemsEvent()
        setupRecyclerViewItems()
        setupItemClickEvent()
        setupLoadMoreItemEvent()
        itemsViewModel.loadItems()
    }

    private fun setupSearchItemsEvent() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                itemsViewModel.onSearchItemQueryChange(newText)
                return false
            }
        })

    }

    private fun setupItemClickEvent() {
        itemAdapter.setOnItemClickListener { item, _ ->
            itemsViewModel.onClickItem(itemAdapter.getAdapterPosition(item))
        }
    }

    private fun setupRecyclerViewItems() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = itemAdapter
    }

    private fun setupLoadMoreItemEvent() {
        if (!::endlessScrollListener.isInitialized) {
            endlessScrollListener =
                object : EndlessScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                        val lastUserItem = itemAdapter.getItem(itemAdapter.itemCount - 1)
                        itemsViewModel.onLoadMoreItem(lastUserItem)
                    }
                }
        }
        recyclerView.addOnScrollListener(endlessScrollListener)
    }

    abstract fun bindRecyclerView(): RecyclerView
    abstract fun bindItemsViewModel(): BaseListItemViewModel
    abstract fun bindSearchView(): SearchView
}