package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.remote.model.UserTask
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_task_dashboard.*

class UserTaskItem(private val userTask: UserTask) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.text_task_name.text = userTask.name
        viewHolder.text_task_name.setCompoundDrawablesWithIntrinsicBounds(
            0, userTask.iconPathId, 0, 0
        )

    }

    override fun getLayout(): Int = R.layout.item_task_dashboard
}