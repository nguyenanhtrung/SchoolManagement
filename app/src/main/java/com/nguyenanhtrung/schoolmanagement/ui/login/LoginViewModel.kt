package com.nguyenanhtrung.schoolmanagement.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nguyenanhtrung.schoolmanagement.R
import com.nguyenanhtrung.schoolmanagement.data.local.model.*
import com.nguyenanhtrung.schoolmanagement.domain.login.LoginUseCase
import com.nguyenanhtrung.schoolmanagement.domain.usertype.GetUserTypesUseCase
import com.nguyenanhtrung.schoolmanagement.ui.base.BaseActivityViewModel
import com.nguyenanhtrung.schoolmanagement.util.Validator
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getUserTypesUseCase: GetUserTypesUseCase
) : BaseActivityViewModel() {

    private val _emailErrorLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val emailErrorLiveData: LiveData<ErrorState>
        get() = _emailErrorLiveData

    private val _passwordErrorLiveData by lazy {
        MutableLiveData<ErrorState>()
    }
    internal val passwordErrorLiveData: LiveData<ErrorState>
        get() = _passwordErrorLiveData

    private val _showForgotPasswordDialog by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val showForgotPasswordDialog: LiveData<Event<Boolean>>
        get() = _showForgotPasswordDialog

    private val _loginResultLiveData by lazy {
        createApiResultLiveData<Boolean>()
    }
    internal val loginResultLiveData: LiveData<Resource<Boolean>>
        get() = _loginResultLiveData

    private val _mainScreenLiveData by lazy {
        MutableLiveData<Event<Boolean>>()
    }
    internal val mainScreenLiveData: LiveData<Event<Boolean>>
        get() = _mainScreenLiveData

    private val _userTypesLiveData by lazy {
        createApiResultLiveData<List<UserType>>()
    }
    internal val userTypeLiveData: LiveData<Resource<List<UserType>>>
        get() = _userTypesLiveData


    internal fun onClickButtonLogin(email: String, password: String) {
        val isLoginValid = Validator.isEmailValid(
            email,
            _emailErrorLiveData
        ) && Validator.isPasswordValid(password, _passwordErrorLiveData)
        if (isLoginValid) {
            //call to login
            val loginPair = email to password
            loginUseCase.invoke(viewModelScope, loginPair, _loginResultLiveData)
        }
    }

    internal fun onClickTextForgotPassword() {
        _showForgotPasswordDialog.value = Event(true)
    }

    internal fun onCheckLogin(isLoginSuccess: Boolean) {
        if (isLoginSuccess) {
            //open main screen
            getUserTypesUseCase.invoke(viewModelScope, false, _userTypesLiveData)
        } else {
            //show login error
            showError(ErrorState.NoAction(R.string.error_wrong_login))
        }
    }

    internal fun onCompletedLoadUserTypes() {
        _mainScreenLiveData.value = Event(true)
    }

}