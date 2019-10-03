package com.nguyenanhtrung.schoolmanagement.ui.createaccount

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.user.CreateUserUseCase
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject

class CreateAccountViewModel @Inject constructor(
    private val getUserTypesUseCase: GetUserTypesUseCase,
    private val createUserUseCase: CreateUserUseCase
) :
    BaseViewModel() {

    internal var maxId: Long = 0
    internal var posUserTypeSelected = 0
    var newFireBaseId = ""
    private lateinit var createAccountInput: CreateAccountInput

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

    private val _errorEmailLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val errorEmailLiveData: LiveData<ErrorState>
        get() = _errorEmailLiveData

    private val _errorPasswordLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val errorPasswordLiveData: LiveData<ErrorState>
        get() = _errorPasswordLiveData

    private val _createUserLiveData by lazy {
        createApiResultLiveData<String>()
    }
    internal val createUserLiveData: LiveData<Resource<String>>
        get() = _createUserLiveData


    internal fun loadUserTypes() {
        if (_userTypesLiveData.value?.data != null) {
            return
        }
        getUserTypesUseCase.invoke(viewModelScope, true, _userTypesLiveData)
    }

    internal fun onClickButtonConfirm(createAccountInput: CreateAccountInput) {
        this.createAccountInput = createAccountInput

        val isNameValid = Validator.isNameValid(createAccountInput.fullName, _errorNameLiveData)
        if (!isNameValid) {
            return
        }

        val isEmailValid = Validator.isEmailValid(createAccountInput.email, _errorEmailLiveData)
        if (!isEmailValid) {
            return
        }

        val isPasswordValid =
            Validator.isPasswordValid(createAccountInput.password, _errorPasswordLiveData)
        if (!isPasswordValid) {
            return
        }

        val userTypeIdSelected = getUserTypeId()
        increaseUserId()
        val createAccountParam = CreateAccountParam(
            maxId.toString(),
            createAccountInput.fullName,
            createAccountInput.email,
            createAccountInput.password,
            userTypeIdSelected
        )
        createUserUseCase.invoke(viewModelScope, createAccountParam, _createUserLiveData)

    }

    internal fun createProfileUpdateArguments(): ProfileUpdateArguments {
        val userType = UserType(getUserTypeId(), getUserTypeName())
        return ProfileUpdateArguments(
            newFireBaseId,
            maxId,
            userType,
            createAccountInput.fullName
        )
    }

    private fun increaseUserId() {
        maxId++
    }

    internal fun getUserTypeName(): String {
        val userTypes = _userTypesLiveData.value?.data
        val userTypeSelected = userTypes?.get(posUserTypeSelected)
        return userTypeSelected?.name ?: return ""
    }

    internal fun getUserTypeId(): String {
        val userTypes = _userTypesLiveData.value?.data
        val userTypeSelected = userTypes?.get(posUserTypeSelected)
        return userTypeSelected?.id ?: return ""
    }
}