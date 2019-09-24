package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_account.*

data class UserItem(var user: User) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text_account_email.text = user.name
        viewHolder.text_account_type.text = user.type.name
        viewHolder.image_account.loadImageIfEmptyPath(user.avatarPath)
    }

    override fun getLayout(): Int = R.layout.item_account
}