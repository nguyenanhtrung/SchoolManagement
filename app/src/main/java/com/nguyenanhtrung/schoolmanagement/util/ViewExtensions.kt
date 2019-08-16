package com.nguyenanhtrung.schoolmanagement.util

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState

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

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    })
}

fun TextInputEditText.clearText() {
    if (text.isNullOrEmpty()) {
        return
    }
    setText("")
}

fun TextInputEditText.enableInput(inputLayout: TextInputLayout) {
    isFocusable = true
    isFocusableInTouchMode = true
    if (inputLayout.visibility != View.VISIBLE) {
        inputLayout.visibility = View.VISIBLE
    }
    if (inputLayout.endIconMode == TextInputLayout.END_ICON_NONE) {
        inputLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
    }

}

fun TextInputEditText.disableInput(inputLayout: TextInputLayout) {
    isFocusable = false
    isFocusableInTouchMode = false
    if (inputLayout.endIconMode == TextInputLayout.END_ICON_CLEAR_TEXT) {
        inputLayout.endIconMode = TextInputLayout.END_ICON_NONE
    }
}

fun TextInputEditText.disableEdit(inputLayout: TextInputLayout) {
    inputType = InputType.TYPE_NULL
    isFocusable = false
    if (inputLayout.endIconMode == TextInputLayout.END_ICON_CLEAR_TEXT) {
        inputLayout.endIconMode = TextInputLayout.END_ICON_NONE
    }
}


fun TextInputLayout.setErrorWithState(errorState: ErrorState) {
    when(errorState) {
        is ErrorState.NoAction -> error = context.getString(errorState.messageId)
        is ErrorState.Empty -> {
            if (error != null) {
                error = null
            }
        }
    }
}

fun Fragment.showKeyboard(view: View) {
    val input = getSystemService(requireActivity() ,InputMethodManager::class.java) as InputMethodManager
    input.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

fun Fragment.hideKeyboard(view: View) {
    val input = getSystemService(requireActivity() ,InputMethodManager::class.java) as InputMethodManager
    input.hideSoftInputFromWindow(view.windowToken, 0)
}

fun ImageView.loadImageIfEmptyPath(imagePath: String) {
    if (imagePath.isEmpty()) {
        Glide.with(this)
            .load(R.drawable.image_empty)
            .override(150, 150)
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

