package com.nguyenanhtrung.schoolmanagement.data.local.model

import com.nguyenanhtrung.schoolmanagement.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_school_room.*

class SchoolRoomItem(val schoolRoom: SchoolRoom) : Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        with(viewHolder) {
            text_room_id.text = schoolRoom.roomNumber
            text_room_name.text = schoolRoom.roomName
        }
    }

    override fun getLayout(): Int = R.layout.item_school_room
}