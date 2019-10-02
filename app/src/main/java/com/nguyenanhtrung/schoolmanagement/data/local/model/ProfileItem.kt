package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.ViewUtils
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_profile.*

class ProfileItem constructor(val profile: Profile) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            image_profile.loadImageIfEmptyPath(profile.profileImagePath)
            text_name.text = profile.name
            text_phone_number.text = profile.phoneNumber
            val context = itemView.context
            text_account_detail_id.text =
                "${context.getString(R.string.title_account_id)}: ${profile.userId}"
            val userType = profile.userType
            ViewUtils.setColorTextAccountType(text_account_type, userType.id)
            text_account_type.text = userType.name
        }
    }

    override fun getLayout(): Int = R.layout.item_profile


}