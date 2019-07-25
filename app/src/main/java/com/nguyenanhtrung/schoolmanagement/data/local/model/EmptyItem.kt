package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_empty.*

class EmptyItem(private val messageId: Int) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        viewHolder.title_empty.text = context.resources.getString(messageId)
    }

    override fun getLayout(): Int = R.layout.item_empty
}