package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.content.Context
import androidx.core.content.ContextCompat
import com.google.android.material.chip.Chip
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.util.loadImageIfEmptyPath
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_profile.*

class ProfileItem constructor(val profile: Profile) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            image_account_profile.loadImageIfEmptyPath(profile.profileImagePath)
            text_name.text = profile.name
            text_phone_number.text = profile.phoneNumber
            val context = itemView.context
            text_account_detail_id.text =
                "${context.getString(R.string.title_account_id)}: ${profile.userId}"
            showProfileStatus(viewHolder.chip_status_profile, context)
        }
    }

    private fun showProfileStatus(chipStatus: Chip, context: Context) {
        if (profile.isProfileUpdated) {
            chipStatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorOnPrimary
                )
            )
            chipStatus.setChipBackgroundColorResource(R.color.colorPrimary)
            chipStatus.text = context.getString(R.string.title_updated)
        } else {
            chipStatus.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorOnBackground
                )
            )
            chipStatus.setChipBackgroundColorResource(R.color.colorBackground)
            chipStatus.text = context.getString(R.string.title_not_update)
        }
    }


    override fun getLayout(): Int = R.layout.item_profile


}