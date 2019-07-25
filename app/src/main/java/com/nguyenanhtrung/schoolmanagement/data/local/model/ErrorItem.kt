package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.include_error_state.*

class ErrorItem(private val onClickButtonRetryListener: OnClickButtonRetryListener) : Item() {

    interface OnClickButtonRetryListener {
        fun onClickButtonRetry(view: View)
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.button_retry.setOnClickListener {
            onClickButtonRetryListener.onClickButtonRetry(viewHolder.button_retry)
        }
    }

    override fun getLayout(): Int = R.layout.include_error_state
}