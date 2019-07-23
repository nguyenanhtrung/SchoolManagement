package com.nguyenanhtrung.schoolmanagement.util

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import java.util.regex.Pattern

class Validator private constructor() {

    companion object {
        private const val MIN_LENGTH_PASSWORD = 8
        private const val MAX_LENGTH_PASSWORD = 15
        private const val MAX_LENGTH_NAME = 40
        private const val PATTERN_FORMAT_NAME =
            "^[a-z0-9A-Z_ÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ ]+$"
        private val patterns = Pattern.compile(PATTERN_FORMAT_NAME)


        private fun checkValid(
            errorMessageId: Int,
            errorLiveData: MutableLiveData<ErrorState>,
            check: () -> Boolean
        ): Boolean {
            if (!check()) {
                errorLiveData.value = ErrorState.NoAction(errorMessageId)
                return false
            }
            errorLiveData.value = ErrorState.Empty
            return true
        }

        fun isEmailValid(email: String, emailErrorLiveData: MutableLiveData<ErrorState>): Boolean {
            return checkValid(R.string.error_format_email, emailErrorLiveData) {
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        }

        fun isNameValid(name: String, nameErrorLiveData: MutableLiveData<ErrorState>): Boolean {
            return checkValid(R.string.error_length_name, nameErrorLiveData) {
                name.length <= MAX_LENGTH_NAME
            } && checkValid(R.string.error_format_name, nameErrorLiveData) {
                patterns.matcher(name).matches()
            }
        }

        fun isPasswordValid(
            password: String,
            passwordErrorLiveData: MutableLiveData<ErrorState>
        ): Boolean {
            return checkValid(R.string.error_length_password, passwordErrorLiveData) {
                password.length in MIN_LENGTH_PASSWORD..MAX_LENGTH_PASSWORD
            }
        }
    }
}