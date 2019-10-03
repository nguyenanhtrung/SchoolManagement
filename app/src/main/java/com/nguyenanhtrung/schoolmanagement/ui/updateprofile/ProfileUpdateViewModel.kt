package com.nguyenanhtrung.schoolmanagement.ui.updateprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.profile.UpdateUserProfileUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject


class ProfileUpdateViewModel @Inject constructor(private val updateUserProfileUseCase: UpdateUserProfileUseCase) :
    BaseProfileViewModel() {


    internal lateinit var profileUpdateArgs: ProfileUpdateArguments

    private val _accountInfoLiveData by lazy {
        MutableLiveData<ProfileUpdateAccountInfo>()
    }
    internal val accountInfoLiveData: LiveData<ProfileUpdateAccountInfo>
        get() = _accountInfoLiveData

    private val _stateUpdateProfile by lazy {
        createApiResultLiveData<String>()
    }
    internal val stateUpdateProfile: LiveData<Resource<String>>
        get() = _stateUpdateProfile


    internal fun onClickConfirmUpdateItem(
        profileUpdateInput: ProfileUpdateInput
    ) {
        if (!Validator.isBirthdayValid(profileUpdateInput.birthday, _birthdayInputError)) {
            return
        }

        if (!Validator.isAddressValid(profileUpdateInput.address, _addressInputError)) {
            return
        }

        if (!Validator.isPhoneNumberValid(profileUpdateInput.phoneNumber, _phoneInputError)) {
            return
        }

        if (!Validator.isEmailValid(profileUpdateInput.email, _emailInputError)) {
            return
        }

        if (!Validator.isProfileImageSelected(_profileImage.value, _imageSelectedError)) {
            return
        }

        val profileUpdateParam =
            createProfileUpdateParam(profileUpdateInput)
        updateUserProfileUseCase.invoke(viewModelScope, profileUpdateParam, _stateUpdateProfile)
    }

    private fun createProfileUpdateParam(
        profileUpdateInput: ProfileUpdateInput
    ): ProfileUpdateParam {
        val imageUri = _profileImage.value ?: ""
        val fireBaseUserId = profileUpdateArgs.firebaseUserId
        val gender = when (profileUpdateInput.genderId) {
            R.id.button_male_gender -> Gender.MALE
            R.id.button_female_gender -> Gender.FEMALE
            else -> throw NoSuchElementException()
        }
        return ProfileUpdateParam(
            imageUri,
            fireBaseUserId,
            profileUpdateInput.name,
            profileUpdateInput.birthday,
            profileUpdateInput.phoneNumber,
            profileUpdateInput.address,
            profileUpdateInput.email,
            gender
        )
    }

    internal fun showAccountInfo() {
        if (::profileUpdateArgs.isInitialized) {
            _accountInfoLiveData.value = ProfileUpdateAccountInfo(
                profileUpdateArgs.userId.toString(),
                profileUpdateArgs.accountName,
                profileUpdateArgs.userType
            )
        }
    }


}