package com.nguyenanhtrung.schoolmanagement.util

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.R

class Validator private constructor() {

    companion object {
        private const val MIN_LENGTH_PASSWORD = 8
        private const val MAX_LENGTH_PASSWORD = 15

        private fun checkValid(
            errorMessageId: Int,
            errorLiveData: MutableLiveData<Int>,
            check: () -> Boolean
        ): Boolean {
            if (!check()) {
                errorLiveData.value = errorMessageId
                return false
            }
            errorLiveData.value = R.string.empty
            return true
        }

        fun isEmailValid(email: String, emailErrorLiveData: MutableLiveData<Int>): Boolean {
            return checkValid(R.string.error_format_email, emailErrorLiveData) {
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        }

        fun isPasswordValid(
            password: String,
            passwordErrorLiveData: MutableLiveData<Int>
        ): Boolean {

            return checkValid(R.string.error_length_password, passwordErrorLiveData) {
                password.length in MIN_LENGTH_PASSWORD..MAX_LENGTH_PASSWORD
            }
        }
    }
}