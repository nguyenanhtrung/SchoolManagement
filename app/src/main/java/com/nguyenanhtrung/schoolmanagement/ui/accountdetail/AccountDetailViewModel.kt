package com.nguyenanhtrung.schoolmanagement.ui.accountdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.ModificationState
import com.nguyenanhtrung.schoolmanagement.data.local.model.Resource
import com.nguyenanhtrung.schoolmanagement.data.local.model.User
import com.nguyenanhtrung.schoolmanagement.data.local.model.UserType
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import javax.inject.Inject

class AccountDetailViewModel @Inject constructor(private val getUserTypesUseCase: GetUserTypesUseCase) : BaseViewModel() {
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

    

    internal fun loadUserInfo() {
        _currentUserLiveData.value = currentUserInfo
    }

    internal fun loadUserTypes() {
        getUserTypesUseCase.invoke(viewModelScope, true, _userTypesLiveData)
    }

    internal fun showSelectedUserType(typeId: String) {
        val userTypesResource = _userTypesLiveData.value
        userTypesResource?.data?.let {
            val indexOfType = it.indexOfFirst { userType ->
                typeId == userType.id
            }
            _indexUserTypeSelected.value = indexOfType
        }
    }

}