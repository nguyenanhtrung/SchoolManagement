package com.nguyenanhtrung.schoolmanagement.ui.baselistitem

import android.os.Bundle
import android.view.View
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeGetItems()
        subscribeItems()
        subscribeEmptyStateGetItems()
        subscribeErrorItems()
        subscribeStateLoadingMoreItems()
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

    override fun setupUiEvents() {
        setupRecyclerViewItems()
        setupItemClickEvent()
        setupLoadMoreItemEvent()
        itemsViewModel.loadItems()
    }

    private fun setupItemClickEvent() {
        itemAdapter.setOnItemClickListener { item, _ ->
            itemsViewModel.onClickItem(itemAdapter.getAdapterPosition(item))
        }
    }

    private fun setupRecyclerViewItems() {
        recyclerView = bindRecyclerView()
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = itemAdapter
    }

    private fun setupLoadMoreItemEvent() {
        if (!::endlessScrollListener.isInitialized) {
            endlessScrollListener = object : EndlessScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
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
}