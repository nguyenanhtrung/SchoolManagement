package com.nguyenanhtrung.schoolmanagement.util

import android.content.Context
import android.content.Intent

fun <T> Context.openActivity(target: Class<T>) {
    val intent = Intent(this, target)
    startActivity(intent)
}