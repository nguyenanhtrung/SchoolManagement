package com.nguyenanhtrung.schoolmanagement.ui.profiledetail

import androidx.collection.ArrayMap
import androidx.collection.arrayMapOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.profile.GetProfileDetailUseCase
import com.nguyenanhtrung.schoolmanagement.domain.profile.SaveProfileModificationUseCase
import com.nguyenanhtrung.schoolmanagement.ui.baseprofile.BaseProfileViewModel
import com.nguyenanhtrung.schoolmanagement.util.AppKey
import javax.inject.Inject

class ProfileDetailViewModel @Inject constructor(
    private val getProfileDetailUseCase: GetProfileDetailUseCase,
    private val saveProfileModificationUseCase: SaveProfileModificationUseCase
) :
    BaseProfileViewModel() {

    private lateinit var profileModificationInfo: ProfileDetail
    internal lateinit var profileDetail: ProfileDetail

    private val _profileDetailResult by lazy {
        createApiResultLiveData<ProfileDetail>()
    }
    internal val profileDetailResult: LiveData<Resource<ProfileDetail>>
        get() = _profileDetailResult

    private val _stateModifyProfileInfo by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateModifyProfileInfo: LiveData<ModificationState>
        get() = _stateModifyProfileInfo

    private val _stateResetProfileImage by lazy {
        MutableLiveData<Visibility>()
    }
    internal val stateResetProfileImage: LiveData<Visibility>
        get() = _stateResetProfileImage

    internal fun onClickButtonEditProfile() {
        _stateModifyProfileInfo.value = ModificationState.Edit
    }

    private val _saveProfileModification by lazy {
        createApiResultLiveData<String>()
    }
    internal val saveProfileModification: LiveData<Resource<String>>
        get() = _saveProfileModification

    private fun updateModifiedField(
        modifiedFields: ArrayMap<String, Any>,
        currentValue: String,
        newValue: String,
        key: String
    ) {
        if (newValue != currentValue) {
            modifiedFields[key] = newValue
        }
    }

    private fun createModifiedFields(
        originProfileInfo: ProfileDetail,
        newProfileInfo: ProfileDetail
    ): ArrayMap<String, Any> {
        val modifiedFields = arrayMapOf<String, Any>()

        updateModifiedField(
            modifiedFields,
            originProfileInfo.birthday,
            newProfileInfo.birthday,
            AppKey.BIRTHDAY_FIELD_PROFILE_PATH
        )

        updateModifiedField(
            modifiedFields,
            originProfileInfo.phoneNumber,
            newProfileInfo.phoneNumber,
            AppKey.PHONE_NUMBER_FIELD_PROFILE_PATH
        )

        updateModifiedField(
            modifiedFields,
            originProfileInfo.address,
            newProfileInfo.address,
            AppKey.ADDRESS_FIELD_PROFILE_PATH
        )

        updateModifiedField(
            modifiedFields,
            originProfileInfo.email,
            newProfileInfo.email,
            AppKey.EMAIL_FIELD_PROFILE_PATH
        )

        return modifiedFields
    }


    internal fun onClickButtonSaveModifiedProfile(newProfileInfo: ProfileDetail) {
        if (isProfileModified(newProfileInfo)) {
            this.profileModificationInfo = newProfileInfo
            val modifiedFields = createModifiedFields(profileDetail, newProfileInfo)

            val originImagePath = profile.profileImagePath
            val selectedImagePath = _profileImage.value ?: originImagePath

            var newImagePath = ""
            if (originImagePath != selectedImagePath) {
                newImagePath = selectedImagePath
            }

            val profileModificationParams = ProfileModificationParams(
                profile.fireBaseUserId,
                newImagePath,
                modifiedFields
            )

            saveProfileModificationUseCase.invoke(
                viewModelScope,
                profileModificationParams,
                _saveProfileModification
            )
        } else {
            _stateModifyProfileInfo.value = ModificationState.Save
        }

    }

    private fun isProfileModified(newProfileInfo: ProfileDetail): Boolean {
        val originImagePath = profile.profileImagePath
        val selectImagePath = _profileImage.value ?: originImagePath

        if (originImagePath != selectImagePath) {
            return true
        }
        val currentProfileInfo = _profileDetailResult.value?.data ?: return false
        return currentProfileInfo != newProfileInfo
    }

    internal fun onClickButtonResetProfileImage() {
        val originImagePath = profile.profileImagePath
        _profileImage.value = originImagePath
        _stateResetProfileImage.value = Visibility.HIDE
    }

    override fun onSuccessProfileImagePicked() {
        val stateResetImage = _stateResetProfileImage.value
        if (stateResetImage == null || stateResetImage == Visibility.HIDE) {
            _stateResetProfileImage.value = Visibility.SHOW
        }
    }

    internal fun onSuccessSaveProfileModification(newImagePath: String) {
        _stateModifyProfileInfo.value = ModificationState.Save
        if (newImagePath.isNotEmpty()) {
            profile.profileImagePath = newImagePath
        }
        if (::profileModificationInfo.isInitialized) {
            profileDetail = profileModificationInfo
        }
    }

    internal fun loadProfileDetail() {
        val fireBaseUserId = profile.fireBaseUserId
        getProfileDetailUseCase.invoke(viewModelScope, fireBaseUserId, _profileDetailResult)
    }

    internal fun initStateModifyProfileInfo() {
        _stateModifyProfileInfo.value = ModificationState.Save
    }
}