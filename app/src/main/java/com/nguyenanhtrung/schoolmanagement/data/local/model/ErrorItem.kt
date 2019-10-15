package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.include_error_state.*

class ErrorItem(
    private val messageId: Int
) : AbstractItem<ErrorItem.ErrorViewHolder>() {

    override val layoutRes: Int = R.layout.include_error_state

    override val type: Int = messageId

    override fun getViewHolder(v: View) = ErrorViewHolder(v)


    class ErrorViewHolder(override val containerView: View) :
        FastAdapter.ViewHolder<ErrorItem>(containerView), LayoutContainer {

        override fun bindView(item: ErrorItem, payloads: MutableList<Any>) {
            val context = containerView.context
            val message = context.getString(item.messageId)
            text_error_content.text = message
        }

        override fun unbindView(item: ErrorItem) {
            text_error_content.text = null
        }

    }
}