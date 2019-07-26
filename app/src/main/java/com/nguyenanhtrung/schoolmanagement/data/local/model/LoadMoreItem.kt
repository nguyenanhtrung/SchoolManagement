package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class LoadMoreItem : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
    }

    override fun getLayout(): Int = R.layout.item_load_more
}