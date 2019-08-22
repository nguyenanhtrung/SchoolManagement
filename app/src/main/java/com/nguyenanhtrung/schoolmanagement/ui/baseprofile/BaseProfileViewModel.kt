package com.nguyenanhtrung.schoolmanagement.ui.baseprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.ErrorState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Profile
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel

abstract class BaseProfileViewModel : BaseViewModel() {

    lateinit var profile: Profile

    protected val _basicProfileInfo by lazy {
        MutableLiveData<Profile>()
    }
    internal val basicProfileInfo: LiveData<Profile>
        get() = _basicProfileInfo

    protected val _profileImage by lazy {
        MutableLiveData<String>()
    }
    internal val profileImage: LiveData<String>
        get() = _profileImage

    protected val _birthdayInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val birthdayInputError: LiveData<ErrorState>
        get() = _birthdayInputError

    protected val _phoneInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val phoneInputError: LiveData<ErrorState>
        get() = _phoneInputError

    protected val _addressInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val addressInputError: LiveData<ErrorState>
        get() = _addressInputError

    protected val _emailInputError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val emailInputError: LiveData<ErrorState>
        get() = _emailInputError

    protected val _imageSelectedError by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val imageSelectedError: LiveData<ErrorState>
        get() = _imageSelectedError

    fun loadBasicProfileInfo() {
        _basicProfileInfo.value = profile
    }

    fun onProfileImagePicked(imageUri: String) {
        _profileImage.value = imageUri
    }
}