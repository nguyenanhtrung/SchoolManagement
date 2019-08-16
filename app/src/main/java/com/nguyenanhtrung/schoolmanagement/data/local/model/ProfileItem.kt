package com.nguyenanhtrung.schoolmanagement.data.local.model

import androidx.core.content.ContextCompat
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_profile.*

class ProfileItem constructor(val profile: Profile) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            image_account_profile.loadImageIfEmptyPath(profile.avatarPath)
            text_name.text = profile.name
            val context = itemView.context
            text_account_detail_id.text =
                "${context.getString(R.string.title_account_id)}: ${profile.userId}"
            if (profile.isProfileUpdated) {
                chip_status_profile.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorOnPrimary
                    )
                )
                chip_status_profile.setChipBackgroundColorResource(R.color.colorPrimary)
                chip_status_profile.text = context.getString(R.string.title_updated)
            } else {
                chip_status_profile.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.colorOnBackground
                    )
                )
                chip_status_profile.setChipBackgroundColorResource(R.color.colorBackground)
                chip_status_profile.text = context.getString(R.string.title_not_update)
            }
        }
    }

    override fun getLayout(): Int = R.layout.item_profile



}