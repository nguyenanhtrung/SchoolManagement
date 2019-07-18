package com.nguyenanhtrung.schoolmanagement.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.R

fun TextInputEditText.getString(): String {
    return text.toString()
}

fun TextInputEditText.clearErrorWhenFocus(inputLayout: TextInputLayout) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (s?.length != 0 && inputLayout.error != null ) {
                inputLayout.error = null
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    })
}

fun ImageView.loadImageIfEmptyPath(imagePath: String) {
    if (imagePath.isEmpty()) {
        Glide.with(this)
            .load(R.drawable.image_empty)
            .into(this)
        return
    }
    Glide.with(this)
        .load(imagePath)
        .into(this)
}

fun ImageView.loadImageResource(imageResId: Int) {
    Glide.with(this)
        .load(imageResId)
        .into(this)
}