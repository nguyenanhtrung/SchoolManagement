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
            "^[a-z0-9A-Z_ÀÁÂÃÈÉÊẾÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶếẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểỄỆỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪễệỉịọỏốồổỗộớờởỡợụủứừỬỮỰỲỴÝỶỸửữựỳỵỷỹ ]+$"
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

        fun isAddressValid(
            address: String,
            addressErrorLiveData: MutableLiveData<ErrorState>
        ): Boolean {
            return checkValid(R.string.error_address_format, addressErrorLiveData) {
                address.isNotEmpty()
            }
        }

        fun isPhoneNumberValid(
            phoneNumber: String,
            phoneErrorLiveData: MutableLiveData<ErrorState>
        ): Boolean {
            return checkValid(R.string.error_phone_format, phoneErrorLiveData) {
                phoneNumber.isNotEmpty()
            }
        }

        fun isBirthdayValid(
            birthday: String,
            birthdayErrorLiveData: MutableLiveData<ErrorState>
        ): Boolean {
            return checkValid(R.string.error_birthday_format, birthdayErrorLiveData) {
                birthday.isNotEmpty()
            }
        }

        fun isProfileImageSelected(
            imageUri: String?,
            imagePickerError: MutableLiveData<ErrorState>
        ): Boolean {
            return checkValid(R.string.error_profile_image_not_selected, imagePickerError) {
                !imageUri.isNullOrEmpty()
            }
        }

        fun isRoomNumberValid(
            roomNumber: String,
            roomNumberError: MutableLiveData<ErrorState>
        ): Boolean {
            return checkValid(R.string.title_empty_room_number, roomNumberError) {
                roomNumber.isNotEmpty()
            }
        }

        fun isRoomNameValid(
            roomName: String,
            roomNameError: MutableLiveData<ErrorState>
        ): Boolean {
            return checkValid(R.string.title_empty_room_name, roomNameError) {
                roomName.isNotEmpty()
            }
        }


    }
}