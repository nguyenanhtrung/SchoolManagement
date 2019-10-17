package com.nguyenanhtrung.schoolmanagement.ui.baselistitem

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.*
import com.mikepenz.fastadapter.adapters.GenericItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseFragment
import com.nguyenanhtrung.schoolmanagement.util.EndlessScrollListener


abstract class BaseListItemFragment<T> : BaseFragment() where T : GenericItem {

    private val itemsViewModel by lazy {
        bindItemsViewModel()
    }

    private lateinit var recyclerView: RecyclerView

    private val itemAdapter by lazy {
        GenericItemAdapter()
    }

    private val fastAdapter: GenericFastAdapter by lazy {
        FastAdapter.with(itemAdapter)
    }

    protected val listItemCount = fastAdapter.itemCount

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
        itemsViewModel.loadItems()
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
            itemAdapter.add(it)
        })
    }

    private fun subscribeGetItems() {
        itemsViewModel.getItemsLiveData.observe(this, Observer {
            itemsViewModel.handleStatusGetItems(it)
        })
    }

    private fun subscribeEmptyStateGetItems() {
        itemsViewModel.emptyUsersLiveData.observe(this, Observer {
            when (it) {
                ListEmptyState.CLEAR -> {
                    itemAdapter.clear()
                }
                is ListEmptyState.EMPTY -> {
                    itemAdapter.add(it.emptyView)
                }
            }

        })
    }

    private fun subscribeErrorItems() {
        itemsViewModel.errorItemsLiveData.observe(this, Observer {
            when (it) {
                is ErrorState.NoAction -> {
                    fastAdapter.addEventHook(object : ClickEventHook<ErrorItem>() {
                        override fun onClick(
                            v: View,
                            position: Int,
                            fastAdapter: FastAdapter<ErrorItem>,
                            item: ErrorItem
                        ) {
                            itemsViewModel.onClickButtonRetry()
                        }

                    })
                    itemAdapter.add(ErrorItem(it.messageId))
                }
            }
        })
    }

    private fun subscribeStateLoadingMoreItems() {
        itemsViewModel.stateLoadMoreItemLiveData.observe(this, Observer {
            when (it) {
                Status.LOADING -> itemAdapter.add(LoadMoreItem())
                else -> itemAdapter.remove(listItemCount - 1)
            }
        })
    }

    private fun bindBaseViews() {
        recyclerView = bindRecyclerView()
        searchView = bindSearchView()
    }

    override fun setupUiEvents() {
        bindBaseViews()
        setupRecyclerViewItems()
        setupSearchItemsEvent()
        setupItemClickEvent()
        setupLoadMoreItemEvent()
    }

    private fun setupSearchItemsEvent() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String): Boolean {
                itemAdapter.filter(newText)
                itemAdapter.itemFilter.filterPredicate =
                    { item: GenericItem, constraint: CharSequence? ->
                        itemsViewModel.onSearchItemQueryChange(constraint.toString(), item)
                    }
                return true
            }
        })

    }

    private fun setupItemClickEvent() {
        fastAdapter.onClickListener = object : ClickListener<GenericItem> {
            override fun invoke(
                v: View?,
                adapter: IAdapter<IItem<out RecyclerView.ViewHolder>>,
                item: IItem<out RecyclerView.ViewHolder>,
                position: Int
            ): Boolean {
                itemsViewModel.onClickItem(position)
                return true
            }

        }
    }

    private fun setupRecyclerViewItems() {
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = fastAdapter

    }

    private fun setupLoadMoreItemEvent() {
        if (!::endlessScrollListener.isInitialized) {
            endlessScrollListener =
                object : EndlessScrollListener(recyclerView.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(totalItemsCount: Int, view: RecyclerView) {
                        val itemCount = listItemCount
                        val lastItemPosition = itemCount - 1
                        itemsViewModel.onLoadMoreItem(lastItemPosition)
                    }
                }
        }
        recyclerView.addOnScrollListener(endlessScrollListener)
    }

    protected fun getItem(position: Int): GenericItem? {
        return fastAdapter.getItem(position)
    }

    protected fun notifyItemChanged(position: Int) {
        fastAdapter.notifyItemChanged(position)
    }

    protected fun onClickViewInItemView(clickEventHook: ClickEventHook<out GenericItem>) {
        fastAdapter.addEventHook(clickEventHook)
    }

    protected fun addItem(item: GenericItem) {
        itemAdapter.add(item)
    }

    abstract fun bindRecyclerView(): RecyclerView
    abstract fun bindItemsViewModel(): BaseListItemViewModel<T>
    abstract fun bindSearchView(): SearchView
}