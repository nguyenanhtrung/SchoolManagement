package com.nguyenanhtrung.schoolmanagement.ui.updateprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.profile.UpdateUserProfileUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject


class ProfileUpdateViewModel @Inject constructor(private val updateUserProfileUseCase: UpdateUserProfileUseCase) :
    BaseViewModel() {

    lateinit var profile: Profile
    var indexProfile: Int = 0

    private val _basicProfileInfo by lazy {
        MutableLiveData<Profile>()
    }
    internal val basicProfileInfo: LiveData<Profile>
        get() = _basicProfileInfo

    private val _profileImage by lazy {
        MutableLiveData<String>()
    }
    internal val profileImage: LiveData<String>
        get() = _profileImage

    private val _birthdayInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val birthdayInputError: LiveData<ErrorState>
        get() = _birthdayInputError

    private val _phoneInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val phoneInputError: LiveData<ErrorState>
        get() = _phoneInputError

    private val _addressInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val addressInputError: LiveData<ErrorState>
        get() = _addressInputError

    private val _emailInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val emailInputError: LiveData<ErrorState>
        get() = _emailInputError

    private val _imageSelectedError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val imageSelectedError: LiveData<ErrorState>
        get() = _imageSelectedError

    private val _stateUpdateProfile by lazy {
        createApiResultLiveData<String>()
    }
    internal val stateUpdateProfile: LiveData<Resource<String>>
        get() = _stateUpdateProfile

    fun loadBasicProfileInfo() {
        _basicProfileInfo.value = profile
    }

    fun onProfileImagePicked(imageUri: String) {
        _profileImage.value = imageUri
    }

    internal fun onClickConfirmUpdateItem(
        birthday: String,
        phoneNumber: String,
        address: String,
        email: String,
        genderId: Int
    ) {
        if (!Validator.isBirthdayValid(birthday, _birthdayInputError)) {
            return
        }

        if (!Validator.isAddressValid(address, _addressInputError)) {
            return
        }

        if (!Validator.isPhoneNumberValid(phoneNumber, _phoneInputError)) {
            return
        }

        if (!Validator.isEmailValid(email, _emailInputError)) {
            return
        }

        if (!Validator.isProfileImageSelected(_profileImage.value, _imageSelectedError)) {
            return
        }

        val profileUpdateParam =
            createProfileUpdateParam(birthday, phoneNumber, address, email, genderId)
        updateUserProfileUseCase.invoke(viewModelScope, profileUpdateParam, _stateUpdateProfile)
    }

    private fun createProfileUpdateParam(
        birthday: String,
        phoneNumber: String,
        address: String,
        email: String,
        genderId: Int
    ): ProfileUpdateParam {
        val imageUri = _profileImage.value ?: ""
        val currentUserInfo = _basicProfileInfo.value
        val fireBaseUserId = currentUserInfo?.fireBaseUserId ?: ""
        val gender = when (genderId) {
            R.id.button_male_gender -> Gender.MALE
            R.id.button_female_gender -> Gender.FEMALE
            else -> throw NoSuchElementException()
        }
        return ProfileUpdateParam(
            imageUri,
            fireBaseUserId,
            birthday,
            phoneNumber,
            address,
            email,
            gender
        )
    }


}