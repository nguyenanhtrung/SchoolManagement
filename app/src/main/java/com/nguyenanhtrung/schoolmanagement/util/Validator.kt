package com.nguyenanhtrung.schoolmanagement.util

import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState

class Validator private constructor() {

    companion object {
        private const val MIN_LENGTH_PASSWORD = 8
        private const val MAX_LENGTH_PASSWORD = 15

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