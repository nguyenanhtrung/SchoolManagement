package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_empty.*

class EmptyItem(private val message: String) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title_empty.text = message
    }

    override fun getLayout(): Int = R.layout.item_empty
}