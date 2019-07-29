package com.nguyenanhtrung.schoolmanagement.ui.accountdetail

import androidx.collection.ArrayMap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.user.ChangeUserPassUseCase
import com.nguyenanhtrung.schoolmanagement.domain.user.UpdateUserInfoUseCase
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import timber.log.Timber
import javax.inject.Inject

class AccountDetailViewModel @Inject constructor(
    private val getUserTypesUseCase: GetUserTypesUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val changeUserPassUseCase: ChangeUserPassUseCase
) : BaseViewModel() {
    internal lateinit var currentUserInfo: User


    private val _currentUserLiveData by lazy {
        MutableLiveData<User>()
    }
    internal val currentUserLiveData: LiveData<User>
        get() = _currentUserLiveData

    private val _userTypesLiveData by lazy {
        createApiResultLiveData<List<UserType>>()
    }
    internal val userTypesLiveData: LiveData<Resource<List<UserType>>>
        get() = _userTypesLiveData

    private val _indexUserTypeSelected by lazy {
        MutableLiveData<Int>()
    }
    internal val indexUserTypeSelected: LiveData<Int>
        get() = _indexUserTypeSelected

    private val _stateModifyInfo by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateModifyInfo: LiveData<ModificationState>
        get() = _stateModifyInfo

    private val _stateModifyPassword by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateModifyPassword: LiveData<ModificationState>
        get() = _stateModifyPassword

    private val _stateUpdateBasicInfo by lazy {
        createApiResultLiveData<Unit>()
    }
    internal val stateUpdateBasicInfo: LiveData<Resource<Unit>>
        get() = _stateUpdateBasicInfo

    private val _errorNameLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val errorNameLiveData: LiveData<ErrorState>
        get() = _errorNameLiveData

    private val _errorPasswordLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val errorPasswordLiveData: LiveData<ErrorState>
        get() = _errorPasswordLiveData

    private val _resultChangePassword by lazy {
        createApiResultLiveData<Unit>()
    }
    internal val resultChangePassword: LiveData<Resource<Unit>>
        get() = _resultChangePassword


    internal fun onClickModifyInfoButton() {
        if (_stateModifyInfo.value == null || _stateModifyInfo.value == ModificationState.Save) {
            _stateModifyInfo.value = ModificationState.Edit
        } else {
            _stateModifyInfo.value = ModificationState.Save
        }
    }

    internal fun onClickModifyPasswordButton() {
        if (_stateModifyPassword.value == null || _stateModifyPassword.value == ModificationState.Save) {
            _stateModifyPassword.value = ModificationState.Edit
        } else {
            _stateModifyPassword.value = ModificationState.Save
        }
    }

    internal fun saveBasicInfoModification(name: String, indexSelectedType: Int) {
        if (isBasicInfoModified(name, indexSelectedType)) {

            val modificationInfos = ArrayMap<String, String>()
            val originUserInfo = _currentUserLiveData.value ?: return
            if (originUserInfo.name != name) {
                //check valid name
                if (!Validator.isNameValid(name, _errorNameLiveData)) {
                    return
                }
                modificationInfos["name"] = name
            }
            val originIndexUserType = _indexUserTypeSelected.value ?: return
            if (originIndexUserType != indexSelectedType) {
                modificationInfos["typeId"] = getUserTypeIdByIndex(indexSelectedType)
            }
            val newUserInfo = Pair(originUserInfo.firebaseUserId, modificationInfos)
            updateUserInfoUseCase.invoke(viewModelScope, newUserInfo, _stateUpdateBasicInfo)
        }

    }

    internal fun savePasswordModification(newPassword: String) {
        if (Validator.isPasswordValid(newPassword, _errorPasswordLiveData)) {
            val originUserInfo = _currentUserLiveData.value ?: return
            val changePassParam = ChangePasswordParam(
                originUserInfo.firebaseUserId,
                originUserInfo.accountName,
                newPassword
            )
            changeUserPassUseCase.invoke(viewModelScope, changePassParam, _resultChangePassword)
        }
    }

    private fun getUserTypeIdByIndex(index: Int): String {
        val userTypes = _userTypesLiveData.value?.data ?: return ""
        if (index < 0 || index >= userTypes.size) {
            return ""
        }
        return userTypes[index].id
    }

    private fun isBasicInfoModified(name: String, indexSelectedType: Int): Boolean {
        val originUserInfo = _currentUserLiveData.value ?: return false
        val originIndexUserType = _indexUserTypeSelected.value
        if (originUserInfo.name == name && originIndexUserType == indexSelectedType) {
            return false
        }
        return true
    }

    internal fun loadUserInfo() {
        _currentUserLiveData.value = currentUserInfo
    }

    internal fun loadUserTypes() {
        getUserTypesUseCase.invoke(viewModelScope, true, _userTypesLiveData)
    }

    internal fun showSelectedUserType(typeId: String) {
        Timber.d("TypeId = $typeId")
        val userTypesResource = _userTypesLiveData.value
        Timber.d("UserTypesData = ${userTypesResource.toString()}")
        userTypesResource?.data?.let {
            val indexOfType = it.indexOfFirst { userType ->
                val check = typeId == userType.id
                Timber.d("Founded TypeId = $typeId")
                check
            }

            _indexUserTypeSelected.value = indexOfType
        }
    }

}