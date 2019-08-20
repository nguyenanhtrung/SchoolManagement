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
import javax.inject.Inject

class AccountDetailViewModel @Inject constructor(
    private val getUserTypesUseCase: GetUserTypesUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase,
    private val changeUserPassUseCase: ChangeUserPassUseCase
) : BaseViewModel() {
    internal lateinit var accountDetailParams: AccountDetailParams


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

    private val _stateModifyName by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateModifyName: LiveData<ModificationState>
        get() = _stateModifyName

    private val _stateModifyPassword by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateModifyPassword: LiveData<ModificationState>
        get() = _stateModifyPassword

    private val _stateModifyUserType by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateModifyUserType: LiveData<ModificationState>
        get() = _stateModifyUserType

    private val _stateEditAccountInfo by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateEditAccountInfo: LiveData<ModificationState>
        get() = _stateEditAccountInfo


    internal fun onClickButtonEdit() {
        _stateModifyName.value = ModificationState.Edit
        _stateModifyPassword.value = ModificationState.Edit
        _stateModifyUserType.value = ModificationState.Edit
        _stateEditAccountInfo.value = ModificationState.Edit
    }

    internal fun onClickButtonSave(name: String, indexSelectedType: Int, password: String) {

    }

    private fun isBasicInfoModified(name: String, indexSelectedType: Int): Boolean {
        val originUserInfo = _currentUserLiveData.value ?: return false
        val originIndexUserType = _indexUserTypeSelected.value
        if (originUserInfo.name == name && originIndexUserType == indexSelectedType) {
            return false
        }
        return true
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
            //updateUserInfoUseCase.invoke(viewModelScope, newUserInfo, _stateUpdateBasicInfo)
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
            //  changeUserPassUseCase.invoke(viewModelScope, changePassParam, _resultChangePassword)
        }
    }

    private fun getUserTypeIdByIndex(index: Int): String {
        val userTypes = _userTypesLiveData.value?.data ?: return ""
        if (index < 0 || index >= userTypes.size) {
            return ""
        }
        return userTypes[index].id
    }



    internal fun loadUserInfo() {
        _currentUserLiveData.value = accountDetailParams.user
    }

    internal fun loadUserTypes() {
        getUserTypesUseCase.invoke(viewModelScope, true, _userTypesLiveData)
    }

    internal fun showSelectedUserType(typeId: String) {
        val userTypesResource = _userTypesLiveData.value
        userTypesResource?.data?.let {
            val indexOfType = it.indexOfFirst { userType ->
                val check = typeId == userType.id
                check
            }

            _indexUserTypeSelected.value = indexOfType
        }
    }

}