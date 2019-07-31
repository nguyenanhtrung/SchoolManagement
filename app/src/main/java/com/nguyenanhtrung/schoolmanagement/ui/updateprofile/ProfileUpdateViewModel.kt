package com.nguyenanhtrung.schoolmanagement.ui.updateprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.nguyenanhtrung.schoolmanagement.data.local.model.Profile
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class ProfileUpdateViewModel @Inject constructor() : BaseViewModel() {
    lateinit var profile: Profile

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

    fun loadBasicProfileInfo() {
        _basicProfileInfo.value = profile
    }

    fun onProfileImagePicked(imageUri: String) {
        _profileImage.value = imageUri
    }
}