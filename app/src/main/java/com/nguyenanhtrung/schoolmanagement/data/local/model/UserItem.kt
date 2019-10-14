package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.ViewUtils
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_account.*

data class UserItem(
    var user: User
) : Item() {

     var onClickUpdateProfileListener: OnClickUpdateProfileListener? = null


    interface OnClickUpdateProfileListener {

        fun onClickUpdateProfile(view: View, profileUpdateArguments: ProfileUpdateArguments)
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.image_account.loadImageIfEmptyPath(user.avatarPath)
        viewHolder.text_account_name.text = user.name
        viewHolder.text_account_type.text = user.type.name
        ViewUtils.setColorTextAccountType(viewHolder.text_account_type, user.type.id)
        when (user.profile_status) {
            true -> viewHolder.text_profile_status.visibility = View.GONE
            else -> viewHolder.text_profile_status.visibility = View.VISIBLE
        }

        viewHolder.text_profile_status.setOnClickListener {
            onClickUpdateProfileListener?.onClickUpdateProfile(it, ProfileUpdateArguments(
                user.firebaseUserId,
                user.id,
                user.type,
                user.name
            )
            )
        }
    }


    override fun getLayout(): Int = R.layout.item_account
}