package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.ViewUtils
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_account.*

data class UserItem(
    var user: User
) : AbstractItem<UserItem.UserViewHolder>() {

    override val layoutRes: Int = R.layout.item_account

    override val type: Int = user.id.toInt()

    override fun getViewHolder(v: View): UserViewHolder = UserViewHolder(v)


    class UserViewHolder(override val containerView: View): FastAdapter.ViewHolder<UserItem>(containerView),
        LayoutContainer {



        override fun bindView(item: UserItem, payloads: MutableList<Any>) {
            val user = item.user
            image_account.loadImageIfEmptyPath(user.avatarPath)
            text_account_name.text = user.name
            val userType = user.type
            text_account_type.text = userType.name
            ViewUtils.setColorTextAccountType(text_account_type, userType.id)
            when(user.profile_status) {
                true -> text_profile_status.visibility = View.GONE
                else -> text_profile_status.visibility = View.VISIBLE
            }
        }

        override fun unbindView(item: UserItem) {
            text_account_name.text = null
            text_account_type.text = null
            text_profile_status.text = null
        }

    }
}