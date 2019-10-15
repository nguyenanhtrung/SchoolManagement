package com.nguyenanhtrung.schoolmanagement.data.local.model

import android.view.View
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem
import com.nguyenanhtrung.schoolmanagement.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_school_room.*

class SchoolRoomItem(var schoolRoom: SchoolRoom) :
    AbstractItem<SchoolRoomItem.SchoolRoomViewHolder>() {

    override val type: Int = schoolRoom.roomId.toInt()

    override fun getViewHolder(v: View): SchoolRoomViewHolder = SchoolRoomViewHolder(v)

    override val layoutRes: Int = R.layout.item_school_room

    class SchoolRoomViewHolder(override val containerView: View) :
        FastAdapter.ViewHolder<SchoolRoomItem>(containerView), LayoutContainer {

        override fun bindView(item: SchoolRoomItem, payloads: MutableList<Any>) {
            val schoolRoom = item.schoolRoom
            text_room_id.text = schoolRoom.roomNumber
            text_room_name.text = schoolRoom.roomName
        }

        override fun unbindView(item: SchoolRoomItem) {
            text_room_id.text = null
            text_room_name.text = null
        }

    }
}