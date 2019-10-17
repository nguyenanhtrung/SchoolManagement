package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class LoadMoreItem : AbstractItem<LoadMoreItem.LoadMoreViewHolder>() {

    override val type: Int = 999

    override fun getViewHolder(v: View): LoadMoreViewHolder = LoadMoreViewHolder(v)

    override val layoutRes: Int = R.layout.item_load_more

    class LoadMoreViewHolder(val view: View) : FastAdapter.ViewHolder<LoadMoreItem>(view) {
        override fun bindView(item: LoadMoreItem, payloads: MutableList<Any>) {
        }

        override fun unbindView(item: LoadMoreItem) {
        }
    }
}