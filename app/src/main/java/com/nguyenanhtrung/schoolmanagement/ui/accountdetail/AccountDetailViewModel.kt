package com.nguyenanhtrung.schoolmanagement.ui.accountdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.user.UpdateUserInfoUseCase
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject

class AccountDetailViewModel @Inject constructor(
    private val getUserTypesUseCase: GetUserTypesUseCase,
    private val updateUserInfoUseCase: UpdateUserInfoUseCase
) : BaseViewModel() {
    internal lateinit var accountDetailParams: AccountDetailParams
    internal var posUserTypeSelected = -1

    private val _accountDetailInfo by lazy {
        MutableLiveData<AccountDetailParams>()
    }
    internal val currentUserLiveData: LiveData<AccountDetailParams>
        get() = _accountDetailInfo

    private val _userTypesLiveData by lazy {
        createApiResultLiveData<List<UserType>>()
    }
    internal val userTypesLiveData: LiveData<Resource<List<UserType>>>
        get() = _userTypesLiveData

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


    private val _stateEditAccountInfo by lazy {
        MutableLiveData<ModificationState>()
    }
    internal val stateEditAccountInfo: LiveData<ModificationState>
        get() = _stateEditAccountInfo

    private val _stateSaveModifiedInfo by lazy {
        createApiResultLiveData<Unit>()
    }
    internal val stateSaveModifiedInfo: LiveData<Resource<Unit>>
        get() = _stateSaveModifiedInfo


    internal fun onClickButtonEdit() {
        _stateEditAccountInfo.value = ModificationState.Edit
    }

    internal fun onClickButtonSave(newName: String, newPassword: String) {
        val accountInfo = accountDetailParams.user
        val oldName = accountInfo.name
        val oldAccountTypeId = accountInfo.type.id
        val oldPassword = accountDetailParams.userDetail.password
        val newAccountTypeId = getUserTypeIdByIndex(posUserTypeSelected)

        if (oldName == newName && newAccountTypeId == oldAccountTypeId
            && oldPassword == newPassword
        ) {
            _stateEditAccountInfo.value = ModificationState.Save
            disableModifyInputs()
            return
        }

        val updateAccountInfoParams = UpdateAccountInfoParams()
        updateAccountInfoParams.fireBaseUserId = accountInfo.firebaseUserId
        if (oldName != newName) {
            if (!Validator.isNameValid(newName, _errorNameLiveData)) {
                return
            }
            updateAccountInfoParams.name = newName
        }

        if (oldPassword != newPassword && Validator.isPasswordValid(
                newPassword,
                _errorPasswordLiveData
            )
        ) {

            updateAccountInfoParams.password = newPassword
            updateAccountInfoParams.accountName = accountInfo.name
        }

        if (oldAccountTypeId != newAccountTypeId) {
            updateAccountInfoParams.typeId = newAccountTypeId
        }

        updateUserInfoUseCase.invoke(
            viewModelScope,
            updateAccountInfoParams,
            _stateSaveModifiedInfo
        )
    }

    private fun disableModifyInputs() {
        _stateEditAccountInfo.value = ModificationState.Save
    }

    internal fun onSuccessSaveModifiedAccountInfo() {
        disableModifyInputs()
        _messageLiveData.value = R.string.success_save_modified_account_info
    }

    private fun getUserTypeIdByIndex(index: Int): String {
        val userTypes = _userTypesLiveData.value?.data ?: return ""
        if (index < 0 || index >= userTypes.size) {
            return ""
        }
        return userTypes[index].id
    }

    private fun getCurrentUserType(): UserType {
        val userTypes = _userTypesLiveData.value?.data ?: return UserType("", "")
        if (posUserTypeSelected < 0 || posUserTypeSelected >= userTypes.size) {
            return UserType("", "")
        }
        return userTypes[posUserTypeSelected]
    }


    internal fun loadUserInfo() {
        _accountDetailInfo.value = accountDetailParams
    }

    internal fun loadUserTypes() {
        getUserTypesUseCase.invoke(viewModelScope, true, _userTypesLiveData)
    }

    internal fun getModifiedAccountInfo(accName: String): User {
        val oldAccInfo = accountDetailParams.user
        return User(
            oldAccInfo.id,
            oldAccInfo.firebaseUserId,
            accName,
            getCurrentUserType()
        )
    }

    internal fun getIndexOfSelectedUserType(): Int {
        val userTypesResource = _userTypesLiveData.value ?: return 0
        val userTypes = userTypesResource.data ?: return 0
        val selectedType = accountDetailParams.user.type
        val selectedTypeId = selectedType.id
        posUserTypeSelected = userTypes.indexOfFirst { userType ->
            val check = selectedTypeId == userType.id
            check
        }
        return posUserTypeSelected
    }

}