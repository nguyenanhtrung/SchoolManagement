package com.nguyenanhtrung.schoolmanagement.util

import android.view.View
import androidx.collection.ArrayMap
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

fun GroupAdapter<ViewHolder>.removeFirstItem() {
    if (itemCount == 0) {
        return
    }
    removeGroup(0)
}

fun GroupAdapter<ViewHolder>.removeLastItem() {
    if (itemCount == 0) {
        return
    }
    removeGroup(itemCount - 1)
}

fun GroupAdapter<ViewHolder>.addErrorItem(messageId: Int, retryEvent: () -> Unit) {
    add(ErrorItem(
        messageId,
        object : ErrorItem.OnClickButtonRetryListener {
            override fun onClickButtonRetry(view: View) {
                retryEvent()
            }
        })
    )
}
