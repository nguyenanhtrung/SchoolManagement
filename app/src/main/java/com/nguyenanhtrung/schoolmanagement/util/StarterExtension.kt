package com.nguyenanhtrung.schoolmanagement.util

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment

fun <T> Context.openActivity(target: Class<T>) {
    val intent = Intent(this, target)
    startActivity(intent)
}

fun <T> Fragment.openActivity(target: Class<T>) {
    val activity = requireActivity()
    activity.openActivity(target)
}