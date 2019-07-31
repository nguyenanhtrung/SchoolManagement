package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_sheet_filter.*

class FilterItem(val filterData: FilterData) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val context = viewHolder.itemView.context
        viewHolder.chip_item_filter.text = context.getString(filterData.titleId)
        viewHolder.chip_item_filter.isSelected = filterData.isSelected
    }

    override fun getLayout(): Int = R.layout.item_sheet_filter

    fun setItemSelected(isSelected: Boolean) {
        filterData.isSelected = isSelected
    }

}