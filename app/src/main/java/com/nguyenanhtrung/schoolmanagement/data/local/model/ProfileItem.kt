package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.ViewUtils
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_profile.*

class ProfileItem constructor(val profile: Profile) : AbstractItem<ProfileItem.ProfileViewHolder>() {

    override val type: Int = profile.userId.toInt()

    override fun getViewHolder(v: View): ProfileViewHolder = ProfileViewHolder(v)

    override val layoutRes: Int = R.layout.item_profile


    class ProfileViewHolder(override val containerView: View) :
        FastAdapter.ViewHolder<ProfileItem>(containerView), LayoutContainer {

        override fun bindView(item: ProfileItem, payloads: MutableList<Any>) {
            val profile = item.profile
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

        override fun unbindView(item: ProfileItem) {
            text_name.text = null
            text_phone_number.text = null
            text_account_detail_id.text = null
            text_account_type.text = null
        }

    }


}