package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_loading_list.*

class LoadingItem : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.lottie_loading_view.playAnimation()
    }

    override fun getLayout(): Int = R.layout.item_loading_list

}