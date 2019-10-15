package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_empty.*

class EmptyItem(private val messageId: Int) : AbstractItem<EmptyItem.EmptyViewHolder>() {


    override val layoutRes: Int = R.layout.item_empty
    override val type: Int = messageId

    override fun getViewHolder(v: View): EmptyViewHolder = EmptyViewHolder(v)


    class EmptyViewHolder(override val containerView: View) :
        FastAdapter.ViewHolder<EmptyItem>(containerView), LayoutContainer {

        override fun bindView(item: EmptyItem, payloads: MutableList<Any>) {
            val context = containerView.context
            title_empty.text = context.resources.getString(item.messageId)
        }

        override fun unbindView(item: EmptyItem) {
            title_empty.text = null
        }

    }
}